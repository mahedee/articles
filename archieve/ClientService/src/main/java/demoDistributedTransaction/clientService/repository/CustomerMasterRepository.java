package demoDistributedTransaction.clientService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import demoDistributedTransaction.clientService.CustomerMaster;

public interface CustomerMasterRepository extends JpaRepository<CustomerMaster, Long> {

}
