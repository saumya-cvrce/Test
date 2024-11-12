package com.demo.accountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.accountservice.dto.AccountRequestDTO;
import com.demo.accountservice.dto.AccountInfoResponseDTO;
import com.demo.accountservice.entity.Account;
import com.demo.accountservice.exception.CustomerNotFoundException;
import com.demo.accountservice.service.AccountService;

@RestController
@RequestMapping("/api/customer")
public class AccountController {

	@Autowired
	AccountService accountService;

	@PostMapping("/openAccount")
	public ResponseEntity<?> openNewAccount(@RequestBody AccountRequestDTO accountDTO) {

		Account account = accountService.openNewAccountForCustomer(accountDTO.getCustomerId(),
				accountDTO.getInitialCredit());
		return new ResponseEntity<>(account, HttpStatus.CREATED);

	}
	
	@GetMapping("/fetchAccount")
	public ResponseEntity<?> getUserInfo(@RequestParam String customerId) {

		AccountInfoResponseDTO accountInfo = accountService.getUserInformation(customerId);
		return new ResponseEntity<>(accountInfo, HttpStatus.OK);

	}
}
