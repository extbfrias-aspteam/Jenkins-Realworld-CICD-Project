package net.cero.ahorro.spei.data;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class WsAuth extends Authenticator{
	String user;  
    String password;  
    public WsAuth(String user,String password)  
    {  
        super();  
        this.user = user;  
        this.password = password;       
    }  
    protected PasswordAuthentication getPasswordAuthentication() {        
        char[] pwdChar = password.toCharArray();       
        return new PasswordAuthentication(user,pwdChar);  
      }
}
