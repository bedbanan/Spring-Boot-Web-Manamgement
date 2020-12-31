package com.lalala.service.impl;

import com.lalala.pojo.Company;
import com.lalala.respositroy.CompanyRespository;
import com.lalala.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {


    @Autowired
    private CompanyRespository companyRespository;

    @Override
    public void save(Company company) {
        companyRespository.save(company);
    }

    @Override
    public void delete(String uuid) {
        companyRespository.deleteById(uuid);
    }

    @Transactional
    @Override
    public void update(Company company) {
        companyRespository.save(company); //以主键是否为null来判断是否是插入还是更新
    }

    @Override
    public List<Company> findAll() {
        return companyRespository.findAll(); //查询全部
    }

    @Override
    public List<Company> findByNativeSQl(String companyname) {
        return companyRespository.findByNativeSQL(companyname); //利用自己写的模糊查询
    }

    @Transactional
    @Override
    //根据名字来更新地址
    public void updateByName(String comaddress, String comname) {
        companyRespository.updateByName(comaddress, comname);
    }

    @Override
    //简单分页
    public Page<Company> findAllSimplePage(Pageable pageable) {
        return companyRespository.findAll(pageable);
    }

    //根据公司名称保证其唯一性
    @Override
    public boolean validateComname(String comname) {
        int number=companyRespository.validateComname(comname); //true==0 false=1
        if(number==0){
            return true;
        }else return false;
    }

    //验证邮箱的唯一性
    @Override
    public boolean validateEmail(String email) {
        int number=companyRespository.validateEmail(email);//true==0 false=1
        if(number==0){
            return true;
        }else return false;
    }

    //验证联系人电话号码是否是唯一
    @Override
    public boolean validateMobile(String mobile) {
        int number=companyRespository.validateEmail(mobile);//true==0 false=1
        if(number==0){
            return true;
        }else return false;
    }

    /**
     * 多条件查询、
     * 常用的查询包括：equal，notEqual，gt-大于，lt-小于，ge-大于等于，le-小于等于，between-两者之间的值，模糊查询-like
     */
    @Override
    public Page<Company> queryDynamic(Map<String, Object> reqMap, Pageable pageable) {
        Specification specification=new Specification() { //通过Specification来进行多条件的查询
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!reqMap.get("comname").toString().equals("")){ //保证其非空
                    //根据公司名称实现模糊查询
                    predicates.add(cb.like(root.get("comname"), "%"+reqMap.get("comname").toString()+"%"));
                    //cb.like(root.get("comname")获取名称，"%"+reqMap.get("comname").toString()+"%"进行模糊查询
                    //前后都+%的是全模糊匹配。最好是单模糊匹配
                }
                if(!reqMap.get("comstatus").toString().equals("全部")){  //运营状态
                    predicates.add(cb.equal(root.get("comstatus"), reqMap.get("comstatus").toString()));

                }
                if(!reqMap.get("employeenumber").toString().equals("")){ //查询其人数
                    predicates.add(cb.equal(root.get("employeenumber"),Integer.parseInt(reqMap.get("employeenumber").toString())));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return  this.companyRespository.findAll(specification,pageable);
    }
}
