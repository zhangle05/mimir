/**
 * 
 */
package com.jotunheim.mimir.web.utils;

import com.jotunheim.mimir.common.utils.CipherHelper;

/**
 * @author zhangle
 *
 */
public class SharedConstants {
    /**
     * cookie for user auto-login
     */
    public static final String USER_COOKIE_KEY = CipherHelper.md5sum("Octopus-UserCookie");
    /**
     * cookie expire time, in seconds
     */
    public static final int COOKIE_EXPIRE_TIME = 3600 * 24 * 15;

    public static final int AJAXCODE_CLIENT_DATA_ERROR = 0;

    public static final int AJAXCODE_OK = 1;

    public static final int AJAXCODE_OK_WITH_MINOR_ISSUE = 2;

    public static final int AJAXCODE_SYSTEM_ERROR = -1;

    public static final int AJAXCODE_AUTHENTICATION_ERROR = -2;

    public static final int AJAXCODE_ROLE_ACCESS_LEVEL_ERROR = -3;

    public static final String AJAX_CODE_KEY = "code";

    public static final String AJAX_MSG_KEY = "message";

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int DEFAULT_ABSTRA_LENGTH = 20;
}
