package com.lalala.service.impl;

import com.lalala.pojo.SysRole;
import com.lalala.respositroy.SysRoleRespository;
import com.lalala.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 角色业务层实现类
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleRespository sysRoleRespository;

    @Override
    public SysRole findById(String uuid) {
        return sysRoleRespository.findByUuid(uuid);
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleRespository.findAll();
    }

    @Override
    public void save(SysRole sysRole) {
        sysRoleRespository.save(sysRole);
    }

    @Override
    @Transactional
    public void deleteByuuid(String uuid) {
        sysRoleRespository.deleteByUuid(uuid);
    }

    @Override
    @Transactional
    public void deleteMaptabByUuid(String uuid) {
        sysRoleRespository.deleteMaptabByUuid(uuid);
    }
}
