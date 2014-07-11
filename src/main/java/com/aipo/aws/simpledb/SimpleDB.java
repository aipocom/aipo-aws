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
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
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

  /**
   * 
   * @param <M>
   * @param rootClass
   * @param sql
   * @return
   */
  public static <M> ResultList<M> select(Class<M> rootClass, String sql) {
    return select(getClient(), rootClass, sql, null);
  }

  /**
   * 
   * @param <M>
   * @param client
   * @param rootClass
   * @param sql
   * @return
   */
  public static <M> ResultList<M> select(AmazonSimpleDB client,
      Class<M> rootClass, String sql) {
    return select(client, rootClass, sql, null);
  }

  /**
   * 
   * @param <M>
   * @param rootClass
   * @param sql
   * @param nextToken
   * @return
   */
  public static <M> ResultList<M> select(Class<M> rootClass, String sql,
      String nextToken) {
    return select(getClient(), rootClass, sql, nextToken);
  }

  /**
   * 
   * @param <M>
   * @param client
   * @param rootClass
   * @param sql
   * @param nextToken
   * @return
   */
  public static <M> ResultList<M> select(AmazonSimpleDB client,
      Class<M> rootClass, String sql, String nextToken) {
    try {

      SelectRequest request =
        new SelectRequest(sql.toString()).withConsistentRead(true);
      if (nextToken != null) {
        request.setNextToken(nextToken);
      }
      SelectResult select = client.select(request);
      List<Item> items = select.getItems();
      ResultList<M> result = new ResultList<M>();
      for (Item item : items) {
        M model = rootClass.newInstance();
        if (model instanceof ResultItem) {
          ResultItem resultItem = (ResultItem) model;
          resultItem.assign(item);
          result.add(model);
        }
      }
      result.setNextToken(select.getNextToken());
      return result;
    } catch (InstantiationException e) {
      //
    } catch (IllegalAccessException e) {
      //
    }
    return new ResultList<M>();
  }

  /**
   * 
   * @param <M>
   * @param rootClass
   * @param domain
   * @param itemName
   * @return
   */
  public static <M> M get(Class<M> rootClass, String domain, String itemName) {
    return get(getClient(), rootClass, domain, itemName);
  }

  /**
   * 
   * @param <M>
   * @param client
   * @param rootClass
   * @param domain
   * @param itemName
   * @return
   */
  public static <M> M get(AmazonSimpleDB client, Class<M> rootClass,
      String domain, String itemName) {
    try {
      M model = rootClass.newInstance();
      if (model instanceof ResultItem) {
        GetAttributesResult result =
          client.getAttributes(new GetAttributesRequest(domain, itemName)
            .withConsistentRead(true));
        ResultItem item = (ResultItem) model;
        item.assign(itemName, result);
      }
      return model;
    } catch (InstantiationException e) {
      //
    } catch (IllegalAccessException e) {
      //
    }

    return null;
  }

  /**
   * 
   * @param domain
   * @return
   */
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
