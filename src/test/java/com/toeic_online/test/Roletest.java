package com.toeic_online.test;

import com.toeic_online.dao.RoleDao;
import com.toeic_online.dao.impl.RoleDaoImpl;
import com.toeic_online.entity.RoleEntity;
import org.testng.annotations.Test;

import java.util.List;

public class Roletest {
    @Test
    public void checkFindAll(){
        RoleDao roleDao = new RoleDaoImpl();
        List<RoleEntity> list = roleDao.findAll();
    }
}
