package com.aps.qrc.exception;

public class CustomExceptionHandler extends RuntimeException {

    public CustomExceptionHandler(String msg) {
        super(msg);
    }

    public CustomExceptionHandler(String msg, Throwable cause) {
        super(msg, cause);
    }


}
