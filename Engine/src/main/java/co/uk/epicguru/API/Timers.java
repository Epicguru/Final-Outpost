package co.uk.epicguru.API;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public final class Timers {

	private Timers() { }
	
	private static ArrayList<TimersRenderOutput> array = new ArrayList<TimersRenderOutput>();
	
	public static long UI;
	public static long entities;
	public static long physics;
	public static long tilesRender;
	public static long entitiesRender;
	public static long light;
	public static long other;
	
	public static void startRenderUI(){
		U.startTimer("#RenderUI");
	}
	
	public static void endRenderUI(){
		UI = U.endTimerNanos("#RenderUI");
	}
	
	public static void startLight(){
		U.startTimer("#Light");
	}
	
	public static void endLight(){
		light = U.endTimerNanos("#Light");
	}
	
	public static void startEntitiesRender(){
		U.startTimer("#EntitiesRender");
	}
	
	public static void endEntitiesRender(){
		entitiesRender = U.endTimerNanos("#EntitiesRender");
	}
	
	public static void startEntities(){
		U.startTimer("#Entities");
	}
	
	public static void endEntities(){
		entities = U.endTimerNanos("#Entities");
	}
	
	public static void startPhysics(){
		U.startTimer("#Physics");
	}
	
	public static void endPhysics(){
		physics = U.endTimerNanos("#Physics");
	}
	
	public static void startTiles(){
		U.startTimer("#Tiles");
	}
	
	public static void endTiles(){
		tilesRender = U.endTimerNanos("#Tiles");
	}
	
	public static void startAll(){
		U.startTimer("#All");
	}
	
	public static void endAll(){
		long others = UI + entities + physics + tilesRender + light;
		other = U.endTimerNanos("#All") - others;
	}
	
	public static TimersRenderOutput getOutput(){
		
		TimersRenderOutput out = new TimersRenderOutput();
		
		long total = UI + entities + physics + tilesRender + light + other;
		final int vars = 7;
		
		// Render
		float rUI = ((float)UI / (float)total);
		float er = ((float)entitiesRender / (float)total);
		float e = ((float)entities / (float)total);
		float l = ((float)light / (float)total);
		float p = ((float)physics / (float)total);
		float t = ((float)tilesRender / (float)total);
		float o = ((float)other / (float)total);
		
		out.percentages = new float[]{
			t,
			er,
			l,
			rUI,
			e,
			p,
			o
		};
		out.colours = new Color[]{
			Color.PINK,
			Color.PURPLE,
			Color.CYAN,
			Color.FIREBRICK,
			Color.GREEN,
			Color.BROWN,
			Color.GRAY
		};
		
		out.radi = new float[vars];
		for(int i = 0; i < vars; i++){
			out.radi[i] = 1f;
		}
		
		array.add(out);
		if(array.size() > 240){
			array.remove(0);
		}
		
		TimersRenderOutput ti = new TimersRenderOutput();
		TimersRenderOutput.average(array, ti);
		ti.colours = out.colours;
		ti.radi = out.radi;
		
		return ti;
	}
	
}
