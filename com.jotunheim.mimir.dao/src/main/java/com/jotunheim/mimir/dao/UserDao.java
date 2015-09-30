package com.jotunheim.mimir.dao;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import com.jotunheim.mimir.domain.User;

/**
 * Dao object for domain model class User.
 * @see com.jotunheim.mimir.domain.User
 * @author Hibernate Tools
 */
public interface UserDao {

    public void persist(User transientInstance);

    public void attachDirty(User instance);
    
    public void attachClean(User instance);
    
    public void delete(User persistentInstance);
    
    public User merge(User detachedInstance);
    
    public User findById( java.lang.Long id);
    
    public List<User> findByExample(User instance);

    public User findByName(String name);

    public List<User> findBySchool(long schoolID);

    public User findByMobile(String mobile);

    public int getUserCount(boolean isSupervisor);

    public List<User> listUsers(int page, int pageSize, boolean isSupervisor);
}

