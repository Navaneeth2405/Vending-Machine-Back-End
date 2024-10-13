package com.vendingmachine.vending_machine_backend.controller;

import com.vendingmachine.vending_machine_backend.model.Item;
import com.vendingmachine.vending_machine_backend.model.PurchaseRequest;
import com.vendingmachine.vending_machine_backend.service.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/vending")
public class VendingMachineController {

    private static final Logger logger = LoggerFactory.getLogger(VendingMachineController.class);

    @Autowired
    private VendingMachineService vendingMachineService;

    @GetMapping("/items")
    public Iterable<Item> getAllItems() {
        logger.info("Received request to fetch all items");
        return vendingMachineService.getAllItems();
    }

    @PostMapping("/purchase")
    public String purchaseItems(@RequestBody PurchaseRequest purchaseRequest) {
        logger.info("Received purchase request for multiple items: {}", purchaseRequest);
        return vendingMachineService.purchaseItems(purchaseRequest);
    }

    @PostMapping("/items")
    public Item addItem(@RequestBody Item item) {
        logger.info("Received request to add a new item: {}", item);
        return vendingMachineService.addItem(item);
    }
}




