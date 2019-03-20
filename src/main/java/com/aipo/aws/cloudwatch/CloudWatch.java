/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.cloudwatch;

import com.aipo.aws.AWSContext;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;

/**
 *
 */
public class CloudWatch {

  public static AmazonCloudWatch getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    AmazonCloudWatchClientBuilder client =
      AmazonCloudWatchClientBuilder.standard().withCredentials(
        new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()));

    String endpoint = awsContext.getCloudWatchEndpoint();

    if (endpoint != null && !"".equals(endpoint)) {
      client.setEndpointConfiguration(
        new EndpointConfiguration(endpoint, null));
    } else {
      client.setRegion(Regions.AP_NORTHEAST_1.getName());
    }

    return client.build();
  }
}
