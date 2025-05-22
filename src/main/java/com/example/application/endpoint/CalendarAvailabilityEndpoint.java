package com.example.application.endpoint;

import com.example.application.model.CalendarAvailability;
import com.example.application.repository.CalendarAvailabilityRepositoryImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@AnonymousAllowed // temporal hasta tener auth
public class CalendarAvailabilityEndpoint {

    private final CalendarAvailabilityRepositoryImpl repository;

    @Autowired
    public CalendarAvailabilityEndpoint(CalendarAvailabilityRepositoryImpl repository) {
        this.repository = repository;
    }

    public void saveCalendarAvailability(CalendarAvailability calendarAvailability) {
        repository.saveCalendar(calendarAvailability);
    }

    public CalendarAvailability getCalendarAvailability(String delegationId, String plateNumber) {
        return repository.getCalendar(delegationId, plateNumber);
    }

}
