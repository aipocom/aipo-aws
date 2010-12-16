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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.aipo.aws.rds.RDS;
import com.aipo.aws.s3.S3;
import com.aipo.aws.simpledb.SimpleDB;
import com.aipo.aws.sqs.SQS;

/**
 * 
 */
public class AWSFilter implements Filter {

  private AWSContext awsContext;

  /**
   * @param filterConfig
   * @throws ServletException
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    awsContext = createAWSContext(filterConfig);
  }

  /**
   * @param request
   * @param response
   * @param chain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    try {
      AWSContextLocator.set(awsContext);
      chain.doFilter(request, response);
    } finally {
      SimpleDB.resetThreadClient();
      S3.resetThreadClient();
      RDS.resetThreadClient();
      SQS.resetThreadClient();
      AWSContextLocator.set(null);
    }
  }

  /**
   * 
   */
  @Override
  public void destroy() {
  }

  protected AWSContext createAWSContext(FilterConfig filterConfig) {
    return new AWSContext(filterConfig);
  }
}
