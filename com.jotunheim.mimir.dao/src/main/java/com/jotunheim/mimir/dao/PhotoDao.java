package com.jotunheim.mimir.dao;
// Generated Jul 2, 2015 6:05:12 PM by Hibernate Tools 4.0.0


import java.util.List;

import com.jotunheim.mimir.domain.Photo;

/**
 * Home object for domain model class Photo.
 * @see com.jotunheim.mimir.domain.Photo
 * @author Hibernate Tools
 */
public interface PhotoDao {

    public void persist(Photo transientInstance);
    
    public void attachDirty(Photo instance);
    
    public void attachClean(Photo instance);
    
    public void delete(Photo persistentInstance);
    
    public Photo merge(Photo detachedInstance);
    
    public Photo findById( long id);
    
    public List<Photo> findByExample(Photo instance);
    
    public List<Photo> listPhotos(int page, int pageSize);
    
    public int getPhotoCount();
}

