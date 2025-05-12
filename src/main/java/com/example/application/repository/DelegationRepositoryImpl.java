package com.example.application.repository;

import com.example.application.model.Delegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

@Repository
public class DelegationRepositoryImpl {

    private final DynamoDbTable<Delegation> delegationTable;

    @Autowired
    public DelegationRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.delegationTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(Delegation.class));

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

    public Delegation getDelegation(String delegationId) {
        Key key = Key.builder()
                .partitionValue(delegationId)
                .sortValue("data") //
                .build();
        return delegationTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }
}



