package com.jotunheim.mimir.dao.impl;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.criterion.Example;

import com.jotunheim.mimir.dao.LocationDao;
import com.jotunheim.mimir.domain.Location;

/**
 * DaoImpl object for domain model class Location.
 * @see com.jotunheim.mimir.domain.Location
 * @author Hibernate Tools
 */
public class LocationDaoImpl extends BaseDaoImpl implements LocationDao {

    private static final Log log = LogFactory.getLog(LocationDaoImpl.class);

    public void persist(Location transientInstance) {
        log.debug("persisting Location instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Location instance) {
        log.debug("attaching dirty Location instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Location instance) {
        log.debug("attaching clean Location instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Location persistentInstance) {
        log.debug("deleting Location instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Location merge(Location detachedInstance) {
        log.debug("merging Location instance");
        try {
            Location result = (Location) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Location findById( long id) {
        log.debug("getting Location instance with id: " + id);
        try {
            Location instance = (Location) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.Location", id);
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
    
    public List findByExample(Location instance) {
        log.debug("finding Location instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.Location")
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

