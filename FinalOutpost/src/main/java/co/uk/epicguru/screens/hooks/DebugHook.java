package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.JPhysics;

public class DebugHook extends ScreenHook {

	public static BitmapFont font = new BitmapFont();
	public boolean active = false;;
	
	float y = 0;
	
	@Override
	public void renderUI(float delta, Batch batch) {
		
		if(Main.INSTANCE.isInputJustDown(Main.DEBUG)){
			this.active = !active;
		}
		
		if(!active)
			return;
		
		float total = (float) (Runtime.getRuntime().totalMemory() / Math.pow(1024, 3));
		float used = (float) (total - Runtime.getRuntime().freeMemory() / Math.pow(1024, 3));
		float p = used / total;
		p *= 100;	
		y = 20;
		
		draw(batch, String.format("%.2f", used) + "/" + String.format("%.2f", total) + "GB, " + (int)p + "%", Color.WHITE);
		draw(batch, JPhysics.getActiveBodies().size() + " bodies active. Gravity is " + JPhysics.getGravity().toString() + " and default drag is " + JPhysics.getDefaultDrag() + " @ " + JPhysics.getDragsPerSecond() + " DPS.", Color.WHITE);
		draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS (" + (int)(1f / Gdx.graphics.getDeltaTime()) + ")", Color.WHITE);
		draw(batch, Allocator.getRunningTimers() + " running timers.", Color.WHITE);
	}
	
	private void draw(Batch batch, String text, Color color){
		font.setColor(color);
		font.draw(batch, text, 10, y);
		font.setColor(Color.WHITE);
		y += 23;
	}

}
