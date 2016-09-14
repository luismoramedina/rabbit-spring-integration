package com.github.luismoramedina;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@IntegrationComponentScan
public class RabbitIntegrationApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(RabbitIntegrationApplication.class)
						.web(false)
						.run(args);
		MyGateway gateway = context.getBean(MyGateway.class);
		gateway.sendToRabbit("data from spring integration");
		gateway.sendToRabbit("data from spring integration 2");
	}

	@Bean
	@ServiceActivator(inputChannel = "amqpOutboundChannel")
	public AmqpOutboundEndpoint amqpOutbound(AmqpTemplate amqpTemplate) {
		AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate);
		outbound.setRoutingKey("hello"); // default exchange - route to queue 'foo'
		return outbound;
	}

	@Bean
	public MessageChannel amqpOutboundChannel() {
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = "amqpOutboundChannel")
	public interface MyGateway {
		void sendToRabbit(String data);

	}

}
