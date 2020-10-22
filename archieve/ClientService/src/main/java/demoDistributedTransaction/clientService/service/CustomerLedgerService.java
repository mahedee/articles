package demoDistributedTransaction.clientService.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demoDistributedTransaction.clientService.CustomerLedger;
import demoDistributedTransaction.clientService.CustomerMaster;
import demoDistributedTransaction.clientService.model.UserRequest;
import demoDistributedTransaction.clientService.rabbitmq.RabbitMQSender;
import demoDistributedTransaction.clientService.repository.CustomerLedgerRepository;
import demoDistributedTransaction.clientService.repository.CustomerMasterRepository;

@Service
public class CustomerLedgerService {
	@Autowired
	private CustomerLedgerRepository customerLedgerRepository;
	
	@Autowired
	private CustomerMasterRepository customerMasterRepository;
	
	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	static final double maxLedgerAmount = 50000;
	
	public List<CustomerLedger> getAllFromCustomerLedger(){
		List<CustomerLedger> customerLedgerList = new ArrayList<CustomerLedger>();
		customerLedgerRepository.findAll().forEach(customerLedgerList::add);
		return customerLedgerList;
	}
	
	public Optional<CustomerLedger> getFromCustomerLedger(Long id) {
		return customerLedgerRepository.findById(id);
	}
	
	public boolean checkEligibility(UserRequest userRequest) {
		Long customerId = userRequest.getCustomerId();
		String transactionType = userRequest.getTransactionType();
		Double customerLedgerAmount = userRequest.getAmount();
		if(customerLedgerAmount>maxLedgerAmount) {
			System.out.println("Amount is too high!");
			return false;
		}
		if(transactionType.equals("DEPOSIT")) {
			return true;
		}
		
		CustomerMaster customerMaster = null; 
		List<CustomerMaster> listOfCustomerMasters = customerMasterRepository.findAll();
		for(int i=0;i<listOfCustomerMasters.size();i++) {
			if(listOfCustomerMasters.get(i).getCustomerId().equals(customerId)) {
				customerMaster = listOfCustomerMasters.get(i);
				break;
			}
		}
		Double customerMasterBalance = customerMaster.getBalance();
		
		if(transactionType.equals("WITHDRAW")) {
			if(customerMasterBalance < customerLedgerAmount) {
				System.out.println("Withdrawal not possible due to lack of balance");
				return false;
			}
			
		}
		return true;
	}
	
	public void addToCustomerLedger(UserRequest userRequest) {
		// ekhane user request k customer ledger e convert korte hobe
		//er por nicher method ta kei call korte hobe
		Date date = new Date();
		CustomerLedger customerLedger = new CustomerLedger();
		List<CustomerLedger> listOfCustomerLedgers =getAllFromCustomerLedger();
		Comparator<CustomerLedger> compareByTransactionId = (CustomerLedger cl1, CustomerLedger cl2) -> cl1.getTransactionId().compareTo(cl2.getTransactionId());
		Collections.sort(listOfCustomerLedgers, compareByTransactionId);
		int ledgerNumber = listOfCustomerLedgers.size();
		//transaction id & batch id can be generated using other method
		long transactionId = listOfCustomerLedgers.get(ledgerNumber-1).getTransactionId()+1;
		long batchId = listOfCustomerLedgers.get(ledgerNumber-1).getBatchId()+1;
		
		System.out.println("Transaction id,batch id: "+transactionId+","+batchId);
		customerLedger.setCustomerId(userRequest.getCustomerId());
		customerLedger.setTransactionId(transactionId);
		customerLedger.setBatchId(batchId);
		customerLedger.setTransactionType(userRequest.getTransactionType());
		customerLedger.setAmount(userRequest.getAmount());
		customerLedger.setTransactionDate(date);
		customerLedger.setStatus(userRequest.getStatus());
		
		addToCustomerLedger(customerLedger);
		
		
	}
	
	public void addToCustomerLedger(CustomerLedger customerLedger) {
//		Long customerId = customerLedger.getCustomerId();
//		String transactionType = customerLedger.getTransactionType();
//		Double customerLedgerAmount = customerLedger.getAmount();
//		CustomerMaster customerMaster = null; 
//		List<CustomerMaster> listOfCustomerMasters = customerMasterRepository.findAll();
//		for(int i=0;i<listOfCustomerMasters.size();i++) {
//			if(listOfCustomerMasters.get(i).getCustomerId().equals(customerId)) {
//				customerMaster = listOfCustomerMasters.get(i);
//				break;
//			}
//		}
//		Double customerMasterBalance = customerMaster.getBalance();
//		if(transactionType.equals("Withdraw")) {
//			if(customerMasterBalance < customerLedgerAmount) {
//				System.out.println("Withdrawal not possible due to lack of balance");
//				return;
//			}
//			
//		}

		customerLedgerRepository.save(customerLedger);
		
		rabbitMQSender.send(customerLedger);
		
		
	}
	
	public void updateCustomerLedger(Long id, CustomerLedger customerLedger) {
		customerLedgerRepository.save(customerLedger);
	}
	
	public void deleteFromCustomerLedger(Long id) {
		customerLedgerRepository.deleteById(id);
	}
}
