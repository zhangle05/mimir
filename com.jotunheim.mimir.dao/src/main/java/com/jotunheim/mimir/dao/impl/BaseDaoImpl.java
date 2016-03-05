/**
 * 
 */
package com.jotunheim.mimir.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author zhangle
 *
 */
public abstract class BaseDaoImpl {

    private HibernateTemplate template = null;
    protected SessionFactory sessionFactory;

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected HibernateTemplate getReadTemplate() {
        if (template == null) {
            template = new HibernateTemplate(sessionFactory);
        }
        return template;
    }

    protected void setUtf8mb4(){
        Query q = sessionFactory.getCurrentSession().createSQLQuery("SET NAMES utf8mb4");
        q.executeUpdate();
    }
}
