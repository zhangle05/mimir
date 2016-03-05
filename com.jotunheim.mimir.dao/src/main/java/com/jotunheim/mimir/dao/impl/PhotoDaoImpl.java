package com.jotunheim.mimir.dao.impl;
// Generated Jul 2, 2015 6:05:12 PM by Hibernate Tools 4.0.0


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Transactional;

import com.jotunheim.mimir.dao.PhotoDao;
import com.jotunheim.mimir.domain.Photo;

/**
 * Home object for domain model class Photo.
 * @see com.jotunheim.mimir.domain.Photo
 * @author Hibernate Tools
 */
@Transactional
public class PhotoDaoImpl extends BaseDaoImpl implements PhotoDao {

    private static final Log log = LogFactory.getLog(PhotoDaoImpl.class);

    public void persist(Photo transientInstance) {
        log.debug("persisting Photo instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(Photo instance) {
        log.debug("attaching dirty Photo instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Photo instance) {
        log.debug("attaching clean Photo instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(Photo persistentInstance) {
        log.debug("deleting Photo instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Photo merge(Photo detachedInstance) {
        log.debug("merging Photo instance");
        try {
            Photo result = (Photo) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public Photo findById( long id) {
        log.debug("getting Photo instance with id: " + id);
        try {
            Photo instance = (Photo) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.Photo", id);
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
    
    public List<Photo> findByExample(Photo instance) {
        log.debug("finding Photo instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.Photo")
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

    public List<Photo> listPhotos(int page, int pageSize) {
        log.debug("list photos, page is:" + page + ", pageSize is:" + pageSize);
        try {
//            Session session = sessionFactory.openSession();
            Query q = sessionFactory.getCurrentSession().createQuery("FROM Photo ORDER BY createTime DESC"); 
            q.setFirstResult(page * pageSize); 
            q.setMaxResults(pageSize);
            List<Photo> results = q.list();
            log.debug("list photo successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public int getPhotoCount() {
        log.debug("getting Photo count");
        try {
            Query q = sessionFactory
                    .getCurrentSession()
                    .createQuery(
                            "SELECT COUNT(id) FROM Photo");
            List results = q.list();
            Long count = (Long) results.iterator().next();
            if (count != null) {
                log.debug("get Photo count successful, result is: "
                        + count.intValue());
                return count.intValue();
            }
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
        return 0;
    }
}

