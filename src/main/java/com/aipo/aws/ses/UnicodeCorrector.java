/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.aipo.aws.ses;

/**
 * Unicode 文字列を補正するためのユーティリティクラスです。 <br />
 * 
 */

public class UnicodeCorrector {

  /**
   * Unicode 文字列を補正する． "MS932" コンバータでエンコードしようとした際に 正常に変換できない部分を補正する．
   */
  public static String correctToCP932(String s) {
    if (s == null || s.equals("")) {
      return "";
    }

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      buf.append(correctToCP932(s.charAt(i)));
    }
    return new String(buf);
  }

  public static char correctToCP932(char c) {
    switch (c) {
      case 0x301c: // WAVE DASH ->
        return 0xff5e; // FULLWIDTH TILDE
      case 0x2014:
        return 0x2015;
      case 0x2016: // DOUBLE VERTICAL LINE ->
        return 0x2225; // PARALLEL TO
      case 0x2212: // MINUS SIGN ->
        return 0xff0d; // FULLWIDTH HYPHEN-MINUS
      case 0x00A2:
        return 0xffe0;
      case 0x00A3:
        return 0xffe1;
      case 0x00A4:
        return 0xffe2;
    }
    return c;
  }

  /**
   * ISO-2022-JP 文字列を補正する． "MS932" コンバータでエンコードしようとした際に 正常に変換できない部分を補正する．
   */
  public static String correctToISO2022JP(String s) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      buf.append(correctToISO2022JP(s.charAt(i)));
    }
    return new String(buf);
  }

  public static char correctToISO2022JP(char c) {
    switch (c) {
      case 0xff5e:
        return 0x301c;
      case 0x2015:
        return 0x2014;
      case 0x2225:
        return 0x2016;
      case 0xff0d:
        return 0x2212;
      case 0xffe0:
        return 0x00A2;
      case 0xffe1:
        return 0x00A3;
      case 0xffe2:
        return 0x00A4;
    }
    return c;
  }

}
