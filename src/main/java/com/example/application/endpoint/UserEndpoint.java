package com.example.application.endpoint;

import com.example.application.model.Booking;
import com.example.application.model.User;
import com.example.application.repository.UserRepositoryImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    public void saveBooking(Booking booking) {
        userRepository.saveBooking(booking);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

}
