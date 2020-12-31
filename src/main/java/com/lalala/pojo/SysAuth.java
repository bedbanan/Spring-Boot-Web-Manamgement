package com.lalala.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 权限表
 */
@Entity
@Table(name="sysauth")
public class SysAuth {

    @Id
    @GeneratedValue(generator = "myuuid")
    @GenericGenerator(name="myuuid",strategy = "uuid")
    @Column(length = 32)
    private String uuid;

    @Column(length = 200)
    private String fullname; //权限的唯一名称，“如系统管理，系统管理_部门成员_增加"

    @Column(length = 20)
    private String treename; //树形节点名称，”增加“

    @Column
    private int id; //本身的id

    @Column
    private int pid;  //父id

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTreename() {
        return treename;
    }

    public void setTreename(String treename) {
        this.treename = treename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
