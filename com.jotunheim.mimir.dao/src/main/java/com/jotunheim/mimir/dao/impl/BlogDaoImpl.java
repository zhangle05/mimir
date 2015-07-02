package com.jotunheim.mimir.dao.impl;
// Generated Jul 2, 2015 6:05:12 PM by Hibernate Tools 4.0.0


import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.jotunheim.mimir.dao.BlogDao;
import com.jotunheim.mimir.domain.Blog;

/**
 * Home object for domain model class Blog.
 * @see com.jotunheim.mimir.domain.Blog
 * @author Hibernate Tools
 */
public class BlogDaoImpl extends BaseDaoImpl implements BlogDao {

    private static final Log log = LogFactory.getLog(BlogDaoImpl.class);

    public void persist(Blog transientInstance) {
        log.debug("persisting Blog instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Blog instance) {
        log.debug("attaching dirty Blog instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Blog instance) {
        log.debug("attaching clean Blog instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Blog persistentInstance) {
        log.debug("deleting Blog instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Blog merge(Blog detachedInstance) {
        log.debug("merging Blog instance");
        try {
            Blog result = (Blog) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Blog findById( long id) {
        log.debug("getting Blog instance with id: " + id);
        try {
            Blog instance = (Blog) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.Blog", id);
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
    
    public List<Blog> findByExample(Blog instance) {
        log.debug("finding Blog instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.Blog")
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

