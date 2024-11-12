package com.demo.accountservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountResponseDTO {
	
	private Long accountID;
	private BigDecimal balance;
	private List<TransactionResponseDTO> transactionDTO;
	
	
	public Long getAccountID() {
		return accountID;
	}
	public void setAccountID(Long accountID) {
		this.accountID = accountID;
	}
	public List<TransactionResponseDTO> getTransactionDTO() {
		return transactionDTO;
	}
	public void setTransactionDTO(List<TransactionResponseDTO> transactionDTO) {
		this.transactionDTO = transactionDTO;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	

}
