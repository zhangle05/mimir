package com.jotunheim.mimir.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	/**
	 * 获取当前时间，精确到秒（php时间）
	 * 
	 * @return
	 */
	public static long getCurrentPhpTime() {
		return System.currentTimeMillis() / 1000;
	}
	
	
	public static long convertJavaTime(long php){
		return php*1000;
	}
	
	public static Date convertJavaData(long php){
		return new Date(convertJavaTime(php));
	}

	public static String formatDate(Date date, String pattern) {
		if(date!=null){
			SimpleDateFormat formator=new SimpleDateFormat(pattern);
			return formator.format(date);
		}
		return null;
	}

	
	public static Date getYesterday(){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	

	public static Date getToday(){
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static int getCurrentYear(){
		Calendar calendar=Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
}
