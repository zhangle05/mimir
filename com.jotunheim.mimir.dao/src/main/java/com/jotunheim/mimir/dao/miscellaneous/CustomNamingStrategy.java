/**
 * 
 */
package com.jotunheim.mimir.dao.miscellaneous;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * @author zhangle
 *
 */
public class CustomNamingStrategy extends ImprovedNamingStrategy {
    private static final long serialVersionUID = 1L;

    private static final String sPrefix = "mimir";

    private String tableName;

    @Override
    public String tableName(String tableName) {
        this.tableName = tableName;
        // lower case for table names
        return (sPrefix.toLowerCase().trim() + "_" + tableName.toLowerCase());
    }

    @Override
    public String columnName(String columnName) {
        // upper case for column names
        return (sPrefix.toUpperCase().trim() + "_" + tableName.toUpperCase() + "_" + columnName.toUpperCase());
    }
}
