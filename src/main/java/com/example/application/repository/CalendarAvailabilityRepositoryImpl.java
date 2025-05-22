package com.example.application.repository;

import com.example.application.model.CalendarAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

public class CalendarAvailabilityRepositoryImpl {

    private final DynamoDbTable<CalendarAvailability> calendarTable;

    @Autowired
    public CalendarAvailabilityRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.calendarTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(CalendarAvailability.class));
    }

    public void saveCalendar(CalendarAvailability calendarAvailability) {
        calendarTable.putItem(calendarAvailability);
    }

    public CalendarAvailability getCalendar(String delegationId, String plateNumber) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue("CALENDAR#CAR#" + plateNumber)
                .build();

        return calendarTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }
}
