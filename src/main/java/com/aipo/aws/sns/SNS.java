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

package com.aipo.aws.sns;

import com.aipo.aws.AWSContext;
import com.aipo.aws.AWSContextLocator;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * 
 */
public class SNS {

  private static ThreadLocal<AmazonSNS> snss;

  public static AmazonSNS getThreadClient() {
    if (snss == null) {
      snss = new ThreadLocal<AmazonSNS>();
    }
    AmazonSNS sns = snss.get();
    if (sns == null) {
      AWSContext awsContext = AWSContextLocator.get();
      sns = new AmazonSNSClient(awsContext.getAwsCredentials());
      String endpoint = awsContext.getSnsEndpoint();
      if (endpoint != null && endpoint != "") {
        sns.setEndpoint(endpoint);
      }
      snss.set(sns);
    }
    return sns;
  }

  public static void resetThreadClient() {
    if (snss != null) {
      snss.set(null);
    }
  }

}
