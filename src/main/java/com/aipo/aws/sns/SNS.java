/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.sns;

import com.aipo.aws.AWSContext;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

/**
 *
 */
public class SNS {

  public static AmazonSNS getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    EndpointConfiguration endpointConfiguration =
      new EndpointConfiguration(awsContext.getSnsEndpoint(), "ap-northeast-1");

    AmazonSNS client =
      AmazonSNSClientBuilder
        .standard()
        .withCredentials(
          new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
        .withEndpointConfiguration(endpointConfiguration)
        .build();
    return client;
  }

}
