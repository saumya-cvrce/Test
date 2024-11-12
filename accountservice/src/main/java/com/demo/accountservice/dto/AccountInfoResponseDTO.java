package com.demo.accountservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountInfoResponseDTO {

	private String firstName;
	private String lastName;
    private List<AccountResponseDTO> account;
    
    public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<AccountResponseDTO> getAccount() {
		return account;
	}
	public void setAccount(List<AccountResponseDTO> account) {
		this.account = account;
	}
	
}
