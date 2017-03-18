package co.uk.epicguru.API.screens.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.FOE;
import net.lingala.zip4j.core.ZipFile;

public class LoadingScreen extends GameScreen {

	public BitmapFont font = new BitmapFont();

	public void update(float delta){
		if(FOE.loaded)
			return;

		if(FOE.loadingText.equals("Loading plugin assets")){
			if(FOE.pluginsLoader.getCurrentZip() != null){
				ZipFile file = FOE.pluginsLoader.getCurrentZip();
				FOE.loadingSubText = "Plugin '" + file.getFile().getName().replace(".zip", "") + "'\n"
						+ U.getCurrentOperation(file) + " : " + file.getProgressMonitor().getPercentDone() + "%\n"
						+ U.nameFromPath(file.getProgressMonitor().getFileName());
			}
		}
	}

	public void renderUI(float delta, Batch batch){
		font.draw(batch, FOE.loadingText == null ? "" : FOE.loadingText, getScreenWidth() / 2f, getScreenHeight() / 2f, -1, 1, false);
		font.draw(batch, FOE.loadingSubText == null ? "" : FOE.loadingSubText, getScreenWidth() / 2f, getScreenHeight() / 2f - 20, -1, 1, false);
	}

}
