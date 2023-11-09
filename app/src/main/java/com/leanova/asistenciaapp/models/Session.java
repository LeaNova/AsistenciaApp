package com.leanova.asistenciaapp.models;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {
    private String token;
    private Date expire;

    public Session() {}
    public Session(String token, Date expire) {
        this.token = token;
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
