package demoDistributedTransaction.bankService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demoDistributedTransaction.bankService.BankMaster;
import demoDistributedTransaction.bankService.service.BankMasterService;

@RestController
public class BankMasterController {
	@Autowired
	private BankMasterService bankMasterService;
	
	@RequestMapping(method=RequestMethod.GET, value = "/bankMasters")
	public List<BankMaster> getAllFromBankMaster(){
		return bankMasterService.getAllFromBankMaster();
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/bankMasters/{id}")
	public Optional<BankMaster> getFromBankMaster(@PathVariable Long id) {
		return bankMasterService.getFromBankMaster(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/bankMasters")
	public void addToBankMaster(@RequestBody BankMaster bankMaster) {
		bankMasterService.addToBankMaster(bankMaster);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/bankMasters/{id}")
	public void updateBankMaster(@PathVariable Long id, @RequestBody BankMaster bankMaster) {
		bankMasterService.updateBankMaster(id, bankMaster);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/bankMasters/{id}")
	public void deleteFromBankMaster(@PathVariable Long id) {
		bankMasterService.deleteFromBankMaster(id);
	}

}
