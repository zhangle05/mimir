/**
 * 
 */
package com.jotunheim.mimir.domain.data;

import java.util.ArrayList;
import java.util.List;

import com.jotunheim.mimir.domain.UserRole;

/**
 * @author zhangle
 *
 */
public class RoleAccessLevel {
    public static final List<UserRole> allRoleList;

    public static final int USER = 1;
    public static final int ADMIN = 8;
    public static final int SUPERVISOR = 16;

    public static final long  ROLE_ID_USER = 1;
    public static final long  ROLE_ID_ADMIN = 2;
    public static final long  ROLE_ID_SUPERVISOR = 3;

    static {
        allRoleList = new ArrayList<UserRole>();
        allRoleList.add(new UserRole(ROLE_ID_USER, "user", USER, "注册用户"));
        allRoleList.add(new UserRole(ROLE_ID_ADMIN, "admin", ADMIN, "管理员"));
        allRoleList.add(new UserRole(ROLE_ID_SUPERVISOR, "supervisor", SUPERVISOR, "超级管理员"));
    }

    public static int getAccessLevel(long roleID) {
        if(roleID == ROLE_ID_USER) {
            return USER;
        }
        else if(roleID == ROLE_ID_ADMIN) {
            return ADMIN;
        }
        else if(roleID == ROLE_ID_SUPERVISOR) {
            return SUPERVISOR;
        }
        return 0;
    }


    public static List<UserRole> getRoleList(UserRole loginRole) {
        List<UserRole> roleList = new ArrayList<UserRole>();
        if(loginRole == null) {
            return roleList;
        }
        for(UserRole r : allRoleList) {
            if(r.getAccessLevel() < loginRole.getAccessLevel()) {
                roleList.add(r);
            }
        }
        return roleList;
    }
}
