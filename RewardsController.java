package com.customer.rewards.controller;

import java.util.List;

import com.customer.rewards.exception.CustomerNotFoundException;
import com.customer.rewards.service.RewardsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.rewards.model.Customer;

@RestController
@RequestMapping("/rewards")
public class RewardsController {
	
	@Autowired
	private RewardsService rewardsService;
	
	@GetMapping
    public ResponseEntity<Object> getCustomerRewards() {
		List<Customer> customerList = rewardsService.calculateRewardsForAll();
		
		if (customerList.isEmpty() || customerList.size() == 0) {
			throw new Exception("Customers not available");
		}
		
		return new ResponseEntity<>(customerList, HttpStatus.OK);
	}
	
	@GetMapping(value="/{customerId}")
    public ResponseEntity<Customer> getCustomerRewardById(@PathVariable Integer customerId) {
		Customer customerRewardsSummary = rewardsService.calculateRewardsById(customerId);
		
		if (customerRewardsSummary == null) {
			throw new Exception("Customer does not exist");
		}
		
		return new ResponseEntity<Customer>(customerRewardsSummary, HttpStatus.OK);
	}
	
}