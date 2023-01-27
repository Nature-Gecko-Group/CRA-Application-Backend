package com.naturegecko.application.controllers;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/websocket/")
public class WebsocketController {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	String destination = "/topic/messages";

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public String test(String message) {
		return message;
	}

	ExecutorService executorService = Executors.newFixedThreadPool(1);
	Future<?> submittedTask;

	@MessageMapping("/start")
	public void startTask() {
		if (submittedTask != null) {
			simpMessagingTemplate.convertAndSend(destination, "Nope- :> Started!");
			return;
		}
		simpMessagingTemplate.convertAndSend(destination, "Started task");
		submittedTask = executorService.submit(() -> {
			while (true) {
				simpMessagingTemplate.convertAndSend(destination, LocalDateTime.now().toString() + ": doing some work");
				Thread.sleep(10000);
			}
		});
	}

	@MessageMapping("/stop")
	@SendTo("/topic/messages")
	public String stopTask() {
		if (submittedTask == null) {
			return "Task not running";
		}
		try {
			submittedTask.cancel(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error occurred while stopping task due to: " + ex.getMessage();
		}
		return "Stopped task";
	}

}
