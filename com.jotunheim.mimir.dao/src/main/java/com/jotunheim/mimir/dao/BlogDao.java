package com.jotunheim.mimir.dao;
// Generated Jul 2, 2015 6:05:12 PM by Hibernate Tools 4.0.0


import java.util.List;

import com.jotunheim.mimir.domain.Blog;

/**
 * Home object for domain model class Blog.
 * @see com.jotunheim.mimir.domain.Blog
 * @author Hibernate Tools
 */
public interface BlogDao {

    public void persist(Blog transientInstance);
    
    public void attachDirty(Blog instance);
    
    public void attachClean(Blog instance);
    
    public void delete(Blog persistentInstance);
    
    public Blog merge(Blog detachedInstance);
    
    public Blog findById( long id);
    
    public List<Blog> findByExample(Blog instance);
    
    public List<Blog> listBlogs(int page, int pageSize);
    
    public int getBlogCount();
}

