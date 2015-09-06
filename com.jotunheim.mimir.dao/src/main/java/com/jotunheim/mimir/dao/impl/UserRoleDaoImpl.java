package com.jotunheim.mimir.dao.impl;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Transactional;

import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.UserRole;

/**
 * DaoImpl object for domain model class UserRole.
 * @see com.jotunheim.mimir.domain.UserRole
 * @author Hibernate Tools
 */
@Transactional
public class UserRoleDaoImpl extends BaseDaoImpl implements UserRoleDao {

    private static final Log log = LogFactory.getLog(UserRoleDaoImpl.class);

    public void persist(UserRole transientInstance) {
        log.debug("persisting UserRole instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
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
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
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
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
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
            UserRole result = (UserRole) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
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
            UserRole instance = (UserRole) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.UserRole", id);
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
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.UserRole")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

