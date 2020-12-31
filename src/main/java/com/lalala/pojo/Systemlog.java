package com.lalala.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "systemlog")
public class Systemlog {

    @Id
    @GeneratedValue(generator = "myuuid")
    @GenericGenerator(name="myuuid",strategy = "uuid")
    @Column(columnDefinition = "char(32)")
    private String uuid;

    @Column(length=100)
    private  String username;  //用户名

    @Column(columnDefinition = "char(19)")
    private  String operatetime;  //操作时间

    @Column(length=20)
    private  String operatetype;  //操作类型

    @Column(length=100)
    private  String operatedesc; //操作简述

    @Column(columnDefinition = "text")
    private  String operatedetail; //

    @Column(length=20)
    private  String ostype;  //操作详情

    @Column(length=20)
    private  String browstype; //浏览器类型

    @Column(length=20)
    private  String ip;   //ip地址

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    public String getOperatedesc() {
        return operatedesc;
    }

    public void setOperatedesc(String operatedesc) {
        this.operatedesc = operatedesc;
    }

    public String getOperatedetail() {
        return operatedetail;
    }

    public void setOperatedetail(String operatedetail) {
        this.operatedetail = operatedetail;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getBrowstype() {
        return browstype;
    }

    public void setBrowstype(String browstype) {
        this.browstype = browstype;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
