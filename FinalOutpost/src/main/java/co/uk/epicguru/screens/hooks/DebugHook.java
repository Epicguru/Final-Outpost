package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.API.time.GameTime;
import co.uk.epicguru.API.time.TimeStyle;
import co.uk.epicguru.languages.Lan;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class DebugHook extends ScreenHook {

	public static BitmapFont font = new BitmapFont();
	public static boolean active = false;;
	
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
		draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS (" + (int)(1f / Gdx.graphics.getDeltaTime()) + ")", Color.WHITE);
		draw(batch, Allocator.getRunningTimers() + " running timers.", Color.WHITE);
		draw(batch, Lan.getLangCount() + " languages loaded and ready to hot-swap.", Color.FIREBRICK);
		
		drawRight(batch, "Time - " + String.format("%.3f", GameTime.getTime()), Color.WHITE);
		
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
