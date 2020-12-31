package com.lalala.service;

import com.lalala.pojo.SysRole;
import java.util.*;
/**
 * 角色Service层接口
 */
public interface SysRoleService {
    //根据id查询角色
    SysRole findById(String uuid);

    /**
     *  查询所有权限
     */
    List<SysRole> findAll();

    /**
     * 保存角色
     */
    void save(SysRole sysRole);

    /**
     * 根据uuid去删除角色
     */
    void deleteByuuid(String uuid);

    /**
     * 根据角色id，删除关联表中的对应记录
     */
    void deleteMaptabByUuid(String uuid);   
}
