package com.lalala.service.impl;

import com.lalala.pojo.Systemlog;
import com.lalala.respositroy.SystemlogRespository;
import com.lalala.service.SystemlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志管理的实现类
 */
@Service("systemlogService")
public class SystemlogServiceImpl implements SystemlogService {

    @Autowired
    private SystemlogRespository systemlogRespository;

    /**
     * 保存日志
     * @param systemlog
     */
    @Override
    public void save(Systemlog systemlog) {
        systemlogRespository.save(systemlog);
    }

    /**
     * 多条件的日志动态查询
     * 常用的查询包括：equal，notEqual，gt-大于，lt-小于，ge-大于等于，le-小于等于，between-两者之间的值，模糊查询-like
     */
    @Override
    public Page<Systemlog> queryDynamic(Map<String, Object> reqMap, Pageable pageable) {
        Specification space=new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!reqMap.get("username").toString().equals("")){ //保证其非空
                    //根据公司名称实现模糊查询
                    predicates.add(cb.like(root.get("username"), "%"+reqMap.get("username").toString()+"%"));
                    //cb.like(root.get("username")获取名称，"%"+reqMap.get("username").toString()+"%"进行模糊查询
                    //前后都+%的是全模糊匹配。最好是单模糊匹配
                }
                if(!reqMap.get("operatetype").toString().equals("全部")){  //操作状态
                    predicates.add(cb.equal(root.get("operatetype"), reqMap.get("operatetype").toString()));

                }
                if(!reqMap.get("operatedesc").toString().equals("")){ //查询其操作简述
                    predicates.add(cb.equal(root.get("operatedesc"),Integer.parseInt(reqMap.get("operatedesc").toString())));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return this.systemlogRespository.findAll(space,pageable);
    }
}
