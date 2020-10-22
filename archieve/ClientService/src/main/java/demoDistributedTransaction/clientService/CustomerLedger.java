package demoDistributedTransaction.clientService;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomerLedger {
	@Id
	private Long transactionId;
	private Long batchId;
	private Long customerId;
	private String transactionType;// for ex- Deposit or Withdraw
	private Double amount;
	private Date transactionDate;
	private String status;// Pending or Completed
	
	public CustomerLedger(Long transactionId, Long batchId, Long cutomerId, String transactionType, Double amount,
			Date transactionDate, String status) {
		super();
		this.transactionId = transactionId;
		this.batchId = batchId;
		this.customerId = cutomerId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.status = status;
	}
	
	public CustomerLedger() {
		
	}
	
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
