/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of nanosmtp
 * Licence: Apache v2
 **/
package com.s8.core.web.manganese.main.java;

public class Email
{

    private String from = null;
    private String recipient = null;
    private String subject = null;
    private String plain_body = null;
    private String html_body = null;
    private Boolean is_html_multipart = false;

    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }

    public String getRecipient()
    {
        return this.recipient;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from ;
    }

    public void setBody(String plain_body)
    {
        this.plain_body = plain_body;
    }

    public String getBody()
    {
        return this.plain_body;
    }

    public void setHtmlBody(String html_body)
    {
        this.html_body = html_body;
        this.is_html_multipart = true;
    }

    public String getHtmlBody()
    {
        return this.html_body;
    }

    public Boolean isHtmlMultipart()
    {
        return this.is_html_multipart;
    }


    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getSubject()
    {
        return this.subject;
    }

}
