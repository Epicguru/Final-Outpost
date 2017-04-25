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
	
	public static void reset(){
		// TODO
		// Reset all time here
		
		time = 0f;		
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
}
