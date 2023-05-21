package hu.bme.mit.alf.manuel.mqclient;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

	@Value("${mq.outboundQ}")
	private String outboundQueue;

	@Bean
	public Queue outboundQueue() {
		return new Queue(outboundQueue);
	}

}
