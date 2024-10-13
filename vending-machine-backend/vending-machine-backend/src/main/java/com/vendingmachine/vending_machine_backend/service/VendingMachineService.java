package com.vendingmachine.vending_machine_backend.service;

import com.vendingmachine.vending_machine_backend.model.Item;
import com.vendingmachine.vending_machine_backend.model.ItemQuantity;
import com.vendingmachine.vending_machine_backend.model.PurchaseRequest;
import com.vendingmachine.vending_machine_backend.model.Transaction;
import com.vendingmachine.vending_machine_backend.repository.ItemRepository;
import com.vendingmachine.vending_machine_backend.repository.TransactionRepository;
import com.vendingmachine.vending_machine_backend.utility.PettyCashManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class VendingMachineService {

     private static final Logger logger = LoggerFactory.getLogger(VendingMachineService.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PettyCashManager pettyCashManager;

    public Iterable<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public String purchaseItems(PurchaseRequest purchaseRequest) {
        List<ItemQuantity> itemsToPurchase = purchaseRequest.getItems();
        int totalPayment = purchaseRequest.getPayment();
        int totalCost = 0;
        StringBuilder response = new StringBuilder();

        // Calculate total cost and check availability
        for (ItemQuantity itemQuantity : itemsToPurchase) {    
            Item item = itemRepository.findByName(itemQuantity.getName());
            
            if (item == null) {
                return "Item not found: " + itemQuantity.getName();
            }
            logger.info("Searching for item: {}", item.getName());

            if (item.getQuantity() < itemQuantity.getQuantity()) {
                return "Not enough stock for item: " + itemQuantity.getName();
            }
            logger.error("Item not found: {}", item.getName());

            totalCost += itemQuantity.getQuantity() * item.getPrice();
        }

        // Check if total payment is enough
        if (totalPayment < totalCost) {
            return "Insufficient payment. Total cost is: " + totalCost + ", but received: " + totalPayment;
        }

        // Check if change can be provided
        int change = totalPayment - totalCost;
        if (change > 0 && !pettyCashManager.isChangeAvailable(change)) {
            return "Unable to provide change!";
        }

        // Process the purchase if all checks pass
        for (ItemQuantity itemQuantity : itemsToPurchase) {
            Item item = itemRepository.findByName(itemQuantity.getName());
            item.setQuantity(item.getQuantity() - itemQuantity.getQuantity());
            itemRepository.save(item);
            response.append("Purchase successful for item: ").append(itemQuantity.getName()).append("\n");
        }

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setItems(itemsToPurchase);
        transaction.setTotalCost(totalCost);
        transaction.setPayment(totalPayment);
        transaction.setChange(change);
        transactionRepository.save(transaction);

        // Dispense change if required
        if (change > 0) {
            pettyCashManager.dispenseChange(change);
        }

        response.append("Total change returned: ").append(change);
        return response.toString().trim();
    }
}
