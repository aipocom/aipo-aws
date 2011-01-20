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

package com.aipo.aws.aeb;

/**
 * 
 */
public class AEBEnvironmentProperties {

  public static final String AWS_ACCESS_KEY_ID = System
    .getProperty("AWS_ACCESS_KEY_ID");

  public static final String AWS_SECRET_KEY = System
    .getProperty("AWS_SECRET_KEY");

  public static final String JDBC_CONNECTION_STRING = System
    .getProperty("JDBC_CONNECTION_STRING");

  public static final String PARAM1 = System.getProperty("PARAM1");

  public static final String PARAM2 = System.getProperty("PARAM2");

  public static final String PARAM3 = System.getProperty("PARAM3");

  public static final String PARAM4 = System.getProperty("PARAM4");

  public static final String PARAM5 = System.getProperty("PARAM5");
}
