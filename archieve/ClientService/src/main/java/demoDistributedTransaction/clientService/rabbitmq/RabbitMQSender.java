package demoDistributedTransaction.clientService.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import demoDistributedTransaction.clientService.CustomerLedger;


@Service
public class RabbitMQSender {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
 
	@Value("${exchangeName}")
	private String exchange;
 
	@Value("${routingkey}")
	private String routingkey;
 
	@Scheduled
	public void send(CustomerLedger customerLedger) {
		String msg = "(batch_id,transaction_type,amount):"+ customerLedger.getBatchId()+","+customerLedger.getTransactionType()+","+customerLedger.getAmount()+"\n";
  
		rabbitTemplate.convertAndSend(exchange, routingkey, customerLedger);
		System.out.println("Sent msg info " + msg);
 
	}
}