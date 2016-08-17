/**
 * 
 */
package com.jotunheim.mimir.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;

/**
 * @author zhangle
 *
 */
@Service
public class UserService {
    
    private static Log LOG = LogFactory.getLog(UserService.class);

    @Autowired
    private UserDao userDao;

    public User findUserByName(String userName) {
        LOG.info("find user by user name:" + userName);
        return userDao.findByName(userName);
    }
    
    public User findUserByMobile(String mobile) {
        LOG.info("find user by mobile:" + mobile);
        return userDao.findByMobile(mobile);
    }
}
