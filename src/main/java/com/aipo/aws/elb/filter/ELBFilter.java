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

package com.aipo.aws.elb.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.aipo.aws.elb.request.ELBHttpServletRequestWrapper;

/**
 * 
 */
public class ELBFilter implements Filter {

  private static final Logger logger = Logger.getLogger(ELBFilter.class
    .getName());

  public static final int DEFAULT_PORT = 8080;

  public static final String DEFAULT_PROTOCOL = "http";

  private String protocol = DEFAULT_PROTOCOL;

  private int port = DEFAULT_PORT;

  /**
   * 
   */
  @Override
  public void destroy() {
  }

  /**
   * @param request
   * @param response
   * @param filterChain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {

    filterChain.doFilter(
      getHttpServletRequestWrapper((HttpServletRequest) request),
      response);
  }

  /**
   * @param filterConfig
   * @throws ServletException
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

    // protocol
    String paramProtocol = filterConfig.getInitParameter("protocol");
    if (paramProtocol != null && paramProtocol != "") {
      if ("http".equals(paramProtocol) || "https".equals(paramProtocol)) {
        protocol = paramProtocol;
      } else {
        logger.warning("init parameter [protocol] ignored.");
      }
    }

    // port
    String paramPort = filterConfig.getInitParameter("port");
    if (paramPort != null && paramPort != "") {
      try {
        port = Integer.valueOf(paramPort);
      } catch (Throwable ignore) {
        logger.warning("init parameter [port] ignored.");
      }
    }

  }

  /**
   * 
   * @param request
   * @return
   */
  protected HttpServletRequestWrapper getHttpServletRequestWrapper(
      HttpServletRequest request) {
    return new ELBHttpServletRequestWrapper(request, protocol, port);
  }
}
