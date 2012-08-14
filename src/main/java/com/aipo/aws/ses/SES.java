/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.ses;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.aipo.aws.AWSContext;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

/**
 *
 */
public class SES {

  /**
   * AmazonSimpleEmailServiceを返します
   * 
   * @return
   */
  public static AmazonSimpleEmailService getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonSimpleEmailService client =
      new AmazonSimpleEmailServiceClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getSesEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    }
    return client;
  }

  public static AmazonSimpleEmailServiceAsync getAsyncClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonSimpleEmailServiceAsync client =
      new AmazonSimpleEmailServiceAsyncClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getSesEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    }
    return client;
  }

  /**
   * 
   * @param name
   * @param email
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String encodeSource(String name, String email)
      throws UnsupportedEncodingException {
    return encodeWordJIS(name) + " <" + email + ">";
  }

  protected static String encodeWordJIS(String s) {
    try {
      return "=?ISO-2022-JP?B?"
        + new String(Base64.encodeBase64(s.getBytes("ISO-2022-JP")))
        + "?=";
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("CANT HAPPEN");
    }
  }

}
