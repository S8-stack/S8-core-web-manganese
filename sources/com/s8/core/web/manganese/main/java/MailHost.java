/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of nanosmtp
 * Licence: Apache v2
 **/
package com.s8.core.web.manganese.main.java;

import java.util.Base64;


public class MailHost
{

    private int port = 465;
    private String host = null;
    private String auth_plain = null;
    private Boolean starttls = false;
    private String username = null;
    private String password = null;
    private int auth_type = 1;

    public static final int AUTH_PLAIN = 1;
    public static final int AUTH_LOGIN = 2;


    public void setAuth(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.auth_plain = Base64.getEncoder().encodeToString(("\0"+username+"\0" + password).getBytes());
    }

    public void useStarttls()
    {
        this.starttls = true;
    }

    public String getAuth()
    {
        return this.auth_plain;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public int getAuthType()
    {
        return this.auth_type;
    }


    public Boolean getStarttls()
    {
        return this.starttls;
    }

    public void setHost(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost()
    {
        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }


    public int setAuthType(int auth_type)
    {
        return this.auth_type = auth_type;
    }


}
