package com.fqueue.commons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeCategory {
	private SimpleDateFormat timeFormat = null;
	private static Calendar cal;
	public TimeCategory(){
		timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
	}
	
	public static String getCategory(Date date){
		cal = Calendar.getInstance();
		cal.setTime(date);
		
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int am_pm = cal.get(Calendar.AM_PM);
		String ampm = null;
		String category = null;		
		
		if (am_pm == 1)
			ampm = "PM";
		else if (am_pm == 0)
			ampm = "AM";
		
		if (minute < 30){
			if (hour==0){
				category = "12:30 " + ampm;
			}else
			category = formatHour(hour) + ":30 " + ampm;
		}else{
			if ((hour+1) == 12 && ampm.equals("AM"))
				ampm = "PM";
			else if ((hour+1) == 12 && ampm.equals("PM"))
				ampm = "AM";
			category = formatHour(hour+1) + ":00 " + ampm;
		}
		return category;
	}
	
	private static String formatHour(int hour){
		NumberFormat formatter = new DecimalFormat("00");
		String newHour = formatter.format(hour);
		return newHour;
	}
}
