package com.huhoot.exception;

public class NotYourOwnException extends Exception {
    public NotYourOwnException(){
        super();
    }
    public NotYourOwnException(String msg){
        super(msg);
    }
}
