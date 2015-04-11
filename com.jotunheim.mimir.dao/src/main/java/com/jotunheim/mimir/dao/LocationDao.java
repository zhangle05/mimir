package com.jotunheim.mimir.dao;
// Generated Mar 19, 2015 5:05:08 PM by Hibernate Tools 4.0.0


import java.util.List;

import com.jotunheim.mimir.domain.Location;

/**
 * Dao object for domain model class Location.
 * @see com.jotunheim.mimir.domain.Location
 * @author Hibernate Tools
 */
public interface LocationDao {

    public void persist(Location transientInstance);
    
    public void attachDirty(Location instance);
    
    public void attachClean(Location instance);
    
    public void delete(Location persistentInstance);
    
    public Location merge(Location detachedInstance);
    
    public Location findById( long id);
    
    public List findByExample(Location instance);
}

