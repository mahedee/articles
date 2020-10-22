package orchestrator.model;

public class UserRequest {
	private Long customerId;
	private String userName;
	private String transactionType;
	private Double amount;
	private Long batchId;
	private String status;
	
	public UserRequest(String userName, Long customerId, String transactionType, Double amount, Long batchId, String status) {
		super();
		this.userName = userName;
		this.customerId = customerId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.batchId = batchId;
		this.status = status;
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
		this.transactionType = transactionType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
