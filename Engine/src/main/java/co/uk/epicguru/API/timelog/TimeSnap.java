package co.uk.epicguru.API.timelog;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

import co.uk.epicguru.API.Pair;

public class TimeSnap {

	public String[] names;
	public float[] percentages;
	public float[] radi;
	public Color[] colours;
	
	public TimeSnap(HashMap<String, Pair<Long, Color>> data, long total){
		
		this.names = new String[data.size()];
		int i = 0;
		for(String s : data.keySet()){
			this.names[i++] = s;
		}
		
		this.radi = new float[data.size()];
		for(int x = 0; x < this.radi.length; x++) this.radi[x] = 1f;
		
		i = 0;
		this.colours = new Color[data.size()];
		for(Pair<Long, Color> p : data.values()){
			this.colours[i++] = p.getB();
		}
		
		i = 0;
		this.percentages = new float[data.size()];
		for(Pair<Long, Color> p : data.values()){
			this.percentages[i++] = (float)p.getA() / (float)total;
		}
	}
	
	public TimeSnap(HashMap<String, Pair<Long, Color>> data){
		this.names = new String[data.size()];
		int i = 0;
		for(String s : data.keySet()){
			this.names[i++] = s;
		}
		this.radi = new float[data.size()];
		for(int x = 0; x < this.radi.length; x++) this.radi[x] = 1f;
		i = 0;
		this.colours = new Color[data.size()];
		for(Pair<Long, Color> p : data.values()){
			this.colours[i++] = p.getB();
		}
	}
	
	public static void average(ArrayList<TimeSnap> array, TimeSnap object){
		
		float[] percentages = new float[array.get(0).percentages.length];
		for(TimeSnap out : array){
			for(int x = 0; x < out.percentages.length; x++){
				percentages[x] += out.percentages[x];
			}
		}
		
		for(int x = 0; x < percentages.length; x++){
			percentages[x] /= array.size();
		}
		
		int i = 0;
		for(String s : object.names){
			String test = (int)(percentages[i] * 100f) + "";
			if(test.length() < 2){
				test = "0" + test;
			}
			test += "%: ";
			object.names[i++] = test + s;
		}
//		
//		Arrays.sort(object.names);
//		ArrayUtils.reverse(object.names);
		
		object.percentages = percentages;		
	}
	
}
