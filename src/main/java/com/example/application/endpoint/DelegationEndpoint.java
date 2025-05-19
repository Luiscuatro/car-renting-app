package com.example.application.endpoint;

import com.example.application.model.Car;
import com.example.application.model.Delegation;
import com.example.application.repository.DelegationRepositoryImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class DelegationEndpoint {

    private final DelegationRepositoryImpl delegationRepository;

    @Autowired
    private DelegationEndpoint(DelegationRepositoryImpl delegationRepository) {
        this.delegationRepository = delegationRepository;
    }

    public void saveCar(Car car, String delegationId) {
        car.setDelegationId(delegationId);
        car.setOperation("CAR#" + car.getPlateNumber());
        delegationTable.putItem(car);
    }


    public List<Delegation> getAllDelegations() {
        return delegationRepository.getAllDelegations();
    }

    public void saveDelegation(Delegation delegation) {
        delegationRepository.saveDelegation(delegation);
    }

    public Delegation getDelegation(String delegationId) {
        return delegationRepository.getDelegation(delegationId);
    }



}
