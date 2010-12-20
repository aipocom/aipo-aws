/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2010 Aimluck,Inc.
 * http://aipostyle.com/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aipo.aws;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;

/**
 * 
 */
public class AWSContext {

  public static final String DEFAULT_AWSCREDENTIALS_PROPERTIES =
    "/WEB-INF/aws.properties";

  private AWSCredentials awsCredentials;

  private String sdbEndpoint;

  private String s3Endpoint;

  private String rdsEndpoint;

  private String sqsEndpoint;

  private String snsEndpoint;

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
    if (awsCredentialsPath == null || awsCredentialsPath == "") {
      awsCredentialsPath = DEFAULT_AWSCREDENTIALS_PROPERTIES;
    }
    setUp(awsCredentialsPath, filterConfig.getServletContext());
  }

  private void setUp(String resourcePath, ServletContext context) {
    Properties properties = new Properties();
    try {
      InputStream resourceAsStream =
        context == null
          ? ClassLoader.getSystemResourceAsStream(resourcePath)
          : context.getResourceAsStream(resourcePath);
      awsCredentials = new PropertiesCredentials(resourceAsStream);

      resourceAsStream =
        context == null
          ? ClassLoader.getSystemResourceAsStream(resourcePath)
          : context.getResourceAsStream(resourcePath);
      try {
        properties.load(resourceAsStream);
      } finally {
        try {
          resourceAsStream.close();
        } catch (Exception e) {
        }
      }
    } catch (Exception e) {
      System.out.println("'" + resourcePath + "' doesn't load.");
    }

    sdbEndpoint = properties.getProperty("sdbEndpoint");
    s3Endpoint = properties.getProperty("s3Endpoint");
    rdsEndpoint = properties.getProperty("rdsEndpoint");
    sqsEndpoint = properties.getProperty("sqsEndpoint");
    snsEndpoint = properties.getProperty("snsEndpoint");
  }

  /**
   * @return awsCredentials
   */
  public AWSCredentials getAwsCredentials() {
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
}
