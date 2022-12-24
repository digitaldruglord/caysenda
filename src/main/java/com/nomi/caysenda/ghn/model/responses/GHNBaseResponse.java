package com.nomi.caysenda.ghn.model.responses;

public class GHNBaseResponse {
    Integer code;
    String message;
    String code_message;
    String code_message_value;

    public String getCode_message_value() {
        return code_message_value;
    }

    public void setCode_message_value(String code_message_value) {
        this.code_message_value = code_message_value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode_message() {
        return code_message;
    }

    public void setCode_message(String code_message) {
        this.code_message = code_message;
    }
}
