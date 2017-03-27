package co.uk.epicguru.API;

import java.util.ArrayList;

/**
 * A utility class that manages and runs {@link AllocatedTimer} objects.
 * @see {@link #add(AllocatedTimer)} to add timers and {@link #removeTimer(AllocatedTimer)} to remove a timer.
 * @author James Billy
 */
public final class Allocator {

	private static ArrayList<AllocatedTimer> timers = new ArrayList<>();
	private static boolean running;
	private static Thread thread;
	private static final long UPS = 200;

	/**
	 * Adds a new timer ({@link AllocatedTimer} object) to the active timers.
	 * @param timer The timer object to pass, will be ignored if null.
	 * @return The timer object for storage.
	 */
	public static AllocatedTimer add(AllocatedTimer timer){
		if(timer != null && !timers.contains(timer))
			timers.add(timer);
		return timer;
	}

	/**
	 * Removes a timer added with {@link #add(AllocatedTimer)}.
	 * @param timer The timer to remove as returned by the add() method. If null is ignored.
	 */
	public static void removeTimer(AllocatedTimer timer){
		if(timer == null || !timers.contains(timer))
			return;
		timers.remove(timer);
	}

	public static void start(){
		if(running)
			return;

		running = true;
		thread = new Thread(() ->{
			// Run all timers
			// Refresh rate of UPS times per second
			long lastTime = 0L;
			long minTime = 1000L / UPS;

			while(running){		
				long time = System.currentTimeMillis();
				// Get time passed
				long passed = time - lastTime;
				
				for(AllocatedTimer timer : timers){
					// Calculate delta
					float delta = passed / 1000f;

					timer.update(delta, () -> {
						// Triggered
						timer.callSuperRunnable();
					});
				}

				// Done
				lastTime = time;
				
				try {
					Thread.sleep(minTime);
				} catch (InterruptedException e) { }
			}
		});
		thread.start();
	}

	public static void stop(){
		if(!running)
			return;
		running = false;
		timers.clear();
		thread = null;
	}

	/**
	 * Gets the amount of running timers.
	 */
	public static int getRunningTimers() {
		return timers.size();
	}
}
