package com.aps.qrc.exception;

// Displaying error, in case of invalid inputs 
public class CustomErrorType {

    private String errMsg;

    public CustomErrorType(String errMsg) {
        super();
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


}
