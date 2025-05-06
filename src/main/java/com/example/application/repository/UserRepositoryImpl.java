package com.example.application.repository;

// Importa la clase User que es el modelo principal con los bookings embebidos
import com.example.application.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// Este import puede importar directamente las clases necesarias para usar DynamoDB
// import software.amazon.awssdk.enhanced.dynamodb.*;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;



@Repository  // Marca esta clase como un bean Spring de tipo repositorio
public class UserRepositoryImpl {

    // Esta es la tabla mapeada con el modelo User
    // Se usa DynamoDbTable<User> para tener acceso a operaciones tipo put, get, etc.
    private final DynamoDbTable<User> userTable;

    // Constructor que recibe el cliente DynamoDB inyectado por Spring
    // Y construye la tabla "UsersTable" con el esquema definido por la clase User
    @Autowired
    public UserRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("UsersTable", TableSchema.fromBean(User.class));
    }

    // Metodo para guardar un usuario (con sus bookings embebidos si los tiene)
    public void saveUser(User user) {
        userTable.putItem(user);  // Inserta o actualiza el ítem en la tabla
    }

    // Metodo para obtener un usuario por su ID
    // Usa la PK y SK formateadas como "USER#<id>" y "PROFILE"
    public User getUser(String userId) {
        Key key = Key.builder()
                .partitionValue("USER#" + userId)   // PK: formateada según tu diseño
                .sortValue("PROFILE")               // SK: fijo, porque es el perfil del usuario
                .build();

        return userTable.getItem(r -> r.key(key));  // Devuelve el User si existe
    }
}
