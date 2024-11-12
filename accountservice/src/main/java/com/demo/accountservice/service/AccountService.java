package com.demo.accountservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.accountservice.dto.AccountInfoResponseDTO;
import com.demo.accountservice.dto.AccountResponseDTO;
import com.demo.accountservice.dto.TransactionRequestDTO;
import com.demo.accountservice.dto.TransactionResponseDTO;
import com.demo.accountservice.entity.Account;
import com.demo.accountservice.entity.Customer;
import com.demo.accountservice.exception.CustomerNotFoundException;
import com.demo.accountservice.repository.AccountRepository;
import com.demo.accountservice.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Value("${transactionservice.url}")
	private String transactionServiceUrl;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RestTemplate restTemplate;

	/** @Create a new ACCOUNT **/
	@Transactional
	public Account openNewAccountForCustomer(String customerId, BigDecimal initialCredit) {

		Optional<Customer> existingAccount = customerRepository.findById(customerId);

		try {

			if (existingAccount.isEmpty()) {
				throw new CustomerNotFoundException("Customer does not exist, account can not be created");
			}

			Account account = new Account();
			account.setCustomer(existingAccount.get());
			//ToDo Update the previous balance if tx for same account, out of scope for this scenario
			account.setBalance(initialCredit);
			 
			Account savedAccount = accountRepository.save(account);

			if (customerId != null && initialCredit != null && initialCredit.compareTo(BigDecimal.ZERO) > 0) {
				account.setAccountID(savedAccount.getAccountID());
				// Initiate a transaction to Transaction micro service
				initiateTransaction(account, initialCredit);
			}
			return savedAccount;

		} catch (CustomerNotFoundException e) {
			logger.error("Customer not found: {}", e.getMessage());
			throw e;
		} catch (ResourceAccessException e) {
			logger.error("Error during communication with transaction service", e);
			throw new RuntimeException("Account creation failed due to communication issues with the service. Please try again later.");
		} catch (Exception e) {
			logger.error("Error during account creation or transaction", e);
			throw new RuntimeException("Account creation failed due to an unexpected server issue. Please try again later.");
		}
	}

	private void initiateTransaction(Account account, BigDecimal amount) {

		TransactionRequestDTO transactionRequest = new TransactionRequestDTO();
		transactionRequest.setAccountId(account.getAccountID());
		transactionRequest.setAmount(amount);

		// Send a request to the Transaction microservice to create a transaction
		restTemplate.postForEntity(transactionServiceUrl+"initiate", transactionRequest, Void.class);
	}
	
	public List<TransactionResponseDTO> fetchTransaction(Long accountId) {

		try{
			// Send a request to the Transaction micro service to fetch a transaction
		
		String urlWithParams = UriComponentsBuilder.fromHttpUrl(transactionServiceUrl + "fetch")
                .queryParam("accountId", accountId)
                .toUriString();
		
		ResponseEntity<List<TransactionResponseDTO>> response = restTemplate.exchange(
				urlWithParams,
	            HttpMethod.GET,
	            null,
	            new ParameterizedTypeReference<List<TransactionResponseDTO>>() {}
	            );
		
		return response.getBody();
	}
	 catch(Exception e) {
		 throw new RuntimeException("Unable to retrieve the transaction due to communication issues with the service. Please try again later.");
	 }
	}
	
	/**Fetch the account information**/
	public AccountInfoResponseDTO getUserInformation(String customerId) {
		
		Optional<Customer> existingAccount = customerRepository.findById(customerId);
		AccountInfoResponseDTO accountInfo = new AccountInfoResponseDTO();
		if (existingAccount.isEmpty()) {
			throw new CustomerNotFoundException("Customer does exist, account info can not be displayed");
		}
		
		List<Account> accounts = accountRepository.findByCustomer_CustomerId(customerId);
		if (accounts.isEmpty()) {
			throw new RuntimeException("No account found for the Customer with customer ID:"+customerId);
        }
		
		accountInfo.setFirstName(existingAccount.get().getName());
		accountInfo.setLastName(existingAccount.get().getSurname());
		List<AccountResponseDTO> accountList = new ArrayList<AccountResponseDTO>();
		Long accountId = 0L;
		
		//Considering only single account for now, this can be further enhanced to the requirement
		for (Account account : accounts) {
			
			accountId = account.getAccountID();
			
			AccountResponseDTO accountDTO = new AccountResponseDTO();
			accountDTO.setAccountID(accountId);
			accountDTO.setBalance(account.getBalance());
			
			List<TransactionResponseDTO> transactions = fetchTransaction(accountId);
			if(transactions != null && transactions.size()>0) {
				accountDTO.setTransactionDTO(transactions);
			}
			accountList.add(accountDTO);
			accountInfo.setAccount(accountList);
		}
		return accountInfo;
	}
}
