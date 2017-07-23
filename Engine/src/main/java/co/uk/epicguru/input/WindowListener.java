package co.uk.epicguru.input;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public class WindowListener implements Lwjgl3WindowListener {

	private static final String TAG = "Window Listener";
	
	@Override
	public void iconified(boolean isIconified) {

	}

	@Override
	public void maximized(boolean isMaximized) {

	}

	@Override
	public void focusLost() {

	}

	@Override
	public void focusGained() {

	}

	@Override
	public boolean closeRequested() {
		
		if(!FOE.loaded){
			Log.error(TAG, "Cannot close window while loading!");
			return false;
		}
		
		return true;
	}

	@Override
	public void filesDropped(String[] files) {
		
		Log.info(TAG, "Files dropped: ");
		for(String s : files){
			Log.info("   ", s);
		}
		
	}

	@Override
	public void refreshRequested() {

	}
}
