package com.jotunheim.mimir.common.utils;

import java.net.URLEncoder;
import java.util.Formatter;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");

	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

	private static final Pattern YEAR_PATTERN = Pattern
			.compile("\\d{4}-\\d{4}学年|\\d{4}年");

	private static final Pattern HTML_TAG = Pattern.compile("<([^>]*)>");
	public static boolean isEmpty(String res) {
		return res == null || res.trim().isEmpty();
	}

	public static String escape(String res, int count, String append) {
		if (!isEmpty(res)) {
			String tmp = res.trim();
			if (tmp.length() > count && count > 0) {
				tmp = tmp.substring(0, count);
				tmp = tmp.trim();
				if (!isEmpty(append)) {
					tmp += append;
				}
			}
			return tmp;
		}
		return res;
	}

	public static void main(String[] args) {
		
		
	}

	public static String filter(String src) {
		if (!isEmpty(src)) {
			return src.replaceAll("contenteditable=\\\"true\\\"", "")
					.replaceAll("contenteditable='true'", "")
					.replaceAll("http://img.jyeoo.net", "/redirect");
		}
		return src;
	}

	public static String filterHtml(String html) {
		if (!isEmpty(html)) {
			Matcher matcher = HTML_TAG.matcher(html);
			boolean isMatch = matcher.find();
			StringBuffer sb = new StringBuffer();
			while (isMatch) {
				matcher.appendReplacement(sb, "");
				isMatch = matcher.find();
			}
			matcher.appendTail(sb);
			return sb.toString().replace("&nbsp;", "").trim();
		}
		return html;
	}

	/**
	 * 编号
	 * 
	 * @param code
	 * @return
	 */
	public static String getLetterCode(String code) {
		try {
			int result = Integer.valueOf(code);
			return (char) (result + 'A') + "";
		} catch (Exception e) {
		}

		return code;
	}

	/**
	 * 编号
	 * 
	 * @param code
	 * @return
	 */
	public static String getLetterCode(int code) {
		try {
			return (char) (code + 'A') + "";
		} catch (Exception e) {
		}
		return "";
	}

	public static String formatUserNickName(String src) {
		if (!isEmpty(src)) {
			if (isMobile(src)) {
				return src.substring(0, 3) + "****" + src.substring(7);
			} else {
				if (src.length() > 4) {
					return src.substring(0, 4) + "...";
				}
			}
		}
		return src;
	}

	/**
	 * 是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		if (!isEmpty(str)) {
			return PHONE_NUMBER_PATTERN.matcher(str).find();
		}
		return false;
	}

	/**
	 * 是否是邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (!isEmpty(str)) {
			return EMAIL_PATTERN.matcher(str).find();
		}
		return false;
	}

	public static String formatJavaScript(String str) {
		if (!isEmpty(str)) {
			str = str.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\\'");
			str = str.replace("\r\n", " ");
			str = str.replace("\n", " ");
			str = str.replace("<", "&lt;");
			str = str.replace(">", "&gt;");
		}
		return str;
	}

	public static HashMap<String, String> splitTitle(String title) {
		if (!isEmpty(title)) {
			Matcher m = YEAR_PATTERN.matcher(title);
			if (m.find()) {
				HashMap<String, String> map = new HashMap<String, String>();
				int start = m.start();
				int end = m.end();
				if (start == 0) {
					map.put("year", title.substring(start, end));
					map.put("title", title.substring(end));
				} else {
					map.put("year", title.substring(start, end));
					map.put("title", title);
				}
				return map;
			}
		}
		return null;
	}

	public static int editDistance(String first, String second) {
		if (first == null) {
			first = "";
		}
		if (second == null) {
			second = "";
		}
		if (first.length() == 0) {
			return second.length();
		}
		if (second.length() == 0) {
			return first.length();
		}

		int firstLen = first.length() + 1;
		int secondLen = second.length() + 1;

		int[][] distanceMatrix = new int[firstLen][secondLen];
		for (int i = 0; i < firstLen; i++) {
			distanceMatrix[i][0] = i;
		}
		for (int j = 0; j < secondLen; j++) {
			distanceMatrix[0][j] = j;
		}

		for (int i = 1; i < firstLen; i++) {
			for (int j = 1; j < secondLen; j++) {
				int deletion = distanceMatrix[i - 1][j] + 1;
				int insertion = distanceMatrix[i][j - 1] + 1;
				int substitution = distanceMatrix[i - 1][j - 1];
				if (first.charAt(i - 1) != second.charAt(j - 1)) {
					substitution++;
				}
				distanceMatrix[i][j] = Math.min(Math.min(insertion, deletion),
						substitution);
			}
		}
		return distanceMatrix[firstLen - 1][secondLen - 1];
	}

	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (Exception e) {
		}
		return url;
	}

	public static String getStackTrace(Throwable throwable) {
		if (throwable != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(throwable.getClass().getName() + ":"
					+ throwable.getMessage() + "\n");
			StackTraceElement[] elements = throwable.getStackTrace();
			int count = 0;
			for (StackTraceElement element : elements) {
				count++;
				sb.append("\tat " + element.getClassName() + "."
						+ element.getMethodName() + "(" + element.getFileName()
						+ ":" + element.getLineNumber() + ")" + "\n");
				if (count > 10) {
					break;
				}
			}
			return sb.toString();
		}
		return null;
	}

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	public static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	
	/**
	 * 截断字符串
	 * @param src
	 * @param length
	 * @return
	 */
	public static String cutStr(String src,int length){
		if(!isEmpty(src)&&length>0){
			if(src.length()>length){
				return src.substring(0, length);
			}
		}		
		return src;
	}
	

}
