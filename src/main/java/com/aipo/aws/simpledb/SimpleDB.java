/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.simpledb;

import java.util.ArrayList;
import java.util.List;

import com.aipo.aws.AWSContext;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient;
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

  public static final String DEFAULT_COUNTER_DOMAIN = "__counter";

  public static AmazonSimpleDB getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonSimpleDB client =
      new AmazonSimpleDBClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getSdbEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    }
    return client;
  }

  public static AmazonSimpleDBAsync getAsyncClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonSimpleDBAsync client =
      new AmazonSimpleDBAsyncClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getSdbEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    }
    return client;
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
    AmazonSimpleDB client = getClient();
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
