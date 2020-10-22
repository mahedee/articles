package demoDistributedTransaction.clientService.repository;

import org.springframework.data.repository.CrudRepository;

import demoDistributedTransaction.clientService.CustomerLedger;

public interface CustomerLedgerRepository extends CrudRepository<CustomerLedger, Long> {

}
