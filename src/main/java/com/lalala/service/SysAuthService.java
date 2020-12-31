package com.lalala.service;

import com.lalala.pojo.SysAuth;
import com.lalala.pojo.Ztree;

import java.util.*;
/**
 * 用户权限Service层接口
 */
public interface SysAuthService {
    /**
     * 查询所有权限
     */
    List<SysAuth> findAll();

    /**
     * 查找所有权限并以集合Ztree的形式存储，并且和角色的权限相对应，以方便贩判断是否展开或者勾选，默认的role的id为nouuid
     */
    List<Ztree> findAllToZtree(String roleid);

    /**
     * 添加节点的子节点：
     * 1.根据节点id查询对应的节点信息：
     * 2.再根据查询到的节点信息和新的节点名称进行名组合，以该组合名称查询对应的节点对应信息是否存在
     * 3.如果新节点信息不存在，则保存该新节点id是当前选定节点的id，name是需要新增加子节点的名称
     */
    String saveChildAuth(int id,String childname);

    /**
     * 根据id查找对应的权限信息
     */
    SysAuth findById(int id);

    /**
     * 根据节点的全称，查找对应的权限信息
     */
    SysAuth findByFullName(String fullname);

    /**
     *根据父节点的id，获取其子节点的最大id
     */
    int findMaxId(int pid);

    /**
     * 根据节点id删除节点及其子节点
     */
    String deleteByChild(int id);

    /**
     * 根据name删除指定节点机器子节点
     */
    void deleteByName(String name);

    /**
     * 给选定的角色赋予权限，其中"authsinfo"是以$分割节点id字符串
     */
    void editRole(String uuid,String authsinfo);
}
