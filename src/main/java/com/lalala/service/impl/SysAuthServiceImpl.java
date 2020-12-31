package com.lalala.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lalala.pojo.SysAuth;
import com.lalala.pojo.SysRole;
import com.lalala.pojo.Ztree;
import com.lalala.respositroy.SysAuthRespository;
import com.lalala.respositroy.SysRoleRespository;
import com.lalala.service.SysAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限业务层实现类
 */
@Service("sysAuthService")
public class SysAuthServiceImpl implements SysAuthService {

    @Autowired
    private SysRoleRespository sysRoleRespository;
    @Autowired
    private SysAuthRespository authRespository;
    /**
     * 查询所有权限
     */
    @Override
    public List<SysAuth> findAll() {

        return authRespository.findAll();
    }

    /**
     * 查找所有权限并以集合Ztree的形式存储，并且和角色的权限相对应，以方便贩判断是否展开或者勾选，默认的role的id为nouuid
     */
    @Override
    public List<Ztree> findAllToZtree(String roleid) {
        List<Ztree> list=new ArrayList<>();
        SysRole sysRole=sysRoleRespository.findByUuid(roleid); //通过权限表的roleid去查找角色表相对应的角色
        List<SysAuth> listauth=findAll(); //查询所有的权限信息
        for(SysAuth auth:listauth){
            //以全部权限为基准进行循环，并和角色对应的权限进行比较，用来设定权限的open和checked属性
            boolean blchecked=false;
            if(!roleid.equals("nouuid")){
                String str=auth.getFullname(); //获取所有权限名
                for(SysAuth roleAuth:sysRole.getSysAuth()){
                    if(roleAuth.getFullname().equals(str)){
                        blchecked=true;
                        break;
                    }
                }
            }
            Ztree z=new Ztree();
            z.id=auth.getId();
            z.pId=auth.getPid();
            z.name=auth.getTreename();
            z.open=true;
            z.checked=blchecked;
            list.add(z);
        }
        return  list;
    }

    /**
     * 添加节点的子节点：
     * 1.根据节点id查询对应的节点信息：
     * 2.再根据查询到的节点信息和新的节点名称进行名组合，以该组合名称查询对应的节点对应信息是否存在
     * 3.如果新节点信息不存在，则保存该新节点id是当前选定节点的id，name是需要新增加子节点的名称
     */
    @Override
    public String saveChildAuth(int id, String childname) {
        if(id==0)//一级权限
        {
            SysAuth sysAuth_Child=authRespository.findByFullname(childname);
            if(sysAuth_Child!=null)//子节点已经存在
            {
                JSONObject result = new JSONObject();
                result.put("msg","exist");
                return result.toJSONString();
            }
            else//节点未存在
            {
                SysAuth newAuth=new SysAuth();
                newAuth.setFullname(childname);//一级节点的全称和树型显示名称一致
                newAuth.setPid(id);
                newAuth.setId(findMaxId(id));
                newAuth.setTreename(childname);//一级节点的全称和树型显示名称一致
                authRespository.save(newAuth);

                JSONObject result = new JSONObject();
                result.put("msg","ok");
                return result.toJSONString();
            }
        }
        else//非一级权限
        {
            SysAuth sysAuth_Parent=authRespository.findById(id);
            String strChildName=sysAuth_Parent.getFullname()+"_"+childname;
            SysAuth sysAuth_Child=authRespository.findByFullname(strChildName);
            if(sysAuth_Child!=null)//子节点已经存在
            {
                JSONObject result = new JSONObject();
                result.put("msg","exist");
                return result.toJSONString();
            }
            else//子节点未存在
            {
                SysAuth newAuth=new SysAuth();
                newAuth.setFullname(strChildName);
                newAuth.setPid(id);
                newAuth.setId(findMaxId(id));
                newAuth.setTreename(childname);
                authRespository.save(newAuth);

                JSONObject result = new JSONObject();
                result.put("msg","ok");
                return result.toJSONString();
            }
        }
    }

    /**
     * 根据id查找对应的权限信息
     */
    @Override
    public SysAuth findById(int id) {
        return authRespository.findById(id);
    }

    /**
     * 根据节点的全称，查找对应的权限信息
     */
    @Override
    public SysAuth findByFullName(String fullname) {
        return authRespository.findByFullname(fullname);
    }

    /**
     *根据父节点的id，获取其子节点的最大id
     */
    @Override
    public int findMaxId(int pid) {
        //判断是否有子节点
        List<SysAuth> sysAuths=authRespository.findAllChildByPid(pid);
        if(sysAuths.size()==0){//没有子节点
            int intNewId=pid*10+1;
            return intNewId;
        }
        else{//有子节点
            return authRespository.findMaxId(pid)+1;
        }
    }

    /**
     * 根据节点id删除节点及其子节点
     */
    @Override
    @Transactional
    public String deleteByChild(int id) {
        SysAuth sysAuth=authRespository.findById(id);
        //先批量删除角色权限中间表对应的记录
        List<SysAuth> list=authRespository.findAllByFullname(sysAuth.getFullname()+"%");
        for (SysAuth sa:list) {
            authRespository.deleteMaptabByUuid(sa.getUuid());
        }

        //批量删除本身节点及子节点
        authRespository.deleteByName(sysAuth.getFullname()+"%");
        return JSON.toJSONString(sysAuth);//这里有返回值，主要是为后面的基于AOP的日志捕捉服务的
    }

    /**
     * 根据name删除指定节点机器子节点
     */
    @Override
    @Transactional
    public void deleteByName(String name) {
        authRespository.deleteByName(name+"%");
    }

    /**
     * 给选定的角色赋予权限，其中"authsinfo"是以$分割节点id字符串
     */
    @Override
    public void editRole(String uuid, String authsinfo) {
        /**
         * 处理前端传过来的字符串形式组装的多权限信息，建立角色和权限的映射关系
         */
        //得到角色信息
        SysRole sysRole=sysRoleRespository.findByUuid(uuid);

        //根据勾选的权限节点id，得到对应的权限对象，并给角色中的权限集合属性赋值
        List<SysAuth> list=new ArrayList<SysAuth>();
        String[] arrAuthid=authsinfo.split("\\$");
        for(int i=0,num=arrAuthid.length;i<num;i++)
        {
            SysAuth sysAuth=authRespository.findById(Integer.parseInt(arrAuthid[i]));
            list.add(sysAuth);
        }
        sysRole.setSysAuth(list);

        //保存或者更新角色信息
        sysRoleRespository.save(sysRole);
    }
}
