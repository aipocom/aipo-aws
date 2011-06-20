/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.ses;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.aipo.aws.AWSContext;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;

/**
 *
 */
public class SES {

  public static AmazonSimpleEmailService getClient() {
    AWSContext awsContext = AWSContext.get();
    if (awsContext == null) {
      throw new IllegalStateException("AWSContext is not initialized.");
    }
    AmazonSimpleEmailService client =
      new AmazonSimpleEmailServiceClient(awsContext.getAwsCredentials());
    String endpoint = awsContext.getSesEndpoint();
    if (endpoint != null && endpoint != "") {
      client.setEndpoint(endpoint);
    }
    return client;
  }

  public static String encodeSource(String name, String email)
      throws UnsupportedEncodingException {
    return new StringBuilder("=?ISO-2022-JP?B?").append(
      new String(Base64.encodeBase64(name.getBytes("ISO-2022-JP")))).append(
      "?=").append(" <").append(email).append(">").toString();
  }

  public static String utf8ToJIS(String input) {
    StringBuffer sb = new StringBuffer();
    char c;
    for (int i = 0; i < input.length(); i++) {
      c = input.charAt(i);
      switch (c) {
        case 0xff3c: // 「\」 を変換
          c = 0x005c;
          break;
        case 0xff5e: // 「〜」を変換
          c = 0x301c;
          break;
        case 0x2225: // 「‖」を変換
          c = 0x2016;
          break;
        case 0xff0d: // 「−」を変換
          c = 0x2212;
          break;
        case 0xffe0: // 「￠」を変換
          c = 0x00a2;
          break;
        case 0xffe1: // 「￡」を変換
          c = 0x00a3;
          break;
        case 0xffe2: // 「￢」　を変換
          c = 0x00ac;
          break;
      }
      sb.append(c);
    }
    return sb.toString();
  }
}
