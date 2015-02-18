package com.darfoo.backend.service.responsemodel;

/**
 * Created by zjh on 14-12-22.
 */
public class UploadToken {
    //token
    String tk;

    public UploadToken(String token) {
        this.tk = token;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }
}
