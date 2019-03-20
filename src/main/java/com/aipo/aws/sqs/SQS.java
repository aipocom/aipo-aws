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
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

/**
 *
 */
public class SQS {

  public static AmazonSQS getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    EndpointConfiguration endpointConfiguration =
      new EndpointConfiguration(awsContext.getSqsEndpoint(), "ap-northeast-1");

    AmazonSQS client =
      AmazonSQSClientBuilder
        .standard()
        .withCredentials(
          new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
        .withEndpointConfiguration(endpointConfiguration)
        .build();
    return client;
  }

  public static AmazonSQSAsync getAsyncClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    EndpointConfiguration endpointConfiguration =
      new EndpointConfiguration(awsContext.getSqsEndpoint(), "ap-northeast-1");

    AmazonSQSAsync client =
      AmazonSQSAsyncClientBuilder
        .standard()
        .withCredentials(
          new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
        .withEndpointConfiguration(endpointConfiguration)
        .build();
    return client;
  }
}
