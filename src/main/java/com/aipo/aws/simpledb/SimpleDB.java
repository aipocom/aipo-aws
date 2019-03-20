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
import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClientBuilder;
import com.amazonaws.services.simpledb.AmazonSimpleDBClientBuilder;
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

  private static final int CONNECTION_TIMEOUT = 50 * 1000;

  private static final int SOCKET_TIMEOUT = 50 * 1000;

  private static final int MAX_ERROR_RETRY = 20;

  private static final int MAX_RETRY_COUNT = 10;

  public static final String DEFAULT_COUNTER_DOMAIN = "__counter";

  public static AmazonSimpleDB getClient() {
    ClientConfiguration configuration = new ClientConfiguration();
    configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
    configuration.setSocketTimeout(SOCKET_TIMEOUT);
    configuration.setMaxErrorRetry(MAX_ERROR_RETRY);

    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    EndpointConfiguration endpointConfiguration =
      new EndpointConfiguration(awsContext.getSdbEndpoint(), "ap-northeast-1");

    AmazonSimpleDB client =
      AmazonSimpleDBClientBuilder
        .standard()
        .withCredentials(
          new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
        .withEndpointConfiguration(endpointConfiguration)
        .build();
    return client;
  }

  public static AmazonSimpleDBAsync getAsyncClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }

    EndpointConfiguration endpointConfiguration =
      new EndpointConfiguration(awsContext.getSdbEndpoint(), "ap-northeast-1");

    AmazonSimpleDBAsync client =
      AmazonSimpleDBAsyncClientBuilder
        .standard()
        .withCredentials(
          new AWSStaticCredentialsProvider(awsContext.getAwsCredentials()))
        .withEndpointConfiguration(endpointConfiguration)
        .build();
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

  public static <M> ResultList<M> select(AmazonSimpleDB client,
      Class<M> rootClass, String sql, String nextToken) {
    ResultList<M> resultList = new ResultList<M>();
    int count = 0;
    while (true) {
      count++;
      try {
        resultList = selectRetry(client, rootClass, sql, nextToken);
        break;
      } catch (Exception e) {
        if (MAX_RETRY_COUNT <= count) {
          throw e;
        }
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException ie) {
          // do nothing
        }
      }
    }
    return resultList;
  }

  /**
   *
   * @param <M>
   * @param client
   * @param rootClass
   * @param sql
   * @param nextToken
   * @return
   * @throws Exception
   */
  public static <M> ResultList<M> selectRetry(AmazonSimpleDB client,
      Class<M> rootClass, String sql, String nextToken)
      throws AmazonClientException {
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
    } catch (AmazonClientException e) {
      throw new AmazonClientException(e);
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
    M model = null;
    int count = 0;
    while (true) {
      count++;
      try {
        model = getRetry(client, rootClass, domain, itemName);
        break;
      } catch (Exception e) {
        if (MAX_RETRY_COUNT <= count) {
          throw e;
        }
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException ie) {
          // do nothing
        }
      }
    }
    return model;
  }

  /**
   *
   * @param <M>
   * @param client
   * @param rootClass
   * @param domain
   * @param itemName
   * @return
   * @throws Exception
   */
  public static <M> M getRetry(AmazonSimpleDB client, Class<M> rootClass,
      String domain, String itemName) throws AmazonClientException {
    try {
      M model = rootClass.newInstance();
      if (model instanceof ResultItem) {
        GetAttributesResult result =
          client.getAttributes(
            new GetAttributesRequest(domain, itemName).withConsistentRead(
              true));
        ResultItem item = (ResultItem) model;
        item.assign(itemName, result);
      }
      return model;
    } catch (InstantiationException e) {
      //
    } catch (IllegalAccessException e) {
      //
    } catch (AmazonClientException e) {
      throw new AmazonClientException(e);
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

  /**
   *
   * @param <M>
   * @param client
   * @param rootClass
   * @param domain
   * @param itemName
   * @return
   */
  public static Integer count(AmazonSimpleDB client,
      GetAttributesRequest getAttributesRequest) {
    Integer count = null;
    int retryCount = 0;
    while (true) {
      retryCount++;
      try {
        count = countRetry(client, getAttributesRequest);
        break;
      } catch (Exception e) {
        if (MAX_RETRY_COUNT <= retryCount) {
          throw e;
        }
        try {
          Thread.sleep(1000L);
        } catch (InterruptedException ie) {
          // do nothing
        }
      }
    }
    return count;
  }

  /**
   *
   * @param <M>
   * @param client
   * @param rootClass
   * @param domain
   * @param itemName
   * @return
   * @throws Exception
   */
  public static Integer countRetry(AmazonSimpleDB client,
      GetAttributesRequest getAttributesRequest) throws AmazonClientException {
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
      return count;
    } catch (AmazonClientException e) {
      throw new AmazonClientException(e);
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return count;
  }

  private static Integer counterJob(String domain) {
    AmazonSimpleDB client = getClient();
    GetAttributesRequest getAttributesRequest = new GetAttributesRequest();
    getAttributesRequest.setDomainName(DEFAULT_COUNTER_DOMAIN);
    getAttributesRequest.setItemName(domain);
    getAttributesRequest.setConsistentRead(true);
    Integer count = null;
    try {
      count = count(client, getAttributesRequest);
    } catch (Throwable t) {
      t.printStackTrace();
    }

    if (count == null) {
      try {
        CreateDomainRequest createDomainRequest =
          new CreateDomainRequest(DEFAULT_COUNTER_DOMAIN);
        client.createDomain(createDomainRequest);
      } catch (Throwable t) {
      }
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
