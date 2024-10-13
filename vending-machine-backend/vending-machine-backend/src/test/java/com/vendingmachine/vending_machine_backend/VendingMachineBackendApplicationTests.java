package com.vendingmachine.vending_machine_backend;

import com.vendingmachine.vending_machine_backend.model.Item;
import com.vendingmachine.vending_machine_backend.model.ItemQuantity;
import com.vendingmachine.vending_machine_backend.model.PurchaseRequest;
import com.vendingmachine.vending_machine_backend.repository.ItemRepository;
import com.vendingmachine.vending_machine_backend.repository.TransactionRepository;
import com.vendingmachine.vending_machine_backend.service.VendingMachineService;
import com.vendingmachine.vending_machine_backend.utility.PettyCashManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VendingMachineBackendApplicationTests {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PettyCashManager pettyCashManager;

    @InjectMocks
    private VendingMachineService vendingMachineService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseItems_Success() {
        // Define items to purchase
        ItemQuantity sodaQuantity = new ItemQuantity();
        sodaQuantity.setName("Soda");
        sodaQuantity.setQuantity(2);

        ItemQuantity chipsQuantity = new ItemQuantity();
        chipsQuantity.setName("Chips");
        chipsQuantity.setQuantity(1);

        List<ItemQuantity> itemsToPurchase = Arrays.asList(sodaQuantity, chipsQuantity);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setItems(itemsToPurchase);
        purchaseRequest.setPayment(50); // Assume payment is 50

        // Mock item repository responses
        Item soda = new Item();
        soda.setName("Soda");
        soda.setPrice(10);
        soda.setQuantity(5);

        Item chips = new Item();
        chips.setName("Chips");
        chips.setPrice(15);
        chips.setQuantity(3);

        when(itemRepository.findByName("Soda")).thenReturn(soda);
        when(itemRepository.findByName("Chips")).thenReturn(chips);
        when(pettyCashManager.isChangeAvailable(15)).thenReturn(true);

        String result = vendingMachineService.purchaseItems(purchaseRequest);

        assertEquals("Purchase successful for item: Soda\nPurchase successful for item: Chips\nTotal change returned: 15", result);
        verify(itemRepository, times(1)).save(soda);
        verify(itemRepository, times(1)).save(chips);
        verify(transactionRepository, times(1)).save(any());
        verify(pettyCashManager, times(1)).dispenseChange(15);
    }

    @Test
    public void testPurchaseItems_InsufficientPayment() {
        // Define items to purchase
        ItemQuantity sodaQuantity = new ItemQuantity();
        sodaQuantity.setName("Soda");
        sodaQuantity.setQuantity(1);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setItems(Arrays.asList(sodaQuantity));
        purchaseRequest.setPayment(5); // Not enough payment

        // Mock item repository responses
        Item soda = new Item();
        soda.setName("Soda");
        soda.setPrice(10);
        soda.setQuantity(5);

        when(itemRepository.findByName("Soda")).thenReturn(soda);

        String result = vendingMachineService.purchaseItems(purchaseRequest);

        assertEquals("Insufficient payment. Total cost is: 10, but received: 5", result);
        verify(itemRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testPurchaseItems_ItemOutOfStock() {
        // Define items to purchase
        ItemQuantity waterQuantity = new ItemQuantity();
        waterQuantity.setName("Water");
        waterQuantity.setQuantity(1);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setItems(Arrays.asList(waterQuantity));
        purchaseRequest.setPayment(100);

        // Mock item repository responses
        Item water = new Item();
        water.setName("Water");
        water.setPrice(20);
        water.setQuantity(0); // Out of stock

        when(itemRepository.findByName("Water")).thenReturn(water);

        String result = vendingMachineService.purchaseItems(purchaseRequest);

        assertEquals("Not enough stock for item: Water", result);
        verify(itemRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testPurchaseItems_UnableToProvideChange() {
        // Define items to purchase
        ItemQuantity sodaQuantity = new ItemQuantity();
        sodaQuantity.setName("Soda");
        sodaQuantity.setQuantity(1);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setItems(Arrays.asList(sodaQuantity));
        purchaseRequest.setPayment(20); // Payment that requires change

        // Mock item repository responses
        Item soda = new Item();
        soda.setName("Soda");
        soda.setPrice(10);
        soda.setQuantity(5);

        when(itemRepository.findByName("Soda")).thenReturn(soda);
        when(pettyCashManager.isChangeAvailable(10)).thenReturn(false); // No change available

        String result = vendingMachineService.purchaseItems(purchaseRequest);

        assertEquals("Unable to provide change!", result);
        verify(itemRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    public void testAddItem_Success() {
        Item item = new Item();
        item.setName("Water");
        item.setPrice(10);
        item.setQuantity(20);

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = vendingMachineService.addItem(item);

        assertEquals("Water", result.getName());
        assertEquals(10, result.getPrice());
        assertEquals(20, result.getQuantity());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void testAddItem_DuplicateName() {
        Item item = new Item();
        item.setName("Soda");
        item.setPrice(15);
        item.setQuantity(10);

        when(itemRepository.findByName("Soda")).thenReturn(item);
        Item newItem = new Item();
        newItem.setName("Soda");
        newItem.setPrice(20);
        newItem.setQuantity(5);

        Item result = vendingMachineService.addItem(newItem);

        // The current logic is to just save the item regardless.
        verify(itemRepository, times(1)).save(newItem);
    }
}
