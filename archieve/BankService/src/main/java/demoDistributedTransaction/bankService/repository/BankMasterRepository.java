package demoDistributedTransaction.bankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demoDistributedTransaction.bankService.BankMaster;

public interface BankMasterRepository extends JpaRepository<BankMaster, Long> {

}
