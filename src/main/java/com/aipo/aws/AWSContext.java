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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.FilterConfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;

/**
 * 
 */
public class AWSContext {

  public static final String DEFAULT_AWSCREDENTIALS_PROPERTIES =
    "/WEB-INF/aws.properties";

  private AWSCredentials awsCredentials;

  private final String sdbEndpoint;

  private final String s3Endpoint;

  private final String rdsEndpoint;

  private final String sqsEndpoint;

  private final String snsEndpoint;

  protected AWSContext(FilterConfig filterConfig) {

    String awsCredentialsPath = filterConfig.getInitParameter("awsCredentials");
    if (awsCredentialsPath == null || awsCredentialsPath == "") {
      awsCredentialsPath = DEFAULT_AWSCREDENTIALS_PROPERTIES;
    }

    Properties properties = new Properties();
    try {
      InputStream resourceAsStream =
        filterConfig
          .getServletContext()
          .getResourceAsStream(awsCredentialsPath);
      awsCredentials = new PropertiesCredentials(resourceAsStream);

      resourceAsStream =
        filterConfig
          .getServletContext()
          .getResourceAsStream(awsCredentialsPath);
      try {
        properties.load(resourceAsStream);
      } finally {
        try {
          resourceAsStream.close();
        } catch (Exception e) {
        }
      }
    } catch (IOException e) {
      System.out.println("'" + awsCredentialsPath + "' doesn't load.");
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
