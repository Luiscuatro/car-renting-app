package com.example.application.repository;

import com.example.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

@Repository
public class UserRepositoryImpl {

    private final DynamoDbTable<User> userTable;

    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
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
                .sortValue("profile") //
                .build();

        return userTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }
}
