/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2014 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.simpledb;

import java.util.ArrayList;

/**
 *
 */
public class ResultList<T> extends ArrayList<T> {

  private static final long serialVersionUID = -4136781059152906185L;

  private String nextToken;

  /**
   * @param nextToken
   *          セットする nextToken
   */
  public void setNextToken(String nextToken) {
    this.nextToken = nextToken;
  }

  /**
   * @return nextToken
   */
  public String getNextToken() {
    return nextToken;
  }

}
