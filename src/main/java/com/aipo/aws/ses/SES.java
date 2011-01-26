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

package com.aipo.aws.ses;

import com.aipo.aws.AWSContext;
import com.aipo.aws.AWSContextLocator;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

/**
 * 
 */
public class SES {

  private static ThreadLocal<AmazonSimpleEmailService> sess;

  public static AmazonSimpleEmailService getThreadClient() {
    if (sess == null) {
      sess = new ThreadLocal<AmazonSimpleEmailService>();
    }
    AmazonSimpleEmailService ses = sess.get();
    if (ses == null) {
      AWSContext awsContext = AWSContextLocator.get();
      ses = new AmazonSimpleEmailServiceClient(awsContext.getAwsCredentials());
      String endpoint = awsContext.getSesEndpoint();
      if (endpoint != null && endpoint != "") {
        ses.setEndpoint(endpoint);
      }
      sess.set(ses);
    }
    return ses;
  }

  public static void resetThreadClient() {
    if (sess != null) {
      sess.set(null);
    }
  }

}
