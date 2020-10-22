package demoDistributedTransaction.clientService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demoDistributedTransaction.clientService.CustomerLedger;
import demoDistributedTransaction.clientService.service.CustomerLedgerService;

@RestController
public class CustomerLedgerController {
	@Autowired
	private CustomerLedgerService customerLedgerService;
	
	@RequestMapping(method=RequestMethod.GET, value = "/customerLedgers")
	public List<CustomerLedger> getAllFromCustomerLedger(){
		return customerLedgerService.getAllFromCustomerLedger();
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/customerLedgers/{id}")
	public Optional<CustomerLedger> getFromCustomerLedger(@PathVariable Long id) {
		return customerLedgerService.getFromCustomerLedger(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/customerLedgers")
	public void addToCustomerLedger(@RequestBody CustomerLedger customerLedger) {
		customerLedgerService.addToCustomerLedger(customerLedger);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/customerLedgers/{id}")
	public void updateCustomerLedger(@PathVariable Long id, @RequestBody CustomerLedger customerLedger) {
		customerLedgerService.updateCustomerLedger(id, customerLedger);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/customerLedgers/{id}")
	public void deleteFromCustomerLedger(@PathVariable Long id) {
		customerLedgerService.deleteFromCustomerLedger(id);
	}

}
