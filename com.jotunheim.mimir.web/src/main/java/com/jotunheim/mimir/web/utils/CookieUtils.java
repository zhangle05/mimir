package com.jotunheim.mimir.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class CookieUtils {

	private String mPrefix = "";

	public CookieUtils(String prefix) {
		setPrefix(prefix);
	}

	public void setPrefix(String prefix) {
		mPrefix = prefix;
		if (prefix == null) {
			prefix = "";
		}
	}

	/**
	 * 保存一个对象到Cookie里 Cookie只在会话内有效
	 * 
	 * @param key
	 * @param value
	 */
	public void setCookie(HttpServletResponse response, String key,
			String value, int expire) {
		setCookie(response, "/", key, value, expire);
	}

	public void setCookieWithPrefix(HttpServletResponse response, String key,
			String value, int expire) {
		setCookie(response, "/", mPrefix + key, value, expire);
	}

	public void setCookie(HttpServletResponse response, String path,
			String key, String value, int expire) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath(path);
		cookie.setMaxAge(expire);
		// cookie.setDomain(domain);
		response.addCookie(cookie);

	}

	/**
	 * 获取指定键的Cookie
	 * 
	 * @param key
	 * @return 如果找到Cookie则返回 否则返回null
	 */
	public Cookie getCookie(HttpServletRequest request, String key) {
		if (StringUtils.isEmpty(key) || request.getCookies() == null)
			return null;
		for (Cookie cookie : request.getCookies()) {
			if (key.equals(cookie.getName()))
				return cookie;
		}
		return null;
	}

	public Cookie getCookieWithPrefix(HttpServletRequest request, String key) {
		if (StringUtils.isEmpty(key) || request.getCookies() == null)
			return null;
		return getCookie(request, mPrefix + key);
	}

	/**
	 * 获取指定键的Cookie值
	 * 
	 * @param key
	 * @return 如果找到Cookie则返回 否则返回null
	 */
	public String getCookieValue(HttpServletRequest request, String key) {
		Cookie cookie = getCookie(request, key);
		return cookie == null ? null : cookie.getValue();
	}

	/**
	 * 获取指定键的Cookie值(带前缀)
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public String getCookieValueWithPrefix(HttpServletRequest request,
			String key) {
		return getCookieValue(request, mPrefix + key);
	}

	/**
	 * 删除cookie
	 * 
	 * @param response
	 * @param key
	 */
	public void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 移除Cookie
	 * 
	 * @param response
	 * @param key
	 */
	public void removeCookieWidthPrefix(HttpServletResponse response, String key) {
		removeCookie(response, mPrefix + key);
	}

}
