package demoDistributedTransaction.bankService.rmqConfig;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig2 {
	//************** sending msg ****************
	@Value("${queueNameBank}")
	String queue2;
	@Value("${exchangeNameBank}")
	String exchange2;
	@Value("${routingkeyBank}")
	private String routingkey2;
	
	@Bean
	Queue queue() {
		return new Queue(queue2, false);
	}
	
	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange2);
	}
	
	@Bean
	Binding binding(Queue queue2, DirectExchange exchange2) {
		return BindingBuilder.bind(queue2).to(exchange2).with(routingkey2);
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

}
