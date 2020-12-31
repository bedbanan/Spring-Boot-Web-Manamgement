package com.lalala.service;

//公司接口

import com.lalala.pojo.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface CompanyService {
    //保存功能
    void save(Company company);

    //根据ID删除记录
    void delete(String uuid);

    @Transactional //声明式事务管理。要么成功要么失败，利用aop实现不影响其他事务的运行
    void update(Company company);

    //查询所有
    List<Company> findAll();

    //执行原生SQL语句的查询
    List<Company> findByNativeSQl(String companyname);

    //根据公司名称更新地址
    @Transactional
    void updateByName(String comaddress,String comname);

    //简单分页查询
    Page<Company> findAllSimplePage(Pageable pageable);

    //验证公司名称唯一性
    boolean validateComname(String comname);

    //验证公司邮箱的唯一性
    boolean validateEmail(String email);

    //验证手机号的唯一性
    boolean validateMobile(String mobile);

    //多条件动态查询
    Page<Company> queryDynamic(Map<String,Object> res,Pageable pageable);
}
