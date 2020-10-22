package demoDistributedTransaction.clientService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demoDistributedTransaction.clientService.repository.CustomerMasterRepository;
import demoDistributedTransaction.clientService.CustomerMaster;
@Service
public class CustomerMasterService {
	@Autowired
	private CustomerMasterRepository customerMasterRepository;
	
	public List<CustomerMaster> getAllFromCustomerMaster(){
		List<CustomerMaster> customerMasterList = new ArrayList<CustomerMaster>();
		customerMasterRepository.findAll().forEach(customerMasterList::add);
		return customerMasterList;
	}
	
	public Optional<CustomerMaster> getFromCustomerMaster(Long id) {
		return customerMasterRepository.findById(id);
	}
	
	public void addToCustomerMaster(CustomerMaster customerMaster) {
		customerMasterRepository.save(customerMaster);
	}
	
	public void updateCustomerMaster(Long id, CustomerMaster customerMaster) {
		customerMasterRepository.save(customerMaster);
	}
	
	public void deleteFromCustomerMaster(Long id) {
		customerMasterRepository.deleteById(id);
	}
}
