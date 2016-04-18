/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.dynamodb;

import com.aipo.aws.AWSContext;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 *
 */
public class DynamoDB {

  public static AmazonDynamoDBClient getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonDynamoDBClient client =
      new AmazonDynamoDBClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getDynamoDBEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    } else {
      client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    }
    return client;
  }

}
