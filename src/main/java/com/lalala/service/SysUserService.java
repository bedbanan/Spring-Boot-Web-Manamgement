package com.lalala.service;

import com.lalala.pojo.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 账号Service层接口
 */
public interface SysUserService {
    /**
     * 添加新账号
     */
    void save(SysUser sysUser);

    /**
     * 根据账号/邮箱/电话号码之一查询账号
     */
    SysUser findByUsernameOrUseremailOrUsermobile(String username,String email,String mobile);

    /**
     * 带查询条件的分页查询
     */
    Page<SysUser> queryDynamic(Map<String,Object> reqMap, Pageable pageable);

    /**
     * 账号名称唯一性验证(如果已经存在，返回false，否则返回true)
     */

    boolean validateUsername(String username);

    /**
     * 邮箱号唯一性验证(如果已经存在，返回false，否则返回true)
     */
    boolean validateEmail(String email);

    /**
     * 手机号唯一性验证(如果已经存在，返回false，否则返回true)
     */
    boolean validateMobile(String mobile);
}
