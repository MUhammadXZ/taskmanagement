package com.example.taskmanagement;

import com.example.taskmanagement.service.GoogleCalendarService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



import com.example.taskmanagement.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

@SpringBootTest
public class GoogleCalendarServiceTest {

	@Autowired
	private GoogleCalendarService googleCalendarService;

	@Test
	public void testAddEventToCalendar() {
		Event event = new Event();
		event.setSummary("Test Event");
		event.setDescription("This is a test event");
		event.setStartTime(LocalDateTime.now());
		event.setEndTime(LocalDateTime.now().plusHours(1));

		try {
			googleCalendarService.addEventToCalendar(event);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();

		}
	}
}

