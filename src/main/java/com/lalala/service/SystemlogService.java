package com.lalala.service;

import com.lalala.pojo.Systemlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 作为日志的Service层
 */
public interface SystemlogService {
    /**
     * 保存日志
     */
    void save(Systemlog systemlog);

    /**
     * 多条件的动态查询日志文件
     *
     */
    Page<Systemlog> queryDynamic(Map<String,Object> reqMap, Pageable pageable);
}
