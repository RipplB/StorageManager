package hu.bme.mit.alf.manuel.mqclient;

import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RabbitListener(queues = "${mq.inboundQ}")
public class MqService {
	private final RabbitTemplate template;
	private final Queue outboundQueue;

	@Setter
	private Consumer<String> consumer;

	@Autowired
	public MqService(RabbitTemplate template, Queue outboundQueue) {
		this.template = template;
		this.outboundQueue = outboundQueue;
		consumer = s -> {};
	}

	public void send(String content) {
		template.convertAndSend(outboundQueue.getName(), content);
	}

	@RabbitHandler
	public void receive(String message) {
		consumer.accept(message);
	}

}
