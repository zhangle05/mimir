/**
 * 
 */
package com.jotunheim.mimir.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jotunheim.mimir.common.utils.CipherHelper;
import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.web.utils.UserHelper;

/**
 * @author zhangle
 *
 */
@Service
public class AccountService {
    private static Log LOG = LogFactory.getLog(AccountService.class);

    public static final int ACCOUNT_LOGIN_SUCCEED = 0;
    public static final int ACCOUNT_NOT_EXIST = -1;
    public static final int ACCOUNT_WRONG_PASSWORD = -2;
    public static final int ACCOUNT_WRONG_COOKIE = -3;

    @Autowired
    private UserDao userDao;

    public LoginData login(String userName, String password,boolean checkPwd){
        User realUser = userDao.findByName(userName);
        if(realUser == null) {
            // try login with lower-case user name
            realUser = userDao.findByName(userName.toLowerCase());
        }
        if(realUser == null) {
            // try login with upper-case user name
            realUser = userDao.findByName(userName.toUpperCase());
        }
        updateLoginTime(realUser);
        LOG.info("login by name:" + userName + ",pswd is:" + password + ",real user is:" + realUser);
        if(!checkPwd){
            return new LoginData(realUser==null ? ACCOUNT_NOT_EXIST : ACCOUNT_LOGIN_SUCCEED, realUser);
        }
        return checkUser(password, realUser);
    }

    public LoginData login(String userName, String password) {
        return login(userName,password,true);
    }

    public LoginData loginByCookie(String userCookie) {
        LOG.info("AccountService.loginByCookie, user cookie is:" + userCookie);
        String[] userCredential = CipherHelper.decrypt(userCookie)
                .split("-");
        if(userCredential == null || userCredential.length < 2) {
            LOG.info("login by cookie, userCredential is:" + userCredential);
            return new LoginData(ACCOUNT_WRONG_COOKIE, null);
        }
        String userName = userCredential[0];
        String password = userCredential[1];
        LOG.info("user:" + userName + ", password:" + userCredential);
        return login(userName, password);
    }

    /**
     * generate cookie string
     *
     * @param userName : login user name
     * @param frontPwd : password input by the user (from front end)
     * @return
     */
    public String generateUserCookie(String userName, String frontPwd) {
        LOG.info("AccountService.generateUserCookie, userName:" + userName + ", frontPwd:" + frontPwd);
        String credential = userName + "-" + frontPwd;
        LOG.info("credential:" + credential);
        return CipherHelper.encrypt(credential);
    }

    private LoginData checkUser(String password, User realUser) {
        if (realUser == null) {
            return new LoginData(ACCOUNT_NOT_EXIST, null);
        } else if (!UserHelper.checkPassword(password, realUser)) {
            return new LoginData(ACCOUNT_WRONG_PASSWORD, null);
        } else {
            return new LoginData(ACCOUNT_LOGIN_SUCCEED, realUser);
        }
    }

    private void updateLoginTime(User realUser) {
        if(realUser == null) {
            return;
        }
        realUser.setLastLoginTime(new java.util.Date());
        userDao.attachDirty(realUser);
    }

    public class LoginData {
        public int statusCode;
        public User realUser;

        public LoginData(int statusCode, User realUser) {
            this.statusCode = statusCode;
            this.realUser = realUser;
        }
    }
}
