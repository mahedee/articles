package user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long customerId;
	private String userName;
	private String transactionType;
	private Double amount;
	private Long batchId;
	private String status;
	
	
	public UserRequest(Long id, Long customerId, String userName, String transactionType, Double amount, Long batchId,
			String status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.userName = userName;
		this.transactionType = transactionType.toUpperCase();
		this.amount = amount;
		this.batchId = batchId;
		this.status = status.toUpperCase();
	}
	
	public UserRequest() {
		super();	
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
		this.transactionType = transactionType.toUpperCase();
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status.toUpperCase();
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	
	
	
}
