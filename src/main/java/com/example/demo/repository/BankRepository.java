package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Bank;
import java.util.List;


public interface BankRepository extends JpaRepository<Bank, Long> {
	
	public Bank findByAccountNo(String accountNo);

}
