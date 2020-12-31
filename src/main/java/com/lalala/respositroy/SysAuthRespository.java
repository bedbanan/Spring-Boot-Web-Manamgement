package com.lalala.respositroy;

import com.lalala.pojo.SysAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限表的持久层
 */

public interface SysAuthRespository extends JpaRepository<SysAuth,String> {

    public SysAuth findById(int id);  //根据节点id查询信息

    //根据节点全程查询节点名称，用于验证对应的节点是否存在
    public SysAuth findByFullname(String fullname);

    //根据父节点的id查询值子节点的最大id
    @Query(value = "select  max(id) from sysauth where pid=?1",nativeQuery = true)
    public int findMaxId(int pid);

    //根据父节点id获取所有的子节点id
    @Query(value = "select * from sysauth where pid=?1",nativeQuery = true)
    public List<SysAuth> findAllChildByPid(int pid);

    //根据节点的id删除节点和子节点
    @Modifying  //在 调用的地方必须加事务，没有事务不能正常执行
    @Query(value = "delete from sysauth where id=?1",nativeQuery = true)
    public int deleteByChird(int id);

    //根据name删除指定的节点和其子节点
    @Modifying
    @Query(value = "delete from sysauth where fullname LIKE ?1",nativeQuery = true)
    public void deleteByName(String name);

    //(用于删除关联表中的记录)根据name查找指定的节点及子节点
    @Query(value = "select * from sysauth where fullname LIKE ?1",nativeQuery = true)
    List<SysAuth> findAllByFullname(String name);

    //根据权限uuid，删除角色权限关联表中角色对应的记录
    @Modifying
    @Query(value = "delete from sysrole_sys_auths where sys_auths_uuid=?1",nativeQuery = true)
    void deleteMaptabByUuid(String uuid);
}
