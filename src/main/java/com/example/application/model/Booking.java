package com.example.application.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
public class Booking {
    private String userId;
    private String operation;
    private String delegationId;
    private String plateNumber;
    private List<String> bookedDates;
    private int price;
    private String status;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbSortKey
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDelegationId() {
        return delegationId;
    }
    public void setDelegationId(String delegationId) {
        this.delegationId = delegationId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public List<String> getBookedDates() {
        return bookedDates;
    }
    public void setBookedDates(List<String> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        if (bookedDates != null && !bookedDates.isEmpty()) {
            return bookedDates.get(0);
        }
        return null;
    }

    public String getEndDate() {
        if (bookedDates != null && !bookedDates.isEmpty()) {
            return bookedDates.get(bookedDates.size() - 1);
        }
        return null;
    }

}
