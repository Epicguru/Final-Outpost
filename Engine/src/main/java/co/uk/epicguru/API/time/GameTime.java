package co.uk.epicguru.API.time;

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
	 * Utility methods will be limited to 
	 */
	
	private static float time; // All hail!
	private static final int HOURS_IN_DAY = 24;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int SECONDS_IN_MINUTE = 60;
	
	public static void reset(){
		// TODO
		// Reset all time here
		
		time = 0f;		
	}	
	
	/*
	 * Getter methods, utility.
	 */
	
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
	
	private static void add(float time){
		// Log, dunno?
		time += time;		
	}
	
	public static void addDays(float days){
		add(days); // Already in correct format
	}
	
	public static void addHours(float hours){
		// (1 day / 24 hours) * hours == hours in terms of days. Clear as mud!
		float hoursInDays = (1f / HOURS_IN_DAY) * hours;
		
		add(hoursInDays);
	}
	
	public static void addMinutes(float minutes){
		// ((1 day / 24 hours) / 60 minutes) * minutes == minutes in terms of days.
		float minutesInDays = ((1f / HOURS_IN_DAY) / MINUTES_IN_HOUR) * minutes;
		
		add(minutesInDays);
	}
	
	public static void addSeconds(float seconds){
		// (((1 day / 24 hours) / 60 minutes) / 60 seconds) * seconds = seconds in terms of days. Very efficient.
		float secondsInDays = (((1f / HOURS_IN_DAY) / MINUTES_IN_HOUR) / SECONDS_IN_MINUTE) * seconds;
		
		add(secondsInDays);
	}
}
