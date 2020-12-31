package com.lalala.respositroy;

import com.lalala.pojo.Systemlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 系统日志的仓库层
 */
@Repository
public interface SystemlogRespository extends JpaRepository<Systemlog,String>, JpaSpecificationExecutor {
}
