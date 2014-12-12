/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2014 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.simpledb;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.util.SimpleDBUtils;

/**
 *
 */
public class ResultItem extends HashMap<String, String> implements Serializable {

  private static final long serialVersionUID = 5732592132008856195L;

  private String itemName;

  private String nextToken;

  protected void assign(Item item) {
    this.itemName = item.getName();
    List<Attribute> attributes = item.getAttributes();
    for (Attribute attr : attributes) {
      put(attr.getName(), attr.getValue());
    }
  }

  protected void assign(String itemName, GetAttributesResult result) {
    this.itemName = itemName;
    List<Attribute> attributes = result.getAttributes();
    for (Attribute attr : attributes) {
      put(attr.getName(), attr.getValue());
    }
  }

  /**
   * @return itemName
   */
  public String getItemName() {
    return itemName;
  }

  public Date getAsDate(String key) {
    String value = get(key);
    if (value == null) {
      return null;
    }
    try {
      return SimpleDBUtils.decodeDate(value);
    } catch (ParseException e) {
      // ignore
    }
    return null;
  }

  public Integer getAsInt(String key) {
    String value = get(key);
    if (value == null) {
      return null;
    }
    return SimpleDBUtils.decodeZeroPaddingInt(value);
  }

  /**
   * @return nextToken
   */
  public String getNextToken() {
    return nextToken;
  }
}
