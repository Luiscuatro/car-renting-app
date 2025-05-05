package com.example.application.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
public class User {
    // Partition Key: identifica de manera única varios ítems relacionados al usuario
    // formato: "USER#<id>"
    private String pk;
    // Sort Key: permite diferenciar tipos de datos bajo el mismo usuario
    // puede ser "PROFILE" para datos personales o "BOOKING#<id>" para una reserva
    private String sk;

    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private boolean admin;
    private List<Booking> bookings;


    @DynamoDbPartitionKey // indica que este es el campo Partition Key en DynamoDB
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }

    @DynamoDbSortKey // indica que este es el campo Sort Key en DynamoDB
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }

    // getters y setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}

