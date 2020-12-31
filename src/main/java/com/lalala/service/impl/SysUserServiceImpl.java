package com.lalala.service.impl;

import com.lalala.pojo.SysRole;
import com.lalala.pojo.SysUser;
import com.lalala.respositroy.SysRoleRespository;
import com.lalala.respositroy.SysUserRepository;
import com.lalala.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 账号业务层实现类
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private SysRoleRespository sysRoleRespository;

    /**
     * 保存功能
     * @param sysUser
     */
    @Override
    public void save(SysUser sysUser) {
        //采用Bcryt加密
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String password=passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(password);

        //根据账号对应的角色id(sysroleid),获取角色id，方面前端显示
        SysRole sysRole=sysRoleRespository.findByUuid(sysUser.getSysroleid());
        sysUser.setSysrolename(sysRole.getRolename());
        sysUser.setSysRole(sysRole);
        sysUserRepository.save(sysUser);
    }

    /**
     *  根据账号/邮箱/手机号三者之一查询账号
     */
    @Override
    public SysUser findByUsernameOrUseremailOrUsermobile(String username, String email, String mobile) {
        return sysUserRepository.findByUsernameOrUseremailOrUsermobile(username,email,mobile);
    }

    /**
     *带查询条件的分页查询
     */
    @Override
    public Page<SysUser> queryDynamic(Map<String, Object> reqMap, Pageable pageable) {
        Specification querySpecifi=new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!(reqMap.get("username")==null||reqMap.get("username").toString().equals("")))//账号名称，like 模糊查询
                {
                    predicates.add(criteriaBuilder.like(root.get("username"),"%"+reqMap.get("username").toString()+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return this.sysUserRepository.findAll(querySpecifi,pageable);
    }

    /**
     * 查询对应的账号名称是否存在（服务层用于唯一性验证）,如果用户已经存在返回false，否则返回true
     */
    @Override
    public boolean validateUsername(String username) {
        int intCount=sysUserRepository.validateUsername(username);
        if(intCount==0){return true;}
        else{return false;}
    }

    /**
     * 邮箱号唯一性验证(如果已经存在，返回false，否则返回true)
     */
    @Override
    public boolean validateEmail(String email) {
        int intCount=sysUserRepository.validateEmail(email);
        if(intCount==0){return true;}
        else{return false;}
    }

    /**
     * 查询手机号的唯一性
     * @param mobile
     * @return
     */
    @Override
    public boolean validateMobile(String mobile) {
        int count=sysUserRepository.validateMobile(mobile);
        if(count==0){
            return true;
        }else{
            return false;
        }
    }
}
