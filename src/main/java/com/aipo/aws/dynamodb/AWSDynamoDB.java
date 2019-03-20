/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.dynamodb;

import java.util.List;

import com.aipo.aws.AWSContext;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.TableStatus;

/**
 *
 */
public class AWSDynamoDB {

  public static AmazonDynamoDB getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonDynamoDBClientBuilder client =
      AmazonDynamoDBClientBuilder.standard().withCredentials(
        new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()));

    String endpoint = awsContext.getDynamoDBEndpoint();

    if (endpoint != null && !"".equals(endpoint)) {
      client.setEndpointConfiguration(
        new EndpointConfiguration(endpoint, null));
    } else {
      client.setRegion(Regions.AP_NORTHEAST_1.getName());
    }
    return client.build();
  }

  public static DescribeTableResult describeTable(AmazonDynamoDBClient client,
      String tableName) {
    return client.describeTable(
      new DescribeTableRequest().withTableName(tableName));
  }

  public static void setUpTable(AmazonDynamoDBClient client, String tableName,
      List<AttributeDefinition> attributeDefinitions,
      List<KeySchemaElement> KeySchemaElements,
      ProvisionedThroughput provisionedThroughputIndex,
      List<GlobalSecondaryIndex> globalSecondaryIndexes) {
    createTable(
      client,
      tableName,
      attributeDefinitions,
      KeySchemaElements,
      provisionedThroughputIndex,
      globalSecondaryIndexes);
    awaitTableCreation(client, tableName);
  }

  /**
   * @return StreamArn
   */
  public static String createTable(AmazonDynamoDBClient client,
      String tableName, List<AttributeDefinition> attributeDefinitions,
      List<KeySchemaElement> KeySchemaElements,
      ProvisionedThroughput provisionedThroughputIndex,
      List<GlobalSecondaryIndex> globalSecondaryIndexes) {

    CreateTableRequest request =
      new CreateTableRequest()
        .withTableName(tableName)
        .withKeySchema(KeySchemaElements)
        .withAttributeDefinitions(attributeDefinitions)
        .withGlobalSecondaryIndexes(globalSecondaryIndexes)
        .withProvisionedThroughput(provisionedThroughputIndex);

    try {
      CreateTableResult result = client.createTable(request);
      return result.getTableDescription().getLatestStreamArn();
    } catch (ResourceInUseException e) {
      return describeTable(client, tableName).getTable().getLatestStreamArn();
    }

  }

  private static void awaitTableCreation(AmazonDynamoDBClient client,
      String tableName) {
    Integer retries = 0;
    Boolean created = false;
    while (!created && retries < 100) {
      DescribeTableResult result = describeTable(client, tableName);
      result.getTable().getTableStatus();
      created =
        result.getTable().getTableStatus().equals(
          TableStatus.ACTIVE.toString());
      if (created) {
        return;
      } else {
        retries++;
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          // do nothing
        }
      }
    }
  }
}
