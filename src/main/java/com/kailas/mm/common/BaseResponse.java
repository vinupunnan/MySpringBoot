package com.kailas.mm.common;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private int code ;
    private String message;
    private Object data;



    private Object details;

    public BaseResponse() {
        super();
    }

    public BaseResponse(int code,String message,Object data,Object details){
        super();
        this.code = code;
        this.message = message;
        this.data =data;
        this.details =details;


    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
