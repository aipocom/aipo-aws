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

package com.aipo.aws.sqs;

import com.aipo.aws.AWSContext;
import com.aipo.aws.AWSContextLocator;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

/**
 * 
 */
public class SQS {

  private static ThreadLocal<AmazonSQS> sqss;

  public static AmazonSQS getThreadClient() {
    if (sqss == null) {
      sqss = new ThreadLocal<AmazonSQS>();
    }
    AmazonSQS sqs = sqss.get();
    if (sqs == null) {
      AWSContext awsContext = AWSContextLocator.get();
      sqs = new AmazonSQSClient(awsContext.getAwsCredentials());
      String endpoint = awsContext.getSqsEndpoint();
      if (endpoint != null && endpoint != "") {
        sqs.setEndpoint(endpoint);
      }
      sqss.set(sqs);
    }
    return sqs;
  }

  public static void resetThreadClient() {
    if (sqss != null) {
      sqss.set(null);
    }
  }

}
