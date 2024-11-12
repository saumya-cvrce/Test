package com.demo.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.accountservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    
}
