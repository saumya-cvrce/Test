package com.demo.transactionservice.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.transactionservice.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccountId(Long accountId);
	
}
