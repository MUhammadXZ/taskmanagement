package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Event;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Your Application Name";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/path/to/your/credentials.json"; // Path to your Google API credentials file
    private GoogleCredential GoogleCredentials;

    public void addEventToCalendar(Event event) throws IOException, GeneralSecurityException {
        Calendar service = getCalendarService();

        com.google.api.services.calendar.model.Event calendarEvent = new com.google.api.services.calendar.model.Event()
                .setSummary(event.getSummary())
                .setDescription(event.getDescription());

        LocalDateTime startDate = event.getStartTime();
        LocalDateTime endDate = event.getEndTime();

        EventDateTime startDateTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .setTimeZone("UTC");
        calendarEvent.setStart(startDateTime);

        EventDateTime endDateTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .setTimeZone("UTC");
        calendarEvent.setEnd(endDateTime);

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10)
        };
        com.google.api.services.calendar.model.Event.Reminders reminders = new com.google.api.services.calendar.model.Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        calendarEvent.setReminders(reminders);

        String calendarId = "primary"; // "primary" for user's primary calendar
        service.events().insert(calendarId, calendarEvent).execute();
    }

    private Calendar getCalendarService() throws IOException, GeneralSecurityException {
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH))
                        .createScoped(Collections.singleton(CalendarScopes.CALENDAR)))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
