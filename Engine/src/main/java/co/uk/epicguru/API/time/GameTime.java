package co.uk.epicguru.API.time;

import com.badlogic.gdx.math.Interpolation;

import co.uk.epicguru.languages.Lan;

public class GameTime {

	/*
	 * TODO Optimise wholeX() methods!
	 * Time will be measured in days.
	 * Time in a day will simply be a floating point value, so:
	 * Day 2.5 		is day 2 midday.
	 * Day 2.0 		is day 2 midnight.
	 * Day 2.9	 	is day 2 around 11 pm.
	 * 
	 * This means that every 0.5 days is obviously 12 hours, with the day starting at x.0.
	 * 
	 * I'll try out every RL second being 1 minute in game, meaning that one minute becomes and hour.
	 * A whole day would be 24 minutes, not bad.
	 */

	private static float time; // All hail!
	private static final int HOURS_IN_DAY = 24;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int DAYS_IN_WEEK = 7;

	// Common time values
	public static final float ONE_DAY = (1f);
	public static final float ONE_HOUR = (ONE_DAY / HOURS_IN_DAY);
	public static final float ONE_MINUTE = (ONE_HOUR / MINUTES_IN_HOUR);
	public static final float ONE_SECOND = (ONE_MINUTE / SECONDS_IN_MINUTE);

	// Other time values
	public static final float ONE_WEEK = (ONE_DAY * DAYS_IN_WEEK);

	// Visual data values
	public static final String TIME_SEP = ":";
	public static final String AM = "AM";
	public static final String PM = "PM";

	public static boolean is24Hour = false;

	public static void reset(){
		// Reset all time here		
		time = 0f;		
	}	

	/*
	 * Getter methods, utility.
	 */

	public static float getTime() {
		return time;
	}

	private static int wholeDay(){
		return (int)time;
	}

	public static float partDay(){
		return time - wholeDay();
	}

	public static float hour(){
		float part = partDay(); // Between 0 and 1, in whole days.
		float hour = HOURS_IN_DAY * part;

		/*
		 * 0.0  = 00.0 // 0 o'clock, midnight AM
		 * 1.0  = 24.0 // 12 0'clock, midnight PM (actually never occurs, it is next day)
		 * 0.5  = 12.0 // 12 o'clock, midday PM
		 * 0.25 = 06.0 // 6 0'clock, 6 AM
		 * 0.75 = 18.0 // 6 0'clock, 6 PM
		 */

		return hour;
	}

	public static int wholeHour(){
		return (int)hour();
	}

	public static float partHour(){
		return hour() - wholeHour();
	}

	public static float minute(){
		float part = partHour(); // Between 0 and 1, in whole hours
		float minutes = MINUTES_IN_HOUR * part;

		return minutes;
	}

	public static int wholeMinute(){
		return (int)minute();
	}

	public static float partMinute(){
		return minute() - wholeMinute();
	}

	public static float second(){
		float part = partMinute();
		float minutes = SECONDS_IN_MINUTE * part;

		return minutes;
	}

	public static int wholeSecond(){
		return (int)second();
	}

	public static float partSecond(){
		return second() - wholeSecond();
	}

	/*
	 * Setter methods, functional.
	 * TODO Optimise addX() methods.
	 */

	public static void add(float time){
		// Log, dunno?

		GameTime.time += time;
	}

	public static void addDays(float days){
		add(days); // Already in correct format
	}

	public static void addHours(float hours){
		// (1 day / 24 hours) * hours == hours in terms of days. Clear as mud!
		float hoursInDays = ONE_HOUR * hours;

		add(hoursInDays);
	}

	public static void addMinutes(float minutes){
		// ((1 day / 24 hours) / 60 minutes) * minutes == minutes in terms of days.
		float minutesInDays = ONE_MINUTE * minutes;

		add(minutesInDays);
	}

	public static void addSeconds(float seconds){
		// (((1 day / 24 hours) / 60 minutes) / 60 seconds) * seconds = seconds in terms of days. Very efficient.
		float secondsInDays = ONE_SECOND * seconds;

		add(secondsInDays);
	}

	/*
	 * String representation methods below.
	 * WIP!
	 */

	private static final StringBuilder str = new StringBuilder();

	public static boolean isAM(){
		float part = partDay();

		if(part >= 0.5f){
			return false;
		}else{
			return true;			
		}
	}

	public static int to12Hour(int hour){

		// If > 12 then subtract 12		
		if(hour > 12){
			return hour - 12;
		}else{
			return hour;
		}
	}

