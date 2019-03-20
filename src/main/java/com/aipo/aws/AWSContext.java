/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import com.aipo.aws.aeb.AEBEnvironmentProperties;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.PropertiesCredentials;

/**
 *
 */
public class AWSContext implements Serializable {

  private static final long serialVersionUID = -6805584882087598678L;

  public static final String DEFAULT_AWSCREDENTIALS_PROPERTIES =
    new StringBuilder(System.getProperty("catalina.home"))
      .append(File.separator)
      .append("aws")
      .append(File.separator)
      .append("aws.properties")
      .toString();

  private static AWSContext instance = null;

  public static AWSContext get() {
    return instance;
  }

  private AWSCredentials awsCredentials;

  private String sdbEndpoint;

  private String s3Endpoint;

  private String rdsEndpoint;

  private String sqsEndpoint;

  private String snsEndpoint;

  private String sesEndpoint;

  private String dynamodbEndpoint;

  private String cloudWatchEndpoint;

  private String prefix;

  protected static void createAWSContext(ServletContext servletContext) {
    instance = new AWSContext(servletContext);
  }

  protected AWSContext(String resourcePath) {
    setUp(resourcePath, null);
  }

  protected AWSContext(ServletContext servletContext) {
    setUp(DEFAULT_AWSCREDENTIALS_PROPERTIES, servletContext);
  }

  protected AWSContext(String resourcePath, ServletContext servletContext) {
    setUp(resourcePath, servletContext);
  }

  protected AWSContext(FilterConfig filterConfig) {
    String awsCredentialsPath = filterConfig.getInitParameter("awsCredentials");
    if (awsCredentialsPath == null || "".equals(awsCredentialsPath)) {
      awsCredentialsPath = DEFAULT_AWSCREDENTIALS_PROPERTIES;
    }
    setUp(awsCredentialsPath, filterConfig.getServletContext());
  }

  private void setUp(String resourcePath, ServletContext context) {
    Properties properties = new Properties();
    try {
      InputStream resourceAsStream = new FileInputStream(resourcePath);
      awsCredentials = new PropertiesCredentials(resourceAsStream);

      resourceAsStream = new FileInputStream(resourcePath);
      try {
        properties.load(resourceAsStream);
      } finally {
        try {
          resourceAsStream.close();
        } catch (Exception e) {
        }
      }

      sdbEndpoint = properties.getProperty("sdbEndpoint");
      s3Endpoint = properties.getProperty("s3Endpoint");
      rdsEndpoint = properties.getProperty("rdsEndpoint");
      sqsEndpoint = properties.getProperty("sqsEndpoint");
      snsEndpoint = properties.getProperty("snsEndpoint");
      sesEndpoint = properties.getProperty("sesEndpoint");
      dynamodbEndpoint = properties.getProperty("dynamodbEndpoint");
      cloudWatchEndpoint = properties.getProperty("cloudWatchEndpoint");
      prefix = properties.getProperty("prefix");

    } catch (Exception e) {
      System.out.println("'" + resourcePath + "' doesn't load.");

      awsCredentials = null;
      prefix = AEBEnvironmentProperties.PREFIX;
    }

  }

  /**
   * @return awsCredentials
   */
  public AWSCredentials getAwsCredentials() {
    if (awsCredentials == null) {
      DefaultAWSCredentialsProviderChain provider =
        new DefaultAWSCredentialsProviderChain();
      return provider.getCredentials();
    }
    return awsCredentials;
  }

  /**
   * @return sdbEndpoint
   */
  public String getSdbEndpoint() {
    return sdbEndpoint;
  }

  /**
   * @return s3Endpoint
   */
  public String getS3Endpoint() {
    return s3Endpoint;
  }

  /**
   * @return rdsEndpoint
   */
  public String getRdsEndpoint() {
    return rdsEndpoint;
  }

  /**
   * @return sqsEndpoint
   */
  public String getSqsEndpoint() {
    return sqsEndpoint;
  }

  /**
   * @return snsEndpoint
   */
  public String getSnsEndpoint() {
    return snsEndpoint;
  }

  /**
   * @return sesEndpoint
   */
  public String getSesEndpoint() {
    return sesEndpoint;
  }

  /**
   *
   * @return dynamodbEndpoint
   */
  public String getDynamoDBEndpoint() {
    return dynamodbEndpoint;
  }

  /**
   *
   * @return
   */
  public String getCloudWatchEndpoint() {
    return cloudWatchEndpoint;
  }

  public String getPrefix() {
    return prefix;
  }

  public String appendConfigPrefix(String value) {
    StringBuilder b = new StringBuilder();
    String param1 = AEBEnvironmentProperties.PARAM1;
    if (param1 == null || param1.length() == 0) {
      String prefix = getPrefix();
      if (prefix == null || prefix.length() == 0) {
        b.append("local.");
      } else {
        b.append(prefix).append(".");
      }
    } else {
      b.append(param1).append(".");
    }
    b.append(value);
    return b.toString();
  }

  public String appendNamespace(String value) {
    StringBuilder b = new StringBuilder();
    String param1 = AEBEnvironmentProperties.PARAM1;
    if (param1 == null || param1.length() == 0) {
      String prefix = getPrefix();
      if (prefix == null || prefix.length() == 0) {
        b.append("local_");
        String username = System.getProperty("user.name");
        if (username == null || username.length() == 0) {
          username = "anon";
        }
        b.append(username).append("_");
      } else {
        b.append(prefix).append("_");
      }
    } else {
      b.append(param1).append("_");
    }
    b.append(value);
    return b.toString();
  }

  public String appendBacketNamespace(String value) {
    StringBuilder b = new StringBuilder();
    String param1 = AEBEnvironmentProperties.PARAM1;
    if (param1 == null || param1.length() == 0) {
      String prefix = getPrefix();
      if (prefix == null || prefix.length() == 0) {
        b.append("local.");
        String username = System.getProperty("user.name");
        if (username == null || username.length() == 0) {
          username = "anon";
        }
        b.append(username).append(".");
      } else {
        b.append(prefix).append(".");
      }
    } else {
      b.append(param1).append(".");
    }
    b.append(value);
    return b.toString();
  }

  /**
   * @return
   */
}
