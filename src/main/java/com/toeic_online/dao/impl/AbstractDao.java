package com.toeic_online.dao.impl;

import com.toeic_online.constant.CoreConstant;
import com.toeic_online.dao.GenericDao;
import com.toeic_online.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractDao<ID extends Serializable, T> implements GenericDao<ID, T> {

    private Class<T> persistenceClass;

    public AbstractDao() {
        this.persistenceClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public String getPersistenceClassName() {
        return persistenceClass.getSimpleName();
    }
    public List<T> findAll() {
        List<T> list = new ArrayList<T>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            StringBuilder sql = new StringBuilder("from ");
            sql.append(this.getPersistenceClassName());
            Query query = session.createQuery(sql.toString());
            list = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return list;
    }
    public T update(T entity){
        T result = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Object object = session.merge(entity);
            result = (T)object;
            transaction.commit();
        }catch (HibernateException e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
        return result;
    }
    public T save(T entity){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            session.persist(entity);
            transaction.commit();
        }catch (HibernateException e){
            transaction.rollback();
        }finally {
            session.close();
        }
        return entity;
    }
    public T findById(ID id){
        T result = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = (T) session.get(persistenceClass, id);
            if(result == null){
                throw new ObjectNotFoundException("NOT FOUND " + id, null);
            }
        }catch (HibernateException e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
        return result;
    }

    public Integer delete(List<ID> ids){
        Integer count = 0;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            for(ID item: ids){
                T t = (T) session.get(persistenceClass, item);
                session.delete(t);
                count++;
            }
            transaction.commit();
        }catch (HibernateException e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
        return count;
    }
    private void setParameterToQuery(Object[] nameQuery, org.hibernate.Query query) {
        if (nameQuery.length == 3) {
            String[] params = (String[]) nameQuery[1];
            Object[] values = (Object[]) nameQuery[2];
            for (int i2 = 0; i2 < params.length ; i2++) {
                query.setParameter(params[i2], values[i2]);
            }
        }
    }
    public Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, String whereClause){
        List<T> list = new ArrayList<T>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Object totalItem = 0;
        Object[] nameQuery = HibernateUtil.buildNameQuery(property);
        try{
            StringBuilder sql1 = new StringBuilder("from");
            sql1.append(getPersistenceClassName()).append(" where 1=1 ").append(nameQuery[0]);
            if(sortExpression != null && sortDirection != null){
                sql1.append(" order by ").append(sortExpression);
                sql1.append(" "+(sortDirection.equals(CoreConstant.SORT_ASC)?"asc":"desc"));

            }
            if(whereClause != null){
                sql1.append(whereClause);
            }
            Query query1 = session.createQuery(sql1.toString());
            setParameterToQuery(nameQuery, query1);
            if(offset != null && offset >= 0){
                query1.setFirstResult(offset);
            }
            if(limit != null && limit > 0){
                query1.setMaxResults(limit);
            }
            list = query1.list();
            StringBuilder sql2 = new StringBuilder("select count(*) from ");
            sql2.append(getPersistenceClassName()).append(" where 1=1 ").append(nameQuery[0]);
            if(whereClause != null){
                sql2.append(whereClause);
            }
            Query query2 = session.createQuery(sql2.toString());
            setParameterToQuery(nameQuery, query2);
            totalItem = query2.list().get(0);
            transaction.commit();
        }catch (HibernateException e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
        return new Object[]{totalItem, list};
    }
}


