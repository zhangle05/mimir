package com.jotunheim.mimir.dao;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import com.jotunheim.mimir.domain.UserRole;

/**
 * Dao object for domain model class UserRole.
 * @see com.jotunheim.mimir.domain.UserRole
 * @author Hibernate Tools
 */
public interface UserRoleDao {

    public void persist(UserRole transientInstance);
    
    public void attachDirty(UserRole instance);
    
    public void attachClean(UserRole instance);
    
    public void delete(UserRole persistentInstance);
    
    public UserRole merge(UserRole detachedInstance);
    
    public UserRole findById( long id);
    
    public List findByExample(UserRole instance);
}

