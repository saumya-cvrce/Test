package com.demo.transactionservice.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.transactionservice.dto.TransactionRequestDTO;
import com.demo.transactionservice.dto.TransactionResponseDTO;
import com.demo.transactionservice.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
    private TransactionService transactionService;
	
	
	@PostMapping("/initiate")
    public void initiateTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        if (transactionRequest.getAccountId() == null || transactionRequest.getAmount() == null || transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transaction request. Account ID and amount must be provided and amount must have positive value");
        }

        transactionService.initiateTransaction(transactionRequest.getAccountId(), transactionRequest.getAmount());
    }
	
	@GetMapping("/fetch")
	public List<TransactionResponseDTO> fetchTransaction(@RequestParam Long accountId) {
		
		return transactionService.fetchTransaction(accountId);
	}

}
