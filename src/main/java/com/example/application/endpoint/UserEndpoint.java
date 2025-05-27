package com.example.application.endpoint;

import com.example.application.model.Booking;
import com.example.application.model.CalendarAvailability;
import com.example.application.model.Car;
import com.example.application.model.User;
import com.example.application.repository.UserRepositoryImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserEndpoint(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public void createBooking(String userId, String delegationId, String plateNumber, String startDate, String endDate) {
        // 1. Generar rango de fechas
        LocalDate from = LocalDate.parse(startDate);
        LocalDate to = LocalDate.parse(endDate);
        List<String> requestedDates = from.datesUntil(to.plusDays(1))
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        // 2. Obtener calendario de disponibilidad
        CalendarAvailability calendar = userRepository.getCalendar(delegationId, plateNumber);
        if (calendar == null || !calendar.getAvailableDates().containsAll(requestedDates)) {
            throw new RuntimeException("Fechas no disponibles para este coche.");
        }

        // 3. Obtener info del coche
        Car car = userRepository.getCar(delegationId, plateNumber);
        if (car == null) throw new RuntimeException("Coche no encontrado");

        // 4. Construir booking con datos embebidos
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setOperation("BOOKING#" + LocalDate.now().getYear() + "#" + System.currentTimeMillis());
        booking.setDelegationId(delegationId);
        booking.setPlateNumber(plateNumber);
        booking.setBookedDates(requestedDates);
        booking.setStatus("confirmed");
        booking.setPrice(car.getPrice());
        booking.setCarBrand(car.getBrand());
        booking.setCarModel(car.getModel());
        booking.setImageUrl(car.getImageUrl());

        // 5. Guardar booking
        userRepository.saveBooking(booking);

        // 6. Actualizar disponibilidad (restar días reservados)
        List<String> updated = new ArrayList<>(calendar.getAvailableDates());
        updated.removeAll(requestedDates);
        calendar.setAvailableDates(updated);
        userRepository.saveCalendar(calendar);
    }

    public List<Booking> getBookingsByUser(String userId) {
        return userRepository.getBookingsByUser(userId);
    }


}
