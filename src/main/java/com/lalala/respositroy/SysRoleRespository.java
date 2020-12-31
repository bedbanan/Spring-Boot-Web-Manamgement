package com.lalala.respositroy;

import com.lalala.pojo.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 角色的持久层仓库表
 */
public interface SysRoleRespository extends JpaRepository<SysRole,String> {

    //根据uuid查找用户信息
    public SysRole findByUuid(String uuid);

    //验证账号的唯一性（如果已经存在则返回0，else返回1）
    @Query(value = "select count(*) from sysuser where username=?1",nativeQuery = true)
    public int validateUsername(String username);

    //根据uuid来删除角色
    @Modifying
    @Query(value = "delete from sysrole where uuid=?1",nativeQuery = true)
    public void deleteByUuid(String uuid);

    //根据角色的uuid，删除用户角色权限关联表中角色对应的记录
    @Modifying
    @Query(value = "delete from sysrole_sys_auths where sys_role_uuid=?1",nativeQuery = true)
    public void deleteMaptabByUuid(String uuid);
}
