package com.vendingmachine.vending_machine_backend.utility;

import com.vendingmachine.vending_machine_backend.model.Currency;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PettyCashManager {

    private static final Logger logger = LoggerFactory.getLogger(PettyCashManager.class);
    private Map<Integer, Currency> pettyCash;

    public PettyCashManager() {
        pettyCash = new HashMap<>();
        pettyCash.put(5, new Currency(5, 10));  // 10 notes of R5
        pettyCash.put(10, new Currency(10, 10)); // 10 notes of R10
        pettyCash.put(20, new Currency(20, 10)); // 10 notes of R20
        logger.info("Petty cash initialized with R5, R10, R20 denominations");
    }

    public boolean isChangeAvailable(int change) {
        int remainingChange = change;
        Map<Integer, Integer> denominationsUsed = new HashMap<>();

        for (Integer denomination : pettyCash.keySet()) {
            int countAvailable = pettyCash.get(denomination).getCount();
            int countNeeded = remainingChange / denomination;

            if (countNeeded > 0) {
                int countToUse = Math.min(countAvailable, countNeeded);
                denominationsUsed.put(denomination, countToUse);
                remainingChange -= countToUse * denomination;
            }
        }

        boolean canProvideChange = remainingChange == 0;
        logger.info("Change availability check for {}: {}", change, canProvideChange);
        return canProvideChange;
    }

    public void dispenseChange(int change) {
        int remainingChange = change;

        for (Integer denomination : pettyCash.keySet()) {
            int countAvailable = pettyCash.get(denomination).getCount();
            int countNeeded = remainingChange / denomination;

            if (countNeeded > 0) {
                int countToUse = Math.min(countAvailable, countNeeded);
                pettyCash.get(denomination).setCount(countAvailable - countToUse);
                remainingChange -= countToUse * denomination;
                logger.info("Dispensing {} notes of denomination R{}", countToUse, denomination);
            }
        }

        logger.info("Total change of {} dispensed successfully", change);
    }
}
