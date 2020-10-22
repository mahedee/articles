package demoDistributedTransaction.bankService.repository;

import org.springframework.data.repository.CrudRepository;

import demoDistributedTransaction.bankService.BankLedger;

public interface BankLedgerRepository extends CrudRepository<BankLedger, Long> {

}
