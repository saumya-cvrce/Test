package com.demo.accountservice.dto;

import java.math.BigDecimal;

public class TransactionRequestDTO {

	private Long accountId;
	private BigDecimal amount;
	
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
