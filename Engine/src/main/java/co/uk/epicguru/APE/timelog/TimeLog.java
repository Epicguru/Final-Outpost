package co.uk.epicguru.APE.timelog;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import co.uk.epicguru.API.Pair;
import co.uk.epicguru.API.U;
import co.uk.epicguru.main.FOE;

public final class TimeLog {

	private TimeLog() { }
	
	public static final int BUFFER_SIZE = 240;
	public static TimeSnap current;
	private static ArrayList<TimeSnap> buffer = new ArrayList<TimeSnap>();	
	private static HashMap<String, Pair<Long, Color>> data = new HashMap<String, Pair<Long, Color>>();
	private static boolean inLog = false;
	
	public static void start(){	
		
		if(!FOE.INSTANCE.getScreen().getClass().getName().equals(FOE.screen_Game)){
			return;
		}
		
		data.clear();	
		inLog = false;
		U.startTimer("#Total");
	}
	
	public static void end(){
		
		if(!FOE.INSTANCE.getScreen().getClass().getName().equals(FOE.screen_Game)){
			return;
		}
		
		long total = U.endTimerNanos("#Total");
		long other = total - dataLength();
		
		data.put("Other", new Pair<Long, Color>(other, Color.LIGHT_GRAY));
		
		TimeSnap snap = new TimeSnap(data, total);
		
		buffer.add(snap);
		
		if(buffer.size() > BUFFER_SIZE){
			buffer.remove(0);
		}	
		
		TimeSnap meanValue = new TimeSnap(data);		
		TimeSnap.average(buffer, meanValue);
		
		current = meanValue;
	}
	
	private static long dataLength(){
		long total = 0L;
		
		for(Pair<Long, Color> d : data.values()){
			total += d.getA();
		}
		
		return total;
	}
	
	public static void startLog(final String name){
		if(inLog){
			throw new IllegalStateException("Cannot start a new time log when one is already running! (" + name + ")");
		}
		
		inLog = true;
		
		U.startTimer(name);
	}
	
	public static void endLog(final String name, final Color colour){		
		if(!inLog){
			return;
		}
		
		long time = U.endTimerNanos(name);
		Pair<Long, Color> current = data.get(name);
		if(current != null){
			current.setA(current.getA() + time);
		}else{
			data.put(name, new Pair<Long, Color>(time, colour));
		}
		
		inLog = false;		
	}
}
