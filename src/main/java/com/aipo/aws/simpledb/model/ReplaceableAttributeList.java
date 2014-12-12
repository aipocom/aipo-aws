/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.simpledb.model;

import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

/**
 *
 */
public class ReplaceableAttributeList extends ArrayList<ReplaceableAttribute> {

  private static final long serialVersionUID = 7438151282101649535L;

  public ReplaceableAttributeList() {
    super();
  }

  public ReplaceableAttributeList(int initialCapacity) {
    super(initialCapacity);
  }

  public void add(String name, String value, Boolean replace) {
    add(new ReplaceableAttribute(name, value == null ? "" : value, replace));
  }

  public void add(Map.Entry<String, String> entry, Boolean replace) {
    add(new ReplaceableAttribute(entry.getKey(), entry.getValue() == null
      ? ""
      : entry.getValue(), replace));
  }
}
