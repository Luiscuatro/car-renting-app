package com.example.application.repository;

import com.example.application.model.Delegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DelegationRepositoryImpl {

    private final DynamoDbTable<Delegation> delegationTable;

    @Autowired
    public DelegationRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.delegationTable = enhancedClient.table("DelegationsTable", TableSchema.fromBean(Delegation.class));
    }

    public void saveDelegation(Delegation delegation) {
        delegationTable.putItem(delegation);
    }

    public Delegation getDelegation(String delegationId) {
        Key key = Key.builder()
                .partitionValue("DELEGATION#" + delegationId)
                .sortValue("PROFILE")
                .build();

        return delegationTable.getItem(r -> r.key(key));
    }
}
