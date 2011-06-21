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
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.codec.binary.Base64;

import com.aipo.aws.AWSContext;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.sk_jp.io.CharCodeConverter;
import com.sk_jp.mail.JISDataSource;

/**
 *
 */
public class SES {

  /**
   * AmazonSimpleEmailServiceを返します
   * 
   * @return
   */
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

  /**
   * 
   * @param name
   * @param email
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String encodeSource(String name, String email)
      throws UnsupportedEncodingException {
    return encodeWordJIS(name) + " <" + email + ">";
  }

  /**
   * テキストをセットします。 <br>
   * Part#setText() の代わりにこちらを使うようにします。
   */
  public static void setTextContent(Part p, String s) throws MessagingException {
    p.setDataHandler(new DataHandler(new JISDataSource(s)));
    p.setHeader("Content-Transfer-Encoding", "7bit");
  }

  /**
   * 日本語を含むヘッダ用テキストを生成します。 <br>
   * 変換結果は ASCII なので、これをそのまま setSubject や InternetAddress のパラメタとして使用してください。
   */
  public static String encodeWordJIS(String s) {
    try {
      return "=?ISO-2022-JP?B?"
        + new String(Base64
          .encodeBase64(CharCodeConverter.sjisToJis(UnicodeCorrecter
            .correctToCP932(s)
            .getBytes("Windows-31J"))))
        + "?=";
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("CANT HAPPEN");
    }
  }

  /**
   * 受信者を指定します。
   * 
   * @param msg
   * @param recipientType
   * @param addrString
   * @throws AddressException
   * @throws MessagingException
   */
  public static void setRecipient(Message msg,
      Message.RecipientType recipientType, String[] addrString)
      throws AddressException, MessagingException {
    if (addrString == null) {
      return;
    }
    int addrStringLength = addrString.length;
    InternetAddress[] address = new InternetAddress[addrStringLength];
    for (int i = 0; i < addrStringLength; i++) {
      address[i] = SES.getInternetAddress(addrString[i]);
    }
    msg.setRecipients(recipientType, address);
  }

  /**
   * String 型のアドレス → InternetAddress 型のアドレス に変換する．
   * 
   * @param addr
   * @return
   */
  public static InternetAddress getInternetAddress(String addr) {
    InternetAddress address = null;
    StringTokenizer st = new StringTokenizer(addr, "<>");
    int count = st.countTokens();
    try {
      if (count <= 0) {
        return null;
      } else if (count == 1) {
        address = new InternetAddress(st.nextToken().trim());
      } else if (count == 2) {
        String name = st.nextToken().trim();
        String addressStr = st.nextToken().trim();
        address = new InternetAddress(addressStr, SES.encodeWordJIS(name));
      }
    } catch (Exception e) {
      return null;
    }
    return address;
  }

  @Deprecated
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