	public static String timeString(TimeStyle style){
		int hour = wholeHour();
		int minute = wholeMinute();
		int second = wholeSecond();

		str.setLength(0);

		// Add values
		switch(style){
		case MINIMAL:

			// 12:16

			str.append(is24Hour ? hour : to12Hour(hour));
			str.append(TIME_SEP);
			str.append(minute);
			break;
		case MEDIUM:

			// Default
			// 12:16 PM

			str.append(is24Hour ? hour : to12Hour(hour));
			str.append(TIME_SEP);
			str.append(minute);

			if(!is24Hour){				
				str.append(' ');
				str.append(isAM() ? AM : PM);
			}
			break;
		case FULL:

			// 12h 16m 12s PM

			str.append(is24Hour ? hour : to12Hour(hour));
			str.append("h ");
			str.append(minute);
			str.append("m ");
			str.append(second);
			str.append("s ");

			if(!is24Hour){				
				str.append(isAM() ? AM : PM);
			}
			break;
		}

		return str.toString();
	}

	private static String getDOW(int day){
		switch(day){
		case 0:
			return "MONDAY";
		case 1:
			return "TUESDAY";
		case 2:
			return "WEDNESDAY";
		case 3:
			return "THURSDAY";
		case 4:
			return "FRIDAY";
		case 5:
			return "SATURDAY";
		case 6:
			return "SUNDAY";
		}

		return null;
	}

	public static String dateString(TimeStyle style){

		int day = wholeDay();
		int week = (int)(day / DAYS_IN_WEEK);
		int dayOfWeek = (int)(day % DAYS_IN_WEEK);

		str.setLength(0);

		switch(style){
		case MINIMAL:

			// D64 

			str.append("D");
			str.append(day);
			break;
		case MEDIUM:

			// Day 64

			str.append(Lan.str("DAY"));
			str.append(' ');
			str.append(day);

			break;

		case FULL:

			// Day 64, Week 9, Monday

			str.append(Lan.str("DAY"));
			str.append(' ');
			str.append(day);
			str.append(", ");

			str.append(Lan.str("WEEK"));
			str.append(' ');
			str.append(week);
			str.append(", ");

			str.append(Lan.str(getDOW(dayOfWeek)));

			break;
		}

		return str.toString();
	}

	public static String makeString(){
		return makeString(TimeStyle.MEDIUM);
	}

	public static String makeString(TimeStyle style){
		return dateString(style) + " - " + timeString(style);
	}

	/*
	 * Light and day-time methods, functional.
	 */

	private static float percentage(float start, float end, float current){
		float range = end - start;
		float addition = current - start;
		return addition / range;
	}

	public static float getAmbientLightAlphaLevel(){
		float light = GameTime.isAM() ? GameTime.partDay() / 0.5f : 1f - (GameTime.partDay() - 0.5f) / 0.5f;

		float maxLight = 0.9f;
		
		return Interpolation.pow2.apply(light) * maxLight;
	}

	public static float getAmbientLightRedLevel(){

		float red = 0;

		/*
		 * START: 0 AM
		 * PEAK: 6:30 AM
		 * END: 10:30 AM
		 * 
		 * START: 6:30 PM
		 * PEAK: 20:30 PM
		 * END: 24:00 PM
		 */

		float maxRed = 0.1f;

		Interpolation i = Interpolation.linear;
		
		if(isAM()){
			// Sunrise
			if(partDay() <= 6.5f * ONE_HOUR){
				// Before peak
				float p = percentage(0, 6.5f * ONE_HOUR, partDay());

				red = maxRed * i.apply(p);
			}else if(partDay() <= 10.5f * ONE_HOUR){
				// After peak
				float p = percentage(6.5f * ONE_HOUR, 10.5f * ONE_HOUR, partDay());
				
				p = 1f - i.apply(p);
				red = maxRed * p;
			}
		}else{
			// Sunset
			if(partDay() >= 16.5f * ONE_HOUR && partDay() <= 20.5f * ONE_HOUR){
				// Before peak
				float p = percentage(16.5f * ONE_HOUR, 20.5f * ONE_HOUR, partDay());

				red = maxRed * 1.5f * i.apply(p);
			}else if(partDay() > 20.5f * ONE_HOUR){
				// After peak
				float p = percentage(20.5f * ONE_HOUR, 24f * ONE_HOUR, partDay());
				
				p = 1f - p;
				red = maxRed * 1.5f * i.apply(p);
			}
		}

		return red;
	}
}
