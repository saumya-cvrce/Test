package com.demo.accountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.accountservice.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByCustomer_CustomerId(String customerId);
}
