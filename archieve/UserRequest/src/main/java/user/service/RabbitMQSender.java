package user.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import user.model.UserRequest;

@Configuration
@Service
public class RabbitMQSender {
	@Autowired
	private AmqpTemplate rabbitTemplate;
 
 	static final String queueName = "UserTransactionRequest";
	static final String exchangeName = "UserTransactionRequestExchange";
	static final String routingkey = "UserTransactionRequestRoutingKey";
	
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchangeName);
	}
	
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
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
	
	@Scheduled
	public void send(UserRequest userRequest) {
		rabbitTemplate.convertAndSend(exchangeName, routingkey, userRequest);
		rabbitTemplate.convertAndSend(exchangeName, routingkey, "a message");
		System.out.println("Sent msg info " + userRequest.getStatus());
 
	}
	
	//it works!
//	String dummyQueue = "DummyQ";
//	String dummyExchange = "DummyExchange";
//	String dummyRK = "DummyRK";
//	
//	@Bean
//	Queue queue2() {
//		return new Queue(dummyQueue, false);
//	}
//	@Bean
//	DirectExchange exchange2() {
//		return new DirectExchange(dummyExchange);
//	}
//	
//	@Bean
//	Binding binding2(Queue queue2, DirectExchange exchange2) {
//		return BindingBuilder.bind(queue2).to(exchange2).with(dummyRK);
//	}
//	@Scheduled
//	public void sendAgain(String dummyString) {
//		rabbitTemplate.convertAndSend(dummyExchange, dummyRK, dummyString);
//		System.out.println("Sent msg info " + dummyString);
// 
//	}
	

}
