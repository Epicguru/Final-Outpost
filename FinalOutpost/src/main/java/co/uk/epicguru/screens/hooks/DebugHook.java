package co.uk.epicguru.screens.hooks;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import co.uk.epicguru.API.Allocator;
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
		drawRight(batch, "Within 2 chunks of origin : " + FOE.engine.getSplitter().getInRect(0, 0, 32, 32), Color.ORANGE);
		drawRight(batch, "Within 1.5 round chunks : " + FOE.engine.getSplitter().getInRange(32, 32, 19f), Color.ORANGE);
		
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
		
		shapes.setColor(0, 1, 0, 1);
		shapes.circle(32, 32, 19f, 100);
		
		shapes.setColor(0.9f, 0.9f, 0.9f, 1);
		shapes.rect(13, 13, 38, 38);
		
		shapes.end();
		
		batch.begin();
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
