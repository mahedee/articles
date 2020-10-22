package demoDistributedTransaction.bankService.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import demoDistributedTransaction.bankService.BankLedger;
import demoDistributedTransaction.bankService.service.BankLedgerService;

@Component
public class RabbitMQReceiver {
	private CustomerLedger incomingCustomerLedger;
	
	@Autowired
	private BankLedgerService bankLedgerService;
	
	@RabbitListener(queues = "${queueName}")
	public void receivedMessage(CustomerLedger incomingCustomerLedger) {
		setIncomingCustomerLedger(incomingCustomerLedger);
		System.out.println("Received msg:\n(batchId,transactionType,amount)="+incomingCustomerLedger.getBatchId()+","+incomingCustomerLedger.getTransactionType()+","+incomingCustomerLedger.getAmount());
		BankLedger bankLedger=new BankLedger();
		bankLedger = createBankLedgerUsingCustomerLedger(incomingCustomerLedger);
		bankLedgerService.addToBankLedger(bankLedger,incomingCustomerLedger);
		
		//bankLedgerService.createBankLedgerUsingCustomerLedger(incomingCustomerLedger);
		
	}
	public BankLedger createBankLedgerUsingCustomerLedger(CustomerLedger incomingCustomerLedger) {
		BankLedger bankLedger=new BankLedger();
		
		bankLedger.setTransactionId(incomingCustomerLedger.getTransactionId());
		bankLedger.setBankId(101L);
		bankLedger.setBatchId(incomingCustomerLedger.getBatchId());
		bankLedger.setAmount(incomingCustomerLedger.getAmount());
		bankLedger.setTransactionType(incomingCustomerLedger.getTransactionType());
		bankLedger.setTransactionDate(incomingCustomerLedger.getTransactionDate());
		
		return bankLedger;
	}

	public CustomerLedger getIncomingCustomerLedger() {
		return incomingCustomerLedger;
	}

	public void setIncomingCustomerLedger(CustomerLedger incomingCustomerLedger) {
		this.incomingCustomerLedger = incomingCustomerLedger;
	}
	

}
