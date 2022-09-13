package com.customer.rewards.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.customer.rewards.RewardsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.rewards.model.Customer;
import com.customer.rewards.model.Transactions;
import com.customer.rewards.repository.CustomerRewardRepository;
import com.customer.rewards.service.RewardsService;

@Service
public class RewardsServiceImpl implements RewardsService {
	
	@Autowired
	CustomerRewardRepository customerRewardRepo;

	@Autowired
	RewardsUtil rewardsUtil;

	@Override
	public List<Customer> calculateRewardsForAll() {

		List<Customer> customerList = customerRewardRepo.findAll();
		if (customerList != null) {
			for (Customer customer : customerList) {
				logger.debug("Calculating rewards for Each Customer:::::"
						+ customer.getName());
				Set<Transactions> setOfTransaction = customer.getTransactions();
				setRewardsPerMonth(setOfTransaction, customer);
			}

		}

		return customerList;
	}

	@Override
	public Customer calculateRewardsById(Integer customerId) {
		logger.info("calculateRewardsbyId method In RewardServiceImpl start....");
		
		Customer customerReward = customerRewardRepo.findById(customerId).orElse(null);
		if(customerReward != null) {
			logger.debug("Calculating rewards for Each Customer:::::"
					+ customerReward.getName());
			Set<Transactions> setOfTransaction = customerReward.getTransactions();
			setRewardsPerMonth(setOfTransaction, customerReward);
		}
		
		return customerReward;
	}
	
	private void setRewardsPerMonth(Set<Transactions> setOfTransaction, Customer customer) {
		LocalDate todayDate = LocalDate.now();
		
		for (Transactions transaction : setOfTransaction) {
			int transactionMon = transaction.getCreationDate().getMonth() + 1;
			int transactionYear = transaction.getCreationDate().getYear() + 1900;

			if ((todayDate.getYear() == transactionYear) && (todayDate.getMonth().getValue() == transactionMon))
				customer.setThirdMonthRewards(customer.getThirdMonthRewards()
						+ rewardsUtil.calculateRewardAmountPerTrans(transaction.getTransactionAmount()));
			
			else if ((todayDate.minusMonths(1).getYear() == transactionYear)
					&& (todayDate.minusMonths(1).getMonth().getValue() == transactionMon))
				customer.setSecondMonthRewards(customer.getSecondMonthRewards()
						+ rewardsUtil.calculateRewardAmountPerTrans(transaction.getTransactionAmount()));
			else if ((todayDate.minusMonths(2).getYear() == transactionYear)
					&& (todayDate.minusMonths(2).getMonth().getValue() == transactionMon))
				customer.setFirstMonthRewards(customer.getFirstMonthRewards()
						+ rewardsUtil.calculateRewardAmountPerTrans(transaction.getTransactionAmount()));


		}
	}

}