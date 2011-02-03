/*
 * Aipo is a groupware program developed by Aimluck,Inc.
 * Copyright (C) 2004-2011 Aimluck,Inc.
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

package com.aipo.aws.simpledb.model;

import java.util.ArrayList;

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
    add(new ReplaceableAttribute(name, value, replace));
  }
}
