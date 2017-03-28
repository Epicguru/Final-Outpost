package co.uk.epicguru.API;

/**
 * An object that allows for a runnable object to be called a certain number of times per time unit.
 * That time unit may be seconds, minutes, hours or any other unit. This is not guaranteed to run exactly on time, but will never
 * miss a call to the run method as long as the {@link Allocator} is still running.
 * @author James Billy
 */
public class AllocatedTimer extends Base {

	private float TPM = 0;
	private float timer;
	private Runnable runabble;
	
	/**
	 * Creates a new timer based on the amount of triggers every hour.
	 * @param timesPerHour The times per hour that the created object will trigger run() in the {@link #update(float, Runnable)} method.
	 * @return The new allocated timer object.
	 */
	public static AllocatedTimer inHour(float timesPerHour, Runnable run){
		return new AllocatedTimer(timesPerHour / 60f, run);
	}
	
	/**
	 * Creates a new timer based on the amount of triggers every minute.
	 * @param timesPerHour The times per hour that the created object will trigger run() in the {@link #update(float, Runnable)} method.
	 * @return The new allocated timer object.
	 */
	public static AllocatedTimer inMinute(float timesPerMinute, Runnable run){
		return new AllocatedTimer(timesPerMinute, run);
	}
	
	/**
	 * Creates a new timer based on the amount of triggers every second.
	 * @param timesPerHour The times per hour that the created object will trigger run() in the {@link #update(float, Runnable)} method.
	 * @return The new allocated timer object.
	 */
	public static AllocatedTimer inSecond(float timesPerSecond, Runnable run){
		return new AllocatedTimer(timesPerSecond * 60f, run);
	}
	
	private AllocatedTimer(float timesPerMinute, Runnable run){	
		this.TPM = timesPerMinute;
		this.runabble = run;
	}
	
	/**
	 * Updates the timer and calls run() as many times as is necessary.
	 * @param delta The time in seconds between this call and the last call to this method.
	 * @param run The Runnable object to run when a reset of timer is triggered, for example once every second.
	 */
	public void update(float delta, Runnable run){
		this.timer += delta;
		
		float interval = 1f / (TPM / 60f);
		
		while(this.timer >= interval){
			this.timer -= interval;
			run.run();
		}
	}
	
	/**
	 * Calls the run method on the runnable object passed in the constructor.
	 */
	public void callSuperRunnable(){
		if(this.runabble != null)
			this.runabble.run();
	}

	/**
	 * Sets the calls per minute of this timer.
	 */
	public void setTPM(float newTPM){
		this.TPM = newTPM;
	}
}
