package com.applock.fingerprint.passwordlock.downloaderUtil.models;

public class ModelInstagramPref {

    public String PREFERENCE_SESSIONID = "";
    public String PREFERENCE_USERID = "";
    public String PREFERENCE_COOKIES = "";
    public String PREFERENCE_CSRF = "";
    public String PREFERENCE_ISINSTAGRAMLOGEDIN = "";

    public ModelInstagramPref() {
    }

    public ModelInstagramPref(String PREFERENCE_SESSIONID, String PREFERENCE_USERID, String PREFERENCE_COOKIES, String PREFERENCE_CSRF, String PREFERENCE_ISINSTAGRAMLOGEDIN) {
        this.PREFERENCE_SESSIONID = PREFERENCE_SESSIONID;
        this.PREFERENCE_USERID = PREFERENCE_USERID;
        this.PREFERENCE_COOKIES = PREFERENCE_COOKIES;
        this.PREFERENCE_CSRF = PREFERENCE_CSRF;
        this.PREFERENCE_ISINSTAGRAMLOGEDIN = PREFERENCE_ISINSTAGRAMLOGEDIN;
    }

    public String getPREFERENCE_SESSIONID() {
        return PREFERENCE_SESSIONID;
    }

    public void setPREFERENCE_SESSIONID(String PREFERENCE_SESSIONID) {
        this.PREFERENCE_SESSIONID = PREFERENCE_SESSIONID;
    }

    public String getPREFERENCE_USERID() {
        return PREFERENCE_USERID;
    }

    public void setPREFERENCE_USERID(String PREFERENCE_USERID) {
        this.PREFERENCE_USERID = PREFERENCE_USERID;
    }

    public String getPREFERENCE_COOKIES() {
        return PREFERENCE_COOKIES;
    }

    public void setPREFERENCE_COOKIES(String PREFERENCE_COOKIES) {
        this.PREFERENCE_COOKIES = PREFERENCE_COOKIES;
    }

    public String getPREFERENCE_CSRF() {
        return PREFERENCE_CSRF;
    }

    public void setPREFERENCE_CSRF(String PREFERENCE_CSRF) {
        this.PREFERENCE_CSRF = PREFERENCE_CSRF;
    }

    public String getPREFERENCE_ISINSTAGRAMLOGEDIN() {
        return PREFERENCE_ISINSTAGRAMLOGEDIN;
    }

    public void setPREFERENCE_ISINSTAGRAMLOGEDIN(String PREFERENCE_ISINSTAGRAMLOGEDIN) {
        this.PREFERENCE_ISINSTAGRAMLOGEDIN = PREFERENCE_ISINSTAGRAMLOGEDIN;
    }
}
