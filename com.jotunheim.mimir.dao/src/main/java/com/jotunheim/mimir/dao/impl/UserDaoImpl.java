package com.jotunheim.mimir.dao.impl;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.transaction.annotation.Transactional;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.utils.UserHelper;

/**
 * DaoImpl object for domain model class User.
 * @see com.jotunheim.mimir.domain.User
 * @author Hibernate Tools
 */
@Transactional
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    private static final Log log = LogFactory.getLog(UserDaoImpl.class);

    public void persist(User transientInstance) {
        log.debug("persisting User instance");
        try {
            transientInstance.setUserPassword(UserHelper.generateEncryptPassword(transientInstance));
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(User instance) {
        log.debug("attaching dirty User instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(User instance) {
        log.debug("attaching clean User instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(User persistentInstance) {
        log.debug("deleting User instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public User merge(User detachedInstance) {
        log.debug("merging User instance");
        try {
            User result = (User) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public User findById( java.lang.Long id) {
        log.debug("getting User instance with id: " + id);
        try {
            User instance = (User) sessionFactory.getCurrentSession()
                    .get("com.jotunheim.mimir.domain.User", id);
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
    
    public List findByExample(User instance) {
        log.debug("finding User instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.jotunheim.mimir.domain.User")
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

    public User findByName(String name) {
        log.debug("getting User instance with name: " + name);
        try {
            List<User> result = getReadTemplate().find(
                    "from User u where u.userName=?", name);
            if (result.size() > 0) {
                log.debug("find by name successful, result is: " + result.get(0));
                return result.get(0);
            }
            return null;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public int getUserCount() {
        log.debug("getting User count");
        try {
            Long count = (Long)getReadTemplate().find(
                    "select count(*) from User as user").iterator().next();
            if(count != null) {
                log.debug("get user count successful, result is: " + count.intValue());
                return count.intValue();
            }
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
        return 0;
    }

    public List listUsers(int page, int pageSize) {
        log.debug("list users, page is:" + page + ", pageSize is:" + pageSize);
        try {
            Query q = sessionFactory.getCurrentSession().createQuery("from User"); 
            q.setFirstResult(page * pageSize); 
            q.setMaxResults(pageSize);
            List results = q.list();
            log.debug("list user successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List findBySchool(long schoolID) {
        log.debug("getting User instance with schoolID: " + schoolID);
        try {
            List<User> result = getReadTemplate().find(
                    "from User u where u.schoolID=?", schoolID);
            if (result.size() > 0) {
                log.debug("find by schoolID successful, result is: " + result.get(0));
                return result;
            }
            return null;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public User findByMobile(String mobile) {
        log.debug("getting User instance with mobile number: " + mobile);
        try {
            List<User> result = getReadTemplate().find(
                    "from User u where u.phoneNumber=?", mobile);
            if (result.size() > 0) {
                log.debug("find by mobile successful, result is: " + result.get(0));
                return result.get(0);
            }
            return null;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}

