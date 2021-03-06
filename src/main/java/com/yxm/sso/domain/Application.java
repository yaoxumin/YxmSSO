package com.yxm.sso.domain;

import java.io.Serializable;
import java.util.Date;

public class Application implements Serializable {
    private Integer id;

    private String url;

    private String appcode;

    private Date starttime;

    private Date endtime;

    private String loginouturl;

    private String acceptticketurl;

    private Integer companyid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode == null ? null : appcode.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getLoginouturl() {
        return loginouturl;
    }

    public void setLoginouturl(String loginouturl) {
        this.loginouturl = loginouturl == null ? null : loginouturl.trim();
    }

    public String getAcceptticketurl() {
        return acceptticketurl;
    }

    public void setAcceptticketur(String acceptticketurl) {
        this.acceptticketurl = acceptticketurl == null ? null : acceptticketurl.trim();
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }
}