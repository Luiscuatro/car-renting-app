package com.example.application.repository;

import com.example.application.model.User;
import com.example.application.model.Booking;
import com.example.application.model.CalendarAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    private final DynamoDbTable<User> userTable;
    private final DynamoDbEnhancedClient enhancedClient;

    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.userTable = enhancedClient.table("UsersTable", TableSchema.fromBean(User.class));
    }

    public void saveUser(User user) {
        try {
            System.out.println("------ GUARDANDO USER ------");
            System.out.println("UserId (PK): " + user.getUserId());
            System.out.println("Operation (SK): " + user.getOperation());
            userTable.putItem(user);
            System.out.println("------ GUARDADO OK ------");
        } catch (Exception e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public User getUser(String userId) {
        Key key = Key.builder()
                .partitionValue(userId)
                .sortValue("profile")
                .build();
        return userTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }

    public void saveBooking(Booking booking) {
        DynamoDbTable<Booking> bookingTable = enhancedClient.table("UsersTable", TableSchema.fromBean(Booking.class));
        bookingTable.putItem(booking);

        DynamoDbTable<CalendarAvailability> calendarTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(CalendarAvailability.class));

        Key key = Key.builder()
                .partitionValue(booking.getDelegationId())
                .sortValue("CALENDAR#CAR#" + booking.getPlateNumber())
                .build();

        CalendarAvailability calendar = calendarTable.getItem(GetItemEnhancedRequest.builder().key(key).build());

        if (calendar != null) {
            List<String> current = calendar.getAvailableDates();
            current.removeAll(booking.getBookedDates());
            calendar.setAvailableDates(current);
            calendarTable.putItem(calendar);
        }
    }

    public List<User> getAllUsers() {
        return userTable.scan(ScanEnhancedRequest.builder().build())
                .items()
                .stream()
                .filter(user -> "profile".equals(user.getOperation()))
                .toList();
    }

}
