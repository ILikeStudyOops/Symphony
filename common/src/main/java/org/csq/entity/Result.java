package org.csq.entity;

import lombok.Data;

import java.io.Serializable;

public class Result<T> implements Serializable {

    private Boolean success = true;
    private String message = "操作成功";
    private Integer code = 200;
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Long timestamp = System.currentTimeMillis();

    public Result(){}

    public Result<T> back(Integer code,String message,Boolean success){
        this.success = success;
        this.code = code;
        this.message = message;
        return this;
    }

    public Result<T> back(Integer code,String message,Boolean success,T data){
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }
}
