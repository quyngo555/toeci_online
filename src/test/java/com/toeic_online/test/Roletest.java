package com.toeic_online.test;

import com.toeic_online.dao.RoleDao;
import com.toeic_online.dao.impl.RoleDaoImpl;
import com.toeic_online.entity.RoleEntity;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class Roletest {
    @Test
    public void checkFindAll(){
        RoleDao roleDao = new RoleDaoImpl();
        List<RoleEntity> list = roleDao.findAll();
    }
    @Test
    public void checkUpdateRole(){
        RoleDao roleDao = new RoleDaoImpl();
        RoleEntity entity = new RoleEntity();
        entity.setRoleId(2);
        entity.setName("USER");
        roleDao.update(entity);
    }
    @Test
    public void checkSaveRole(){
        RoleDao roleDao = new RoleDaoImpl();
        RoleEntity entity = new RoleEntity();
        entity.setRoleId(2);
        entity.setName("ADMIN");
        roleDao.update(entity);
    }

    @Test
    public void checkDeleteRole(){
        List<Integer> listId = new ArrayList<Integer>();
        listId.add(1);
        listId.add(2);
        RoleDao roleDao = new RoleDaoImpl();
        Integer count = roleDao.delete(listId);
    }

    @Test
    public void checkFindById(){
        RoleDao roleDao = new RoleDaoImpl();
        RoleEntity entity = roleDao.findById(1);
    }
}
