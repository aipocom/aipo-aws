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

package com.aipo.aws.simpledb;

import java.util.ArrayList;
import java.util.List;

import com.aipo.aws.AWSContext;
import com.aipo.aws.AWSContextLocator;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.UpdateCondition;

/**
 * 
 */
public class SimpleDB {

  private static ThreadLocal<AmazonSimpleDB> sdbs;

  public static final String DEFAULT_COUNTER_DOMAIN = "__counter";

  public static AmazonSimpleDB getThreadClient() {
    if (sdbs == null) {
      sdbs = new ThreadLocal<AmazonSimpleDB>();
    }
    AmazonSimpleDB sdb = sdbs.get();
    if (sdb == null) {
      AWSContext awsContext = AWSContextLocator.get();
      sdb = new AmazonSimpleDBClient(awsContext.getAwsCredentials());
      String endpoint = awsContext.getSdbEndpoint();
      if (endpoint != null && endpoint != "") {
        sdb.setEndpoint(endpoint);
      }
      sdbs.set(sdb);
    }
    return sdb;
  }

  public static void resetThreadClient() {
    if (sdbs != null) {
      sdbs.set(null);
    }
  }

  public static Integer counter(String domain) {
    for (int i = 0; i < 5; i++) {
      try {
        return counterJob(domain);
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }
    return null;
  }

  private static Integer counterJob(String domain) {
    AmazonSimpleDB client = getThreadClient();
    GetAttributesRequest getAttributesRequest = new GetAttributesRequest();
    getAttributesRequest.setDomainName(DEFAULT_COUNTER_DOMAIN);
    getAttributesRequest.setItemName(domain);
    getAttributesRequest.setConsistentRead(true);
    Integer count = null;
    try {
      GetAttributesResult attributes =
        client.getAttributes(getAttributesRequest);
      List<Attribute> list = attributes.getAttributes();
      for (Attribute item : list) {
        if ("c".equals(item.getName())) {
          try {
            count = Integer.valueOf(item.getValue());
          } catch (Throwable ignore) {

          }
        }
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }

    if (count == null) {
      CreateDomainRequest createDomainRequest =
        new CreateDomainRequest(DEFAULT_COUNTER_DOMAIN);
      client.createDomain(createDomainRequest);
      count = 0;
    }

    int next = count + 1;
    PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
    putAttributesRequest.setDomainName(DEFAULT_COUNTER_DOMAIN);
    putAttributesRequest.setItemName(domain);
    List<ReplaceableAttribute> attr = new ArrayList<ReplaceableAttribute>();
    attr.add(new ReplaceableAttribute("c", String.valueOf(next), true));
    putAttributesRequest.setAttributes(attr);
    UpdateCondition updateCondition = new UpdateCondition();
    if (next == 1) {
      updateCondition.setExists(false);
      updateCondition.setName("c");
    } else {
      updateCondition.setExists(true);
      updateCondition.setName("c");
      updateCondition.setValue(String.valueOf(count));
    }
    putAttributesRequest.setExpected(updateCondition);

    client.putAttributes(putAttributesRequest);

    return next;
  }
}
