/**
 * 
 */
package com.jotunheim.mimir.domain.utils;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jotunheim.mimir.common.utils.CipherHelper;
import com.jotunheim.mimir.domain.User;

/**
 * @author konglh
 *
 */
public final class UserHelper {
    private static Log LOG = LogFactory.getLog(UserHelper.class);
    private static final String DEFAULT_SALT_PREFIX = "mimir_backend-";
    private static final String DEFAULT_CLIENT_SALT_PREFIX = "octopus_front-";
    private static final int DEFAULT_PASSWORD_LENGTH = 6;

    /**
     * Generate encrypted password with salt. salt is mixed by a default string
     * and the user name string.
     * 
     * @param user user must has both set his id and password
     * @return encrypted password
     */
    public static String generateEncryptPassword(User user) {
        if (user == null) {
            return ""; // TODO: throw exception
        }
        return UserHelper.generateEncryptPassword(user.getUserName(),
                user.getUserPassword());
    }

    /**
     * Generate encrypted password with salt.
     * 
     * @param userName
     *            user's name
     * @param passwd
     *            initial encrypted password
     * @return salted encrypted password
     */
    public static String generateEncryptPassword(String userName, String passwd) {
        String passwdWithSalt = DEFAULT_SALT_PREFIX + userName + passwd;
        LOG.debug("generateEncryptPassword for:" + userName + ", passwd:" + passwd);
        passwd = CipherHelper.md5sum(passwdWithSalt);
        LOG.debug("encrypted passwd:" + passwd);
        return passwd;
    }

    /**
     * Check user's inputing password
     * 
     * @param passwd
     *          initial encrypted user's input password
     * @param realUser
     *          user stored in server
     * @return whether the user's inputing name/password pair is correct
     */
    public static boolean checkPassword(String passwd, User realUser) {
        if (StringUtils.isEmpty(passwd) || realUser == null) {
            return false;
        }
        return UserHelper.checkPassword(realUser.getUserName(), passwd, realUser);
    }

    /**
     * Check user's inputing password
     * 
     * @param userName
     *            unique user name of the user
     * @param passwd
     *            initial encrypted user's input password
     * @param realUser
     *            user stored in server
     * @return whether the user's inputing name/password pair is correct
     */
    public static boolean checkPassword(String userName, String passwd,
            User realUser) {
        if (StringUtils.isEmpty(passwd)|| realUser == null) {
            return false;
        } else if (!UserHelper.generateEncryptPassword(userName, passwd).equals(
                realUser.getUserPassword())) {
            return false;
        }
        return true;
    }

    /**
     * Generate plain text password for initialize the user,
     * the default password is a string of DEFAULT_PASSWORD_LENGTH random numbers
     * 
     * @return
     */
    public static String generatePlainPassword() {
        String result = "";
        char[] pswdCharPool = {'1','2','3','4','5','6','7','8','9','0'};
        Random r = new Random(System.currentTimeMillis());
        for(int i=0; i<DEFAULT_PASSWORD_LENGTH; i++) {
            result += pswdCharPool[r.nextInt(pswdCharPool.length)];
        }
        return result;
    }

    /**
     * Generate client encrypted password from the very origin of a plain text password
     * 
     * @param plainPswd
     * @return
     */
    public static String generateClientPassword(String plainPswd) {
        String passwdWithSalt = DEFAULT_CLIENT_SALT_PREFIX + plainPswd;
        return CipherHelper.md5sum(passwdWithSalt);
    }
}
