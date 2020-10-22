package demoDistributedTransaction.clientService.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demoDistributedTransaction.clientService.CustomerMaster;
import demoDistributedTransaction.clientService.service.CustomerMasterService;

@RestController
public class CustomerMasterController {
	@Autowired
	private CustomerMasterService customerMasterService;
	
	@RequestMapping(method=RequestMethod.GET, value = "/customerMasters")
	public List<CustomerMaster> getAllFromCustomerMaster(){
		return customerMasterService.getAllFromCustomerMaster();
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/customerMasters/{id}")
	public Optional<CustomerMaster> getFromCustomerMaster(@PathVariable Long id) {
		return customerMasterService.getFromCustomerMaster(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/customerMasters")
	public void addToCustomerMaster(@RequestBody CustomerMaster customerMaster) {
		customerMasterService.addToCustomerMaster(customerMaster);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/customerMasters/{id}")
	public void updateCustomerMaster(@PathVariable Long id, @RequestBody CustomerMaster customerMaster) {
		customerMasterService.updateCustomerMaster(id, customerMaster);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/customerMasters/{id}")
	public void deleteFromCustomerMaster(@PathVariable Long id) {
		customerMasterService.deleteFromCustomerMaster(id);
	}
	
	
}
