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

package com.aipo.aws.elb.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 
 */
public class ELBHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private final String protocol;

  private final int port;

  private String remoteAddr;

  /**
   * @param request
   */
  public ELBHttpServletRequestWrapper(HttpServletRequest request,
      String protocol, int port) {
    super(request);

    this.protocol = protocol;
    this.port = port;

    String header = getHeader("X-FORWARDED-FOR");

    if (header != null && header != "") {
      String[] split = header.split(",");
      remoteAddr = split[0];
    } else {
      remoteAddr = null;
    }
  }

  public ELBHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);

    String hfor = getHeader("X-FORWARDED-FOR");

    if (hfor != null && hfor != "") {
      String[] split = hfor.split(",");
      remoteAddr = split[0];
    } else {
      remoteAddr = null;
    }
    String hport = getHeader("X-FORWARDED-PORT");
    if (hfor != null && hfor != "") {
      port = Integer.valueOf(hport);
    } else {
      port = -1;
    }
    String hhttps = getHeader("X-FORWARDED-PROTO");
    if (hfor != null && hfor != "") {
      protocol = hhttps;
    } else {
      protocol = null;
    }
  }

  /**
   * 
   * @return
   */
  @Override
  public int getServerPort() {
    return port != -1 ? port : super.getServerPort();
  }

  /**
   * 
   * @return
   */
  @Override
  public String getScheme() {
    return protocol != null ? protocol : super.getScheme();
  }

  /**
   * 
   * @return
   */
  @Override
  public String getRemoteAddr() {
    return isELBRequest() ? remoteAddr : super.getRemoteAddr();
  }

  @Override
  public StringBuffer getRequestURL() {
    int port = getServerPort();
    String protocol = getScheme();
    return isELBRequest() ? new StringBuffer(protocol).append("://").append(
      getServerName()).append((port == 443 || port == 80) ? "" : port).append(
      getRequestURI()) : super.getRequestURL();
  }

  @Override
  public boolean isSecure() {
    return protocol != null ? "https".equals(protocol) : super.isSecure();
  }

  /**
   * 
   * @return
   */
  protected boolean isELBRequest() {
    return remoteAddr != null;
  }

}
