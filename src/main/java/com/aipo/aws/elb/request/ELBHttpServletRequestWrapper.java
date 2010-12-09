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

  /**
   * 
   * @return
   */
  @Override
  public int getServerPort() {
    return isELBRequest() ? port : super.getServerPort();
  }

  /**
   * 
   * @return
   */
  @Override
  public String getScheme() {
    return isELBRequest() ? protocol : super.getScheme();
  }

  /**
   * 
   * @return
   */
  @Override
  public String getRemoteAddr() {
    return isELBRequest() ? remoteAddr : super.getRemoteAddr();
  }

  /**
   * 
   * @return
   */
  protected boolean isELBRequest() {
    return remoteAddr != null;
  }

}
