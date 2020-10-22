package demoDistributedTransaction.bankService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demoDistributedTransaction.bankService.repository.BankMasterRepository;
import demoDistributedTransaction.bankService.BankMaster;

@Service
public class BankMasterService {
	@Autowired
	private BankMasterRepository bankMasterRepository;
	
	public List<BankMaster> getAllFromBankMaster(){
		List<BankMaster> bankMasterList = new ArrayList<BankMaster>();
		bankMasterRepository.findAll().forEach(bankMasterList::add);
		return bankMasterList;
	}
	
	public Optional<BankMaster> getFromBankMaster(Long id) {
		return bankMasterRepository.findById(id);
	}
	
	public void addToBankMaster(BankMaster bankMaster) {
		bankMasterRepository.save(bankMaster);
	}
	
	public void updateBankMaster(Long id, BankMaster bankMaster) {
		bankMasterRepository.save(bankMaster);
	}
	
	public void deleteFromBankMaster(Long id) {
		bankMasterRepository.deleteById(id);
	}
}
