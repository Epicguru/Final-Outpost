package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.Gdx;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Main;

public class InputHook extends ScreenHook {

	public void update(float delta){
		if(Main.INSTANCE.isInputJustDown(Main.VSYNC)){
			Gdx.graphics.setVSync(Gdx.graphics.getFramesPerSecond() > 80 ? false : true); // No getVsync??
		}
	}
	
}
