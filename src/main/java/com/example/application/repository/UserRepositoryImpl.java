package com.example.application.repository;

import com.example.application.model.Car;
import com.example.application.model.User;
import com.example.application.model.Booking;
import com.example.application.model.CalendarAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl {

    private final DynamoDbTable<User> userTable;
    private final DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DelegationRepositoryImpl delegationRepository;

    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.userTable = enhancedClient.table("UsersTable", TableSchema.fromBean(User.class));
    }

    // Obtener coche desde delegación
    public Car getCar(String delegationId, String plateNumber) {
        List<Car> cars = delegationRepository.getCarsByDelegation(delegationId);
        return cars.stream()
                .filter(c -> c.getPlateNumber().equals(plateNumber))
                .findFirst()
                .orElse(null);
    }

    // Obtener disponibilidad de un coche
    public CalendarAvailability getCalendar(String delegationId, String plateNumber) {
        return delegationRepository.getCalendar(delegationId, plateNumber);
    }

    // Guardar nuevo calendario actualizado
    public void saveCalendar(CalendarAvailability calendar) {
        delegationRepository.calendarTable().putItem(calendar);
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
    }

    public List<Booking> getBookingsByUser(String userId) {
        DynamoDbTable<Booking> bookingTable = enhancedClient.table("UsersTable", TableSchema.fromBean(Booking.class));
        QueryConditional condition = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(userId)
                .build());

        return bookingTable.query(condition)
                .items()
                .stream()
                .filter(b -> b.getOperation().startsWith("BOOKING#"))
                .collect(Collectors.toList());
    }

}
