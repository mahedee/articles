package orchestrator.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import orchestrator.model.UserRequest;

@Component
@Service
@Configuration
public class RabbitMQConfig {
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	static final String queueToClient = "CLedgerRequest";
	static final String exchangeClient = "CLedgerExchange";
	static final String routingkeyClient = "CLedgerRoutingKey";
	
	@Bean
	Queue queue() {
		return new Queue(queueToClient, false);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchangeClient);
	}
	
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkeyClient);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
	@RabbitListener(queues = "${UserRequestQ}")
	public void receiveUserTransactionRequest(UserRequest userRequest) {
		System.out.println("Received userRequest:\n(name,transactionType,amount)="+ userRequest.getUserName()+", "+userRequest.getTransactionType()+", "+userRequest.getAmount());
		sendCLedger(userRequest);
	}
	
	//ekhane parametre e user request add korte hobe. message e validity yes or no thakbe. (String validity, UserRequest userRequest)
	//validity thakle batch id generate korbe. batch id user request e pathabe, client ledger update korbe.
	@RabbitListener(queues = "${UserRequestQ}")
	public void receiveMessage(String message) {
		System.out.println(message);
		
	}
	@Scheduled
	public void sendCLedger(UserRequest userRequest) {
		rabbitTemplate.convertAndSend(exchangeClient, routingkeyClient, userRequest);
		System.out.println("Sent userRequest to CLedger " + userRequest.getStatus());
 
	}


}
