/*
 * This file is part of the com.aipo.aws package.
 * Copyright (C) 2004-2011 Aimluck,Inc.
 * http://www.aipo.com
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package com.aipo.aws.ses;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import com.amazonaws.services.simpleemail.model.RawMessage;

/**
*
*/
public interface ALMimeMessage {

  /**
   *
   * @param from
   * @throws MessagingException
   */
  public void setFrom(String from) throws MessagingException;

  /**
   *
   * @param name
   * @param from
   * @throws MessagingException
   */
  public void setFrom(String name, String from) throws MessagingException;

  /**
   *
   * @param recipientType
   * @param recipients
   * @throws MessagingException
   */
  public void setRecipients(RecipientType recipientType, String... recipients)
      throws MessagingException;

  /**
   *
   * @param subject
   * @throws MessagingException
   */
  public void setSubject(String subject) throws MessagingException;

  /**
   *
   * @param text
   * @throws MessagingException
   */
  public void setTextContent(String text) throws MessagingException;

  /**
   *
   * @return
   */
  public RawMessage getRawMessage();

}
