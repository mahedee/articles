package demoDistributedTransaction.bankService.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demoDistributedTransaction.bankService.BankLedger;
import demoDistributedTransaction.bankService.service.BankLedgerService;

@RestController
public class BankLedgerController {
	@Autowired
	private BankLedgerService bankLedgerService;
	
	@RequestMapping(method=RequestMethod.GET, value = "/bankLedgers")
	public List<BankLedger> getAllFromBankLedger(){
		return bankLedgerService.getAllFromBankLedger();
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/bankLedgers/{id}")
	public Optional<BankLedger> getFromBankLedger(@PathVariable Long id) {
		return bankLedgerService.getFromBankLedger(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/bankLedgers")
	public void addToBankLedger(@RequestBody BankLedger bankLedger) {
		bankLedgerService.addToBankLedger(bankLedger);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/bankLedgers/{id}")
	public void updateBankLedger(@PathVariable Long id, @RequestBody BankLedger bankLedger) {
		bankLedgerService.updateBankLedger(id, bankLedger);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/bankLedgers/{id}")
	public void deleteFromBankLedger(@PathVariable Long id) {
		bankLedgerService.deleteFromBankLedger(id);
	}

}
