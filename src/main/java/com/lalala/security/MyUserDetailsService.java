package com.lalala.security;

import com.lalala.pojo.SysAuth;
import com.lalala.pojo.SysUser;
import com.lalala.service.SysAuthService;
import com.lalala.service.SysRoleService;
import com.lalala.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户加载类，用于验证用户，在验证通过的情况下，加载用户所对应的全部权限
 * 报我们自己定义好的账号转换为Spring security内的账号
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Resource(name="sysUserService")
    private SysUserService sysUserService;

    @Resource(name = "sysRoleService")
    private SysRoleService sysRoleService;

    @Resource(name = "sysAuthService")
    private SysAuthService sysAuthService;

    @Autowired
    private HttpSession session;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //根据账号名称、邮箱、手机号进行搜索(用JPA的方式，参数是名称/手机号/邮箱之一)，则通过
        SysUser sysUser=sysUserService.findByUsernameOrUseremailOrUsermobile(s,s,s);
        if(sysUser==null){
            throw new UsernameNotFoundException("密码或者账号错误！！！");
        }
        //根据用户id创建session
        session.setAttribute("userinfo",sysUser);
        List<SysAuth> sysAuths=new ArrayList<>(); //获取对应用户的权限
        if(sysUser.getSysRole().getRolename().equals("超级管理员")){
            sysAuths=sysAuthService.findAll();
        }else{
            for(SysAuth sysAuth:sysUser.getSysRole().getSysAuth()){  //不是超级管理员就根据对应的角色赋值
                sysAuths.add(sysAuth);
            }
        }
        //将前面获取是syaAuths的集合转换成GrantedAuthority的
        //将权限信息添加到GrantedAuthority对象中，在后面进行权限的验证时会使用到GrantedAuthority对象
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        for(SysAuth sysAuth:sysAuths){
            if(sysAuth!=null&&sysAuth.getFullname()!=null){
                GrantedAuthority grantedAuthority=new SimpleGrantedAuthority(sysAuth.getFullname());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new User(sysUser.getUsername(),sysUser.getPassword(),grantedAuthorities);
    }
}
