package com.example.application.repository;

import com.example.application.model.Delegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DelegationRepositoryImpl {

    private final DynamoDbTable<Delegation> delegationDynamoDbTable;

    @Autowired
    public DelegationRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.delegationDynamoDbTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(Delegation.class));

    }


}
