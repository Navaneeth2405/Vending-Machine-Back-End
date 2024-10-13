package com.vendingmachine.vending_machine_backend.repository;

import com.vendingmachine.vending_machine_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
