/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.sqs;

import com.aipo.aws.AWSContext;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;

/**
 *
 */
public class SQS {

  public static AmazonSQS getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    // AmazonSQS client =
    // AmazonSQSClientBuilder
    // .standard()
    // .withCredentials(
    // new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
    // .build();
    AwsClientBuilder client;

    String endpoint = awsContext.getSqsEndpoint();

    if (endpoint != null && endpoint != "") {
      client.setEndpointConfiguration(
        new EndpointConfiguration(
          endpoint,
          Region.getRegion(Regions.AP_NORTHEAST_1).toString()));
    } else {
      client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1).toString());
    }

    return client;
  }

  public static AmazonSQSAsync getAsyncClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    // AmazonSQSAsync client =
    // new AmazonSQSAsyncClient(awsContext.getAwsCredentials());
    AwsClientBuilder client;

    String endpoint = awsContext.getSqsEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpointConfiguration(
        new EndpointConfiguration(
          endpoint,
          Region.getRegion(Regions.AP_NORTHEAST_1).toString()));
    } else {
      client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1).toString());
    }
    return client;
  }
}
