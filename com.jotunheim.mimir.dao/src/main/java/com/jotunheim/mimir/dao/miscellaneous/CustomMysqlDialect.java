/**
 * 
 */
package com.jotunheim.mimir.dao.miscellaneous;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author zhangle
 *
 */
public class CustomMysqlDialect extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
