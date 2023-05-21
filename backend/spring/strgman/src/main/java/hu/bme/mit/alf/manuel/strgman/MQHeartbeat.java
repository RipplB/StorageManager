package hu.bme.mit.alf.manuel.strgman;

import hu.bme.mit.alf.manuel.mqclient.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQHeartbeat {
	private final MqService mqService;

	@Autowired
	public MQHeartbeat(MqService mqService) {
		this.mqService = mqService;
	}

	@Scheduled(initialDelay = 0, fixedRate = 1000)
	public void sendHeartbeat() {
		log.debug("Sending heartbeat");
		mqService.send("I'm alive");
	}
}
