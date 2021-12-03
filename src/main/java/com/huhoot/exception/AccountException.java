package com.huhoot.exception;

public class AccountException extends Exception{
    public AccountException(){
        super();
    }
    public AccountException(String msg){
        super(msg);
    }
}
