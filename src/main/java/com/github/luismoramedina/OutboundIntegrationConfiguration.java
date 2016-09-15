package com.github.luismoramedina;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

/**
 * @author luismoramedina
 */
@Configuration
@ConditionalOnProperty(value = "outbound", matchIfMissing = true)
public class OutboundIntegrationConfiguration {

	@Autowired
	OutboundIntegrationConfiguration.MyGateway gateway;

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

	@Bean
	CommandLineRunner commandLineRunner() {
		return strings -> {
			gateway.sendToRabbit("data from spring integration");
			gateway.sendToRabbit("data from spring integration 2");
		};
	}

}
