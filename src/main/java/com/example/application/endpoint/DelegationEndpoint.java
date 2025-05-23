package com.example.application.endpoint;

import com.example.application.model.Car;
import com.example.application.model.CalendarAvailability;
import com.example.application.model.Delegation;
import com.example.application.repository.DelegationRepositoryImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed // temporal hasta integrar Cognito
public class DelegationEndpoint {

    private final DelegationRepositoryImpl repository;

    @Autowired
    public DelegationEndpoint(DelegationRepositoryImpl repository) {
        this.repository = repository;
    }

    public void saveCar(Car car, String delegationId) {
        car.setDelegationId(delegationId);
        car.setOperation("CAR#" + car.getPlateNumber());
        repository.saveCar(car); // también crea calendar automáticamente
    }

    public CalendarAvailability getCalendarAvailability(String delegationId, String plateNumber) {
        return repository.getCalendar(delegationId, plateNumber);
    }

    public void saveDelegation(Delegation delegation) {
        repository.saveDelegation(delegation);
    }

    public List<Delegation> getAllDelegations() {
        return repository.getAllDelegations();
    }

    public Delegation getDelegation(String delegationId) {
        return repository.getDelegation(delegationId);
    }
}
