package com.example.application.repository;

import com.example.application.model.CalendarAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

@Repository
public class CalendarAvailabilityRepositoryImpl {

    private final DynamoDbTable<CalendarAvailability> calendarTable;

    @Autowired
    public CalendarAvailabilityRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.calendarTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(CalendarAvailability.class));
    }

    public void saveCalendar(CalendarAvailability calendarAvailability) {
        try {
            calendarTable.putItem(calendarAvailability);
        } catch (Exception e) {
            System.err.println("Error al guardar disponibilidad: " + e.getMessage());
        }
    }

    public CalendarAvailability getCalendar(String carId) {
        Key key = Key.builder()
                .partitionValue(carId)
                .sortValue("calendar")
                .build();

        return calendarTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }
}
