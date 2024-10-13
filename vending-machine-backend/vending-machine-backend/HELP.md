# Vending Machine Backend

## Overview
The Vending Machine Backend is a Spring Boot-based application that simulates a vending machine. It provides REST APIs for purchasing items, handling payments using known currency denominations, managing petty cash, and ensuring proper change issuance.

## Technologies Used
- **Spring Boot**: For building a Java backend with REST APIs.
- **Java 8**: As the programming language.
- **Hibernate (JPA)**: For persistence and database management.
- **H2 In-Memory Database**: For easy database management and prototyping.
- **JUnit 5**: For unit testing.
- **SLF4J/Logback**: For logging.

## Requirements and How We Achieved Them

### 1. **Startup with Preconfigured Items and Petty Cash**
   - The application initializes with preconfigured items using the `data.sql` script.
   - Petty cash is managed by the `PettyCashManager` class, with initial values configured during its instantiation.

### 2. **Allow a User to Add and Purchase Items**
   - REST endpoint `/api/vending/items` (GET) allows users to **view all items**.
   - REST endpoint `/api/vending/items` (POST) allows **adding new item** to the vending machine.
   - Users can **purchase multiple items in a single request** using the `/api/vending/purchase` endpoint, which now supports multiple items and quantities for each item.

### 3. **Facilitate Payment and Store Petty Cash by Known Currency Denominations**
   - Payments are processed in known denominations (e.g., R5, R10, R20), facilitated by the `purchaseItems()` method in `VendingMachineService`.
   - Petty cash is adjusted after every transaction to maintain proper inventory of available denominations.

### 4. **Issue Change by Known Currency Denominations**
   - The `PettyCashManager` class is responsible for issuing change.
   - The application ensures change availability before confirming the purchase, and dispenses change using the available petty cash denominations.

### 5. **Adjust Available Quantity of Items and Petty Cash**
   - Item quantities and petty cash are adjusted after each successful transaction.
   - If an item runs out of stock or if petty cash runs out, the purchase request will be denied accordingly.

### 6. **Exception Handling**
   - Handled exceptions for:
     - Items being out of stock.
     - Insufficient payment.
     - Insufficient change availability.
   - Clear and user-friendly responses are returned to guide the user in each scenario.

### 7. **Logging**
   - **SLF4J** with **Logback** is used for logging, providing `INFO`, `WARN`, and `ERROR` level logs throughout key processes.
   - Logs are used for debugging, tracking operations, and monitoring issues.

## REST API Endpoints

### 1. Get All Items
**Endpoint**: `GET /api/vending/items`  
**Description**: Fetches all items available in the vending machine, including their name, price, and available quantity.  
**Response**:
```json
[
  {
    "id": 1,
    "name": "Soda",
    "price": 15,
    "quantity": 10
  },
  {
    "id": 2,
    "name": "Chips",
    "price": 10,
    "quantity": 5
  }
]
```

### 2. Add an Item
**Endpoint**: `POST /api/vending/items`  
**Description**: Adds item to the vending machine inventory.  
**Request Body** (Single Item):
```json
{
  "name": "Water",
  "price": 10,
  "quantity": 15
}

```

### 3. Purchase Multiple Items
**Endpoint**: `POST /api/vending/purchase`  
**Description**: Purchases multiple items and returns change if the payment is sufficient.  
**Request Body**:
```json
{
  "payment": 50,
  "items": [
    {
      "itemName": "Soda",
      "quantity": 2
    },
    {
      "itemName": "Chips",
      "quantity": 3
    }
  ]
}
```
**Explanation**:
- **payment**: The total amount being paid for the items.
- **items**: A list of items being purchased, with the name and quantity for each item.

**Example Response (Successful Purchase)**:
```json
{
  "message": [
    "Purchase successful for item: Soda",
    "Purchase successful for item: Chips"
  ],
  "totalChange": 15
}
```

**Example Response (Insufficient Payment)**:
```json
"Insufficient payment. Total cost is: 60, but received: 50"
```

**Example Response (Item Out of Stock)**:
```json
"Not enough stock for item: Water"
```

## Summary of Endpoints
| Endpoint               | Method | Description                             |
|------------------------|--------|-----------------------------------------|
| `/api/vending/items`   | `GET`  | Get a list of all available items.      |
| `/api/vending/items`   | `POST` | Add one or many items to the vending machine.     |
| `/api/vending/purchase`| `POST` | Purchase multiple items by specifying their names, quantities, and payment amount. |

---

