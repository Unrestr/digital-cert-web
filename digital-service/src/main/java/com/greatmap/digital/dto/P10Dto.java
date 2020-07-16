package com.greatmap.digital.dto;

import com.greatmap.framework.support.annotation.FieldLabel;

import java.io.Serializable;

/**
 * @author gaorui
 * @create 2020-06-29 15:41
 */
public class P10Dto implements Serializable {

    @FieldLabel("PKCS10REP")
    private String pkcs10Req;

    public String getPkcs10Req() {
        return pkcs10Req;
    }

    public void setPkcs10Req(String pkcs10Req) {
        this.pkcs10Req = pkcs10Req;
    }

    @Override
    public String toString() {
        return
                "pkcs10Req:" + pkcs10Req + "\r\n";
    }
}
