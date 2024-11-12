package com.demo.transactionservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.transactionservice.dto.TransactionResponseDTO;
import com.demo.transactionservice.entity.Transaction;
import com.demo.transactionservice.repository.TransactionRepository;

@Service
public class TransactionService {


	 @Autowired
	 private TransactionRepository transactionRepository;
	
	
	public Transaction initiateTransaction(Long accountId, BigDecimal amount) {
	   
		Transaction transaction = new Transaction();
	    transaction.setAccountId(accountId);
	    transaction.setAmount(amount);
	    transaction.setTransactionDate(LocalDateTime.now());

	    return transactionRepository.save(transaction);
	 }
	
	public List<TransactionResponseDTO> fetchTransaction(Long accountId) {
		 
		List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

		return transactions.stream()
	            .map(transaction -> new TransactionResponseDTO(
	                transaction.getTranId(),
	                transaction.getAmount(),
	                transaction.getTransactionDate()))
	            .collect(Collectors.toList());
	 }
}
