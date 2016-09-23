/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.s3;

import com.aipo.aws.AWSContext;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 *
 */
public class S3 {

  private static final int CONNECTION_TIMEOUT = 30000;

  private static final int SOCKET_TIMEOUT = 180000;

  public static AmazonS3 getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    ClientConfiguration configuration = new ClientConfiguration();
    configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
    configuration.setSocketTimeout(SOCKET_TIMEOUT);

    AmazonS3 client =
      new AmazonS3Client(awsContext.getAwsCredentials(), configuration);
    String endpoint = awsContext.getS3Endpoint();
    if (endpoint != null && !"".equals(endpoint)) {
      client.setEndpoint(endpoint);
    } else {
      client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    }
    return client;
  }

}
