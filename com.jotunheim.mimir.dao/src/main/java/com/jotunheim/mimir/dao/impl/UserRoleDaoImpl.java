package com.jotunheim.mimir.dao.impl;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;

import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.UserRole;

/**
 * DaoImpl object for domain model class UserRole.
 * @see com.jotunheim.mimir.domain.UserRole
 * @author Hibernate Tools
 */
public class UserRoleDaoImpl extends BaseDaoImpl implements UserRoleDao {

    private static final Log log = LogFactory.getLog(UserRoleDaoImpl.class);

    public void persist(UserRole transientInstance) {
        log.debug("persisting UserRole instance");
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().persist(transientInstance);
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(UserRole instance) {
        log.debug("attaching dirty UserRole instance");
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(UserRole instance) {
        log.debug("attaching clean UserRole instance");
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(UserRole persistentInstance) {
        log.debug("deleting UserRole instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public UserRole merge(UserRole detachedInstance) {
        log.debug("merging UserRole instance");
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            UserRole result = (UserRole) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public UserRole findById( long id) {
        log.debug("getting UserRole instance with id: " + id);
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            UserRole instance = (UserRole) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.UserRole", id);
            sessionFactory.getCurrentSession().getTransaction().commit();
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(UserRole instance) {
        log.debug("finding UserRole instance by example");
        try {
            sessionFactory.getCurrentSession().beginTransaction();
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.UserRole")
                    .add(Example.create(instance))
            .list();
            sessionFactory.getCurrentSession().getTransaction().commit();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

