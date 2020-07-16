package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.Version;

import java.util.Date;

/**
 * @author gaorui
 * @create 2020-06-25 16:50
 */
public class BaseModel {
    private String cjr;
    private String gxr;
    private Date cjsj;
    private Date gxsj;
    @Version
    private Integer version;

    public String getCjr() {
        return cjr;
    }

    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    public String getGxr() {
        return gxr;
    }

    public void setGxr(String gxr) {
        this.gxr = gxr;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public Date getGxsj() {
        return gxsj;
    }

    public void setGxsj(Date gxsj) {
        this.gxsj = gxsj;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
