/**
 * 
 */
package com.jotunheim.mimir.web.service;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jotunheim.mimir.common.utils.CipherHelper;
import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.utils.UserHelper;
import com.jotunheim.mimir.web.utils.SharedConstants;

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

    public LoginResult login(String userName, String password,boolean checkPwd){
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
            return new LoginResult(realUser==null ? ACCOUNT_NOT_EXIST : ACCOUNT_LOGIN_SUCCEED, realUser);
        }
        return checkUser(password, realUser);
    }

    public LoginResult login(String userName, String password) {
        return login(userName,password,true);
    }

    public LoginResult loginByCookie(String userCookie) {
        LOG.info("AccountService.loginByCookie, user cookie is:" + userCookie);
        try {
            String[] userCredential = CipherHelper.decrypt(userCookie)
                    .split("-");
            if(userCredential == null || userCredential.length < 2) {
                LOG.info("login by cookie, userCredential is:" + userCredential);
                return new LoginResult(ACCOUNT_WRONG_COOKIE, null);
            }
            String userName = userCredential[0];
            String password = userCredential[1];
            LOG.info("user:" + userName + ", password:" + userCredential);
            return login(userName, password);
        } catch (Exception ex) {
            LOG.error("login by cookie failed, exception is:" + ex + "-" + ex.getMessage());
            return new LoginResult(ACCOUNT_WRONG_COOKIE, null);
        }
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

    public synchronized boolean addUser(User user, JSONObject json) {
        LOG.info("AccountService.addUser:" + user);
        try {
            if(user.getId() > 0) {
                json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                json.put(SharedConstants.AJAX_MSG_KEY, "user id '" + user.getId() + "' is wrong!");
                return false;
            }
            if(userDao.findByName(user.getUserName()) != null) {
                json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                json.put(SharedConstants.AJAX_MSG_KEY, "user name '" + user.getUserName() + "' is taken!");
                return false;
            }
            if(!StringUtils.isEmpty(user.getPhoneNumber())
                    && userDao.findByMobile(user.getPhoneNumber()) != null) {
                json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                json.put(SharedConstants.AJAX_MSG_KEY, "phone number '" + user.getPhoneNumber() + "' is taken!");
                return false;
            }
            userDao.persist(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.put(SharedConstants.AJAX_MSG_KEY, ex.toString() + "-" + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean changePswd(long uid, String oldPswd, String newPswd, JSONObject json) {
        return false;
    }

    private LoginResult checkUser(String password, User realUser) {
        if (realUser == null) {
            return new LoginResult(ACCOUNT_NOT_EXIST, null);
        } else if (!UserHelper.checkPassword(password, realUser)) {
            return new LoginResult(ACCOUNT_WRONG_PASSWORD, null);
        } else {
            return new LoginResult(ACCOUNT_LOGIN_SUCCEED, realUser);
        }
    }

    private void updateLoginTime(User realUser) {
        if(realUser == null) {
            return;
        }
        realUser.setLastLoginTime(new java.util.Date());
        userDao.attachDirty(realUser);
    }

    public class LoginResult {
        public int statusCode;
        public User realUser;

        public LoginResult(int statusCode, User realUser) {
            this.statusCode = statusCode;
            this.realUser = realUser;
        }
    }

}
