package demoDistributedTransaction.clientService;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomerMaster {
	
	
	@Id
	private Long customerId;
	private Double balance;
	private Date lastTransactionDate;
	
	public CustomerMaster(Long customerId, Double balance, Date lastTransactionDate) {
		super();
		
		this.customerId = customerId;
		this.balance = balance;
		this.lastTransactionDate = lastTransactionDate;
	}
	public CustomerMaster() {
		
	}
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	
	
}
