package com.example.filestestapp.model.ajax;

import com.example.filestestapp.model.AbstractDTO;

public class AjaxResponse<T extends AbstractDTO> {
    public T response;
    public String msg;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
