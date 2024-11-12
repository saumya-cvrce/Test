package com.demo.accountservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.demo.accountservice.dto.*;
import com.demo.accountservice.entity.Account;
import com.demo.accountservice.entity.Customer;
import com.demo.accountservice.exception.CustomerNotFoundException;
import com.demo.accountservice.repository.AccountRepository;
import com.demo.accountservice.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(accountService, "transactionServiceUrl", "http://mocked-transaction-service-url/");
    }

    @Test
    void openNewAccountForCustomer_success() {
        String customerId = "12345";
        BigDecimal initialCredit = BigDecimal.valueOf(100.00);
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Account savedAccount = new Account();
        savedAccount.setAccountID(1L);
        savedAccount.setBalance(initialCredit);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Account result = accountService.openNewAccountForCustomer(customerId, initialCredit);

        assertNotNull(result);
        assertEquals(savedAccount.getAccountID(), result.getAccountID());
        assertEquals(initialCredit, result.getBalance());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(TransactionRequestDTO.class), eq(Void.class));
    }

    @Test
    void openNewAccountForCustomer_customerNotFound() {
        String customerId = "12345";
        BigDecimal initialCredit = BigDecimal.valueOf(100.00);
        
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.openNewAccountForCustomer(customerId, initialCredit));
    }

    @Test
    void fetchTransaction_success() {
        Long accountId = 1L;
        TransactionResponseDTO transaction = new TransactionResponseDTO();
        transaction.setTransactionId(1L);
        transaction.setAmount(BigDecimal.valueOf(100.00));
        List<TransactionResponseDTO> transactions = Collections.singletonList(transaction);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(transactions));

        List<TransactionResponseDTO> result = accountService.fetchTransaction(accountId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction.getTransactionId(), result.get(0).getTransactionId());
    }

    @Test
    void getUserInformation_success() {
        String customerId = "12345";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("David");
        customer.setSurname("Miller");

        Account account = new Account();
        account.setAccountID(1L);
        account.setBalance(BigDecimal.valueOf(100.00));
        account.setCustomer(customer);

        TransactionResponseDTO transaction = new TransactionResponseDTO();
        transaction.setTransactionId(1L);
        transaction.setAmount(BigDecimal.valueOf(100.00));

        List<TransactionResponseDTO> transactions = Collections.singletonList(transaction);
        List<Account> accounts = Collections.singletonList(account);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomer_CustomerId(customerId)).thenReturn(accounts);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(transactions));

        AccountInfoResponseDTO result = accountService.getUserInformation(customerId);

        assertNotNull(result);
        assertEquals("David", result.getFirstName());
        assertEquals("Miller", result.getLastName());
        assertEquals(1, result.getAccount().size());
        assertEquals(transactions.size(), result.getAccount().get(0).getTransactionDTO().size());
    }

    @Test
    void getUserInformation_customerNotFound() {
        String customerId = "12345";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.getUserInformation(customerId));
    }
}

