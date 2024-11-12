package com.demo.accountservice.dto;

import java.math.BigDecimal;

public class AccountRequestDTO {
	
	private String customerId;
	private String name;
	private String surname;
	private BigDecimal initialCredit;
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public BigDecimal getInitialCredit() {
		return initialCredit;
	}
	public void setInitialCredit(BigDecimal initialCredit) {
		this.initialCredit = initialCredit;
	}
	

}
