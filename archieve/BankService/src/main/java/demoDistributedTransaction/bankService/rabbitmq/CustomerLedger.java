package demoDistributedTransaction.bankService.rabbitmq;

import java.util.Date;

public class CustomerLedger {
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
	
	public Long getBatchId() {
		return batchId;
	}
	
	public Long getCustomerId() {
		return customerId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public Double getAmount() {
		return amount;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
