package com.example.application.repository;

import com.example.application.model.CalendarAvailability;
import com.example.application.model.Car;
import com.example.application.model.Delegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DelegationRepositoryImpl {

    private final DynamoDbTable<Delegation> delegationTable;
    private final DynamoDbTable<Car> carTable;
    private final DynamoDbTable<CalendarAvailability> calendarTable;

    @Autowired
    public DelegationRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.delegationTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(Delegation.class));
        this.carTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(Car.class));
        this.calendarTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(CalendarAvailability.class));
    }

    public DynamoDbTable<CalendarAvailability> calendarTable() {
        return this.calendarTable;
    }

    public List<Delegation> getAllDelegations() {
        return delegationTable.scan().items().stream()
                .filter(item -> "DATA".equals(item.getOperation()))
                .collect(Collectors.toList());
    }

    public void saveDelegation(Delegation delegation) {
        try {
            System.out.println("------ GUARDANDO DELEGATION ------");
            System.out.println("DelegationId (PK): " + delegation.getDelegationId());
            System.out.println("Operation (SK): " + delegation.getOperation());
            delegationTable.putItem(delegation);
            System.out.println("------ GUARDADO OK ------");
        } catch (Exception e) {
            System.err.println("Error al guardar delegation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveCar(Car car) {
        carTable.putItem(car);

        List<String> availableDates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i <= 180; i++) {
            availableDates.add(today.plusDays(i).toString()); // formato YYYY-MM-DD
        }

        CalendarAvailability calendar = new CalendarAvailability();
        calendar.setDelegationId(car.getDelegationId());
        calendar.setOperation("CALENDAR#CAR#" + car.getPlateNumber());
        calendar.setAvailableDates(availableDates);

        calendarTable.putItem(calendar);
    }

    public Delegation getDelegation(String delegationId) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue("data") //
                .build();
        return delegationTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }

    public CalendarAvailability getCalendar(String delegationId, String plateNumber) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue("CALENDAR#CAR#" + plateNumber)
                .build();
        return calendarTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }

    public List<Car> getCarsByDelegation(String delegationId) {
        return carTable.query(
                        QueryConditional.keyEqualTo(Key.builder()
                                .partitionValue(delegationId)
                                .build()))
                .items().stream()
                .filter(item -> item.getOperation().startsWith("CAR#"))
                .collect(Collectors.toList());
    }

}
