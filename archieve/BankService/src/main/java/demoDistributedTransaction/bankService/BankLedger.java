package demoDistributedTransaction.bankService;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BankLedger {
	@Id
	private Long transactionId;
	private Long bankId;
	private Long batchId;
	private Double amount;
	private Date transactionDate;
	private String transactionType; // for ex- Deposit or Withdraw
	public BankLedger(Long transactionId, Long batchId, Long bankId, String transactionType, Double amount,
			Date transactionDate) {
		super();
		this.transactionId = transactionId;
		this.batchId = batchId;
		this.bankId = bankId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	public BankLedger() {
		
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

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
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
	
}
