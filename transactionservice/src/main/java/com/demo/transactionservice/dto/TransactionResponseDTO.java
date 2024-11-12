package com.demo.transactionservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

	private Long transactionId;
	private BigDecimal amount;
    private LocalDateTime transactionDate;
    
    public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public TransactionResponseDTO(Long transactionId, BigDecimal amount, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }
	
}
