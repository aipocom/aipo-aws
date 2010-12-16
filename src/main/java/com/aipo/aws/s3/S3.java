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

package com.aipo.aws.s3;

import com.aipo.aws.AWSContext;
import com.aipo.aws.AWSContextLocator;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * 
 */
public class S3 {

  private static ThreadLocal<AmazonS3> s3s;

  public static AmazonS3 getThreadClient() {
    if (s3s == null) {
      s3s = new ThreadLocal<AmazonS3>();
    }
    AmazonS3 s3 = s3s.get();
    if (s3 == null) {
      AWSContext awsContext = AWSContextLocator.get();
      s3 = new AmazonS3Client(awsContext.getAwsCredentials());
      String endpoint = awsContext.getS3Endpoint();
      if (endpoint != null && endpoint != "") {
        s3.setEndpoint(endpoint);
      }
      s3s.set(s3);
    }
    return s3;
  }

  public static void resetThreadClient() {
    if (s3s != null) {
      s3s.set(null);
    }
  }

}
