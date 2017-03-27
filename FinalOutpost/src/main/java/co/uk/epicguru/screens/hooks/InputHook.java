package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.Gdx;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.Main;

public class InputHook extends ScreenHook {
	
	public static boolean vSync;
	
	public void update(float delta){
		
		vSync = Gdx.graphics.getFramesPerSecond() > 70 ? false : true;
		
		if(Main.INSTANCE.isInputJustDown(Main.VSYNC)){
			Gdx.graphics.setVSync(!vSync); // No getVsync??
			Log.info("Input Hook", "Toggled VSync to " + !vSync);
			Main.graphics.set("VSync", !vSync);
		}
	}
	
}
