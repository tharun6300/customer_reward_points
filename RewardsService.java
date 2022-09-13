package com.customer.rewards.service;

import java.util.List;
import com.customer.rewards.model.Customer;

public interface RewardsService {
	
	List<Customer> calculateRewardsForAll();

	Customer calculateRewardsById(Integer customerId);
	
	
}