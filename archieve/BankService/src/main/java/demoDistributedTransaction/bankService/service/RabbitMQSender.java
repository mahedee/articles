package demoDistributedTransaction.bankService.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import demoDistributedTransaction.bankService.rabbitmq.CustomerLedger;


@Service
public class RabbitMQSender {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
 
	@Value("${exchangeNameBank}")
	private String exchange;
 
	@Value("${routingkeyBank}")
	private String routingkey;
 
	@Scheduled
	public void send(String status, CustomerLedger customerLedger) {
		
		if(customerLedger.getStatus().equals("PENDING")) {
			customerLedger.setStatus(status);
		}
		
		rabbitTemplate.convertAndSend(exchange, routingkey, customerLedger);
		System.out.println("Sent msg info " + customerLedger.getCustomerId());
 
	}
}