package demoDistributedTransaction.bankService;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BankMaster {
	@Id
	private Long id;
	private Long accountId;
	private Double amount;
	private Date lastTransactionDate;
	public BankMaster(Long id, Long accountId, Double amount, Date lastTransactionDate) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.amount = amount;
		this.lastTransactionDate = lastTransactionDate;
	}
	public BankMaster() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}
	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}
	
	
	
}
