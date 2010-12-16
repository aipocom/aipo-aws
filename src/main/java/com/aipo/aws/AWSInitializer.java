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

package com.aipo.aws;

import com.aipo.aws.rds.RDS;
import com.aipo.aws.s3.S3;
import com.aipo.aws.simpledb.SimpleDB;
import com.aipo.aws.sqs.SQS;

/**
 * 
 */
public class AWSInitializer {

  public static final String DEFAULT_AWSCREDENTIALS_PROPERTIES =
    "aws.properties";

  public static void setUp(String resource) {
    AWSContextLocator.set(new AWSContext(resource));
  }

  public static void setUp() {
    setUp(DEFAULT_AWSCREDENTIALS_PROPERTIES);
  }

  public static void tearDown() {
    SimpleDB.resetThreadClient();
    S3.resetThreadClient();
    RDS.resetThreadClient();
    SQS.resetThreadClient();
    AWSContextLocator.set(null);
  }
}
