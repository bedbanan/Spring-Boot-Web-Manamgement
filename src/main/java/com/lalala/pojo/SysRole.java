package com.lalala.pojo;

import org.hibernate.annotations.GenericGenerator;
import java.util.*;
import javax.persistence.*;

/**
 * 角色表
 */
@Entity
@Table(name="sysrole")
public class SysRole {
    @Id
    @GeneratedValue(generator = "myuuid")
    @GenericGenerator(name = "myuuid",strategy ="uuid")
    @Column(length = 32)
    private String uuid;  //id

    @Column(length = 30)
    private String rolename; //角色名称（唯一）

    @Column(length = 200)
    private String roledesc; //角色描述

    /**
     * CascadeType.PERSIST：级联保存，当A实体中有B实体这个属性，数据库操作，保存A时，如果B不存在，则也会自动保存B；
     * CascadeType.REMOVE：级联删除，当A实体中有B实体这个属性，数据库操作，删除A时，B也会删除；
     * CascadeType.MERGE：级联更新，当A实体中有B实体这个属性，数据库操作，更新A时，会相应的更新B中的数据；
     * CascadeType.DETACH：级联脱管，如果你要删除一个实体，但是它有外键无法删除，你就需要这个级联权限了，它会撤销所有相关的外键关联；
     * CascadeType.REFRESH：级联刷新，当A实体中有B实体这个属性，数据库操作，更新A时，会先刷新B，再将A和B保存；
     * CascadeType.ALL：拥有以上所有级联操作权限。
     *
     */
    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER) //CascadeType.REFRESH即连接操作
    private List<SysAuth> sysAuth;  //和权限表多对多

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getRoledesc() {
        return roledesc;
    }

    public void setRoledesc(String roledesc) {
        this.roledesc = roledesc;
    }

    public List<SysAuth> getSysAuth() {
        return sysAuth;
    }

    public void setSysAuth(List<SysAuth> sysAuth) {
        this.sysAuth = sysAuth;
    }
}
