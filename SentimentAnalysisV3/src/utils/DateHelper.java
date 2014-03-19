package utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;



public class DateHelper {
	
	public DateHelper(){
		
		
	}
	
	public String getWeekday(String date){
		String weekday = "Weekday not defined";
		String year = date.split("T")[0].split("-")[0];
		String month = date.split("T")[0].split("-")[1];
		String day = date.split("T")[0].split("-")[2];
		
		System.out.println("YEAR: " + year);
		System.out.println("MONTH: " + month);
		System.out.println("DAY: " + day);
		
		int intYear = Integer.parseInt(year);
		int intMonth = Integer.parseInt(month);
		int intDay = Integer.parseInt(day);
		
    	DateTime d = new DateTime(intYear, intMonth, intDay, 0,0);
//		DateTime d = new DateTime(2014, 02, 19, 0,0);
		System.out.println("DateTime day of week: " + d.getDayOfWeek());

		
		switch (d.getDayOfWeek()) {
		case 1:
			weekday = "Monday";
			break;
		case 2:
			weekday = "Tuesday";
			break;
		case 3:
			weekday = "Wednesday";
			break;
		case 4:
			weekday = "Thursday";
			break;
		case 5:
			weekday = "Friday";
			break;
		case 6:
			weekday = "Saturday";
			break;
		case 7:
			weekday = "Sunday";
			break;
		default:
			break;
		}
		System.out.println(weekday);
		
		return weekday;
		
	}
	
	
	public static void main(String[] args){
		DateHelper help = new DateHelper();
		help.getWeekday("2013-01-14T13:29:53Z");
		
	}

}
