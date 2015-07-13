/**
 * 
 */
package com.jotunheim.mimir.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhangle
 *
 */
public class Blog implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private String author;

    private Date createTime;

    private Date lastUpdateTime;

    /**
     * abstract
     */
    private String abstra;

    private String htmlBody;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the abstra
     */
    public String getAbstra() {
        return abstra;
    }

    /**
     * @param abstra the abstra to set
     */
    public void setAbstra(String abstra) {
        this.abstra = abstra;
    }

    /**
     * @return the htmlBody
     */
    public String getHtmlBody() {
        return htmlBody;
    }

    /**
     * @param htmlBody the htmlBody to set
     */
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
