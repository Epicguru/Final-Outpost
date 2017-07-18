package co.uk.epicguru.screens.hooks;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.Timers;
import co.uk.epicguru.API.TimersRenderOutput;
import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.API.time.GameTime;
import co.uk.epicguru.API.time.TimeStyle;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.languages.Lan;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class DebugHook extends ScreenHook {

	public static BitmapFont font = new BitmapFont();
	public static boolean active = false;
	public ShapeRenderer shapes;
	
	float y = 0;
	float y2 = 0;
	
	@Override
	public void renderUI(float delta, Batch batch) {
		
		if(Main.INSTANCE.isInputJustDown(Main.DEBUG)){
			active = !active;
		}
		
		if(!active)
			return;
		
		if(shapes == null){
			shapes = new ShapeRenderer();
		}
		
		float total = (float) (Runtime.getRuntime().totalMemory() / Math.pow(1024, 3));
		float used = (float) (total - Runtime.getRuntime().freeMemory() / Math.pow(1024, 3));
		float p = used / total;
		p *= 100;
		
		// Reset
		y = 20;
		y2 = 20;
		
		draw(batch, String.format("%.2f", used) + "/" + String.format("%.2f", total) + "GB, " + (int)p + "%", Color.WHITE);
		draw(batch, GameTime.makeString(TimeStyle.FULL), Color.GREEN);
		draw(batch, FOE.engine.getWorld() != null ? FOE.engine.getWorld().getBodyCount() + " physics bodies, " + FOE.engine.getWorld().getContactCount() + " contacts." : "No physics world!", Color.GREEN);
		draw(batch, FOE.engine.getAllEntities().size() + " active entities.", Color.YELLOW);
		draw(batch, FOE.map.getRenderCalls() + " / " + FOE.map.getRenderLoops() + " Calls/Loops " + String.format("(%.1f", (float)FOE.map.getRenderCalls()/FOE.map.getRenderLoops() * 100f) + "%)", Color.YELLOW);
		draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS (" + (int)(1f / Gdx.graphics.getDeltaTime()) + ")", Color.WHITE);
		draw(batch, Allocator.getRunningTimers() + " running timers.", Color.WHITE);
		draw(batch, Lan.getLangCount() + " languages loaded and ready to hot-swap.", Color.ORANGE);		
		draw(batch, "Mouse pos: " + (int)Input.getMouseWorldPos().x + ", " + (int)Input.getMouseWorldPos().y, Color.WHITE);
		
		drawRight(batch, "Time - " + String.format("%.3f", GameTime.getTime()), Color.WHITE);
		ArrayList<Entity> entities = FOE.engine.getSplitter().getInPoint(Input.getMouseWorldPos());
		drawRight(batch, "Entities in mouse region : " + (entities == null ? "NOT IN REGION" : entities.size()), Color.ORANGE);
		drawRight(batch, "Splitter region size : " + FOE.engine.getSplitter().getRegionSize(), Color.ORANGE);
		
		drawTimers(batch);
		
	}
	
	public void render(float delta, Batch batch){
		
		if(!active)
			return;
		
		renderRegions(batch);
	}
	
	private void renderRegions(Batch batch){
		if(shapes == null){
			shapes = new ShapeRenderer();
		}
		
		batch.end();
		
		Color a = Color.ORANGE;
		Color b = Color.PURPLE;
		int i = 0;
		
		shapes.setProjectionMatrix(batch.getProjectionMatrix());
		shapes.begin(ShapeType.Line);
		
		float size = FOE.engine.getSplitter().getRegionSize();
		for(int x = 0; x < FOE.engine.getSplitter().getWidth(); x++){
			shapes.setColor((i++ % 2) == 0 ? a : b);
			shapes.line(x * size, 0, x * size, FOE.map.getHeight());
		}
		for(int y = 0; y < FOE.engine.getSplitter().getHeight(); y++){
			shapes.setColor((i++ % 2) == 0 ? a : b);
			shapes.line(0, y * size, FOE.map.getWidth(), y * size);
		}
		
		shapes.end();
		
		batch.begin();
	}
	
	private void drawTimers(Batch batch){
		
		batch.end();
		shapes.setProjectionMatrix(batch.getProjectionMatrix());
		shapes.begin(ShapeType.Filled);		
		
		TimersRenderOutput out = Timers.getOutput();
		int parts = out.percentages.length;
		float currentAngle = 0;
		
		
		final float degInCircle = 360f;
		final float totalSegments = 360f;
		final float x = 120;
		final float y = Gdx.graphics.getHeight() - 120;
		final float totalRadius = 100f;
		
		shapes.setColor(Color.BLACK);
		shapes.circle(x, y, totalRadius * 1.1f, (int)(totalSegments * 1.1f));
		
		for(int i = 0; i < parts; i++){
			
			float degrees = degInCircle * out.percentages[i] + 2f;
			int segments = (int)(totalSegments * out.percentages[i] * out.radi[i]);
			if(segments <= 0)
				segments = 1;
			float radius = out.radi[i] * totalRadius;
			
			shapes.setColor(out.colours[i]);
			shapes.arc(x, y, radius, currentAngle - 1, degrees, segments);
			
			currentAngle += degrees;
		}
		
		shapes.end();
		batch.begin();
//		t,
//		er,
//		l,
//		rUI,
//		e,
//		p,
//		o
		String[] names = new String[]{
			"Render - Tiles",
			"Render - Entities",
			"Render - Light",
			"Render - UI",
			"Update - Entities",
			"Update - Physics",
			"Other"
		};
		
		int i = 0;
		for(String s : names){
			
			font.setColor(out.colours[i]);
			font.draw(batch, s, 10, (y - totalRadius * 1.3f) - (i * 20f));
			
			i++;
		}
		
	}
	
	private void drawRight(Batch batch, String text, Color color){
		font.setColor(color);
		font.draw(batch, text, Gdx.graphics.getWidth() - 10, y2, 0, text.length(), 0, 0, false);
		font.setColor(Color.WHITE);
		y2 += 23;
	}
	
	private void draw(Batch batch, String text, Color color){
		font.setColor(color);
		font.draw(batch, text, 10, y);
		font.setColor(Color.WHITE);
		y += 23;
	}

}
