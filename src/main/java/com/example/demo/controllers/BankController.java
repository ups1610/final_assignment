package com.example.demo.controllers;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Bank;
import com.example.demo.repository.BankRepository;

@RestController
@RequestMapping("/bank")
public class BankController {
	
	@Autowired
	BankRepository bankRepository;
	
	@GetMapping("/all")
	public List<Bank> getAllData()
	{
		return bankRepository.findAll();
	}
	
	@PostMapping("/register")
	public Bank register(@RequestBody Bank bank) {
		Random random = new Random();
		String accountNo = String.format("%06d", random.nextInt(1000000));
		bank.setAccountNo(accountNo);
		return bankRepository.save(bank);
    }
	
	@PutMapping("/deposit/{accountNo}/{ifsc}/{amount}")
	public void deposit(@PathVariable String accountNo, @PathVariable String ifsc, @PathVariable long amount) {
	    Bank account = bankRepository.findByAccountNo(accountNo);
	    
	    account.setAmount(account.getAmount() + amount);
	    
	    bankRepository.save(account);
	}
	
	@PutMapping("/withdraw/{accountNo}/{ifsc}/{amount}")
	public void withdraw(@PathVariable String accountNo, @PathVariable String ifsc, @PathVariable long amount) {
	    Bank account = bankRepository.findByAccountNo(accountNo);
	    
	    if (account.getAmount() >= amount) {
	        account.setAmount(account.getAmount() - amount);
	        bankRepository.save(account);
	    } 
	    else {
	        throw new RuntimeException("Insufficient balance");
	    }
	}
	
	@PutMapping("/cheque-deposit/{fromAccountNo}/{fromIfsc}/{toAccountNo}/{toIfsc}/{amount}")
	public void chequeDeposit(@PathVariable String fromAccountNo, @PathVariable String fromIfsc,
	                           @PathVariable String toAccountNo, @PathVariable String toIfsc,
	                           @PathVariable long amount) {
	    // Find the bank accounts by account numbers
	    Bank fromAccount = bankRepository.findByAccountNo(fromAccountNo);
	    Bank toAccount = bankRepository.findByAccountNo(toAccountNo);
	    
	    // Check if both accounts exist and have sufficient balance
	    if (fromAccount != null && toAccount != null && fromAccount.getAmount() >= amount) {
	        // Transfer the amount from one account to another
	        fromAccount.setAmount(fromAccount.getAmount() - amount);
	        toAccount.setAmount(toAccount.getAmount() + amount);
	        
	        // Update the account balances in the database
	        bankRepository.save(fromAccount);
	        bankRepository.save(toAccount);
	    } 
	    else {
	        throw new RuntimeException("Invalid account or insufficient balance");
	    }
	}
	
	@GetMapping("/balance-enquiry/{accountNo}/{ifsc}")
	public long balanceEnquiry(@PathVariable String accountNo, @PathVariable String ifsc) {
	    Bank account = bankRepository.findByAccountNo(accountNo);
	    return account.getAmount();
	}




	
	

}
