package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Main;

public class DebugHook extends ScreenHook {

	public static BitmapFont font = new BitmapFont();
	public boolean active = false;;
	
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
		
		font.draw(batch, "" + String.format("%.2f", used) + "/" + String.format("%.2f", total) + "GB, " + (int)p + "%", 20, 20);
	}

}
