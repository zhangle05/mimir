package com.jotunheim.mimir.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class User implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 昵称
     */
    private String userNickName;
    /**
     * 微信OpenId
     */
    private String openId;
    /**
     * 注册时间(微信用户是关注时间)
     */
    private Date regTime;
    /**
     * 是否订阅
     */
    private Integer isSubscribe = 1;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 所在学校, 对应School表
     */
    private long schoolID;
    /**
     * 所在班级, 对应SchoolClass表
     */
    private long classID;
    /**
     * 学校
     */
    private String schoolName;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户是否有效
     */
    private int status;
    /**
     * 菁优登录Token
     */
    private String token;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 所在的市区
     */
    private String district;
    /**
     * 国家
     */
    private String country;
    /**
     * 用户更新时间
     */
    private Date updateTime;
    /**
     * 微信UnionId（同一个微信帐号，UnionId唯一）
     */
    private String unionId;
    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 是否同步微信
     */
    private Integer asyncWechat;

    /**
     * 用户真实名
     */
    private String reallyName;
    /**
     * 用户住址
     */
    private String address;
    /**
     * 用户角色, 对应UserRole表
     */
    private long roleID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String school) {
        this.schoolName = school;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 设置位置信息
     * 
     * @param location
     */
    public void setLocation(Location location) {
        if (location != null) {
            province = location.getProvince();
            city = location.getCity();
            district = location.getDistrict();
        }
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", userName=" + userName
                + ", userPassword=" + userPassword + ", userNickName="
                + userNickName + ", openId=" + openId + ", regTime=" + regTime
                + ", isSubscribe=" + isSubscribe
                + ", lastLoginTime=" + lastLoginTime
                + ", schoolName=" + schoolName + ", phoneNumber=" + phoneNumber
                + ", email=" + email + ", status=" + status + ", token="
                + token + ", province=" + province + ", city=" + city
                + ", district=" + district
                + ", updateTime=" + updateTime + "]";
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAsyncWechat() {
        return asyncWechat;
    }

    public void setAsyncWechat(Integer asyncWechat) {
        this.asyncWechat = asyncWechat;
    }

    public String getReallyName() {
        return reallyName;
    }

    public void setReallyName(String reallyName) {
        this.reallyName = reallyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param roleID the roleID to set
     */
    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    /**
     * @return the schoolID
     */
    public long getSchoolID() {
        return schoolID;
    }

    /**
     * @param schoolID the schoolID to set
     */
    public void setSchoolID(long schoolID) {
        this.schoolID = schoolID;
    }

    /**
     * @return the classID
     */
    public long getClassID() {
        return classID;
    }

    /**
     * @param classID the classID to set
     */
    public void setClassID(long classID) {
        this.classID = classID;
    }

    /**
     * @return the roleID
     */
    public long getRoleID() {
        return roleID;
    }

}
