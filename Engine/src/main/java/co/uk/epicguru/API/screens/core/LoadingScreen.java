package co.uk.epicguru.API.screens.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.FOE;
import net.lingala.zip4j.core.ZipFile;

public class LoadingScreen extends GameScreen {

	public BitmapFont font = new BitmapFont();
	private float timer;
	private int index;
	private final int size = 100;
	private StringBuilder str = new StringBuilder();

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
		
		this.updateTimer(delta);
	}

	private void updateTimer(float delta){
		
		timer += delta;
		float interval = 0.01f;
		while(timer >= interval){
			timer -= interval;
			
			index++;
			if(index >= size){
				index = 0;
			}
			
			this.updateStringBuidler();
		}
		
	}
	
	private void updateStringBuidler(){
		
		str.setLength(0);
		
		// -------<||||||||>------
		
		for(int i = 0; i < size; i++){
			int dst = Math.abs(index - i);
			
			if(i == 0){
				str.append('#');
			}else if(i == size - 1){
				str.append('#');
			}else if(dst <= 5){
				str.append('|');
			}else if(index - i == 6){
				str.append('<');
			}else if(index - i == -6){
				str.append('>');
			}else{
				str.append('-');
			}
		}
		
	}
	
	public void renderUI(float delta, Batch batch){
		font.draw(batch, FOE.loadingText == null ? "" : FOE.loadingText, getScreenWidth() / 2f, getScreenHeight() / 2f, -1, 1, false);
		font.draw(batch, FOE.loadingSubText == null ? "" : FOE.loadingSubText, getScreenWidth() / 2f, getScreenHeight() / 2f - 20, -1, 1, false);
		font.draw(batch, this.str.toString(), getScreenWidth() / 2f, getScreenHeight() / 2f - 40, -1, 1, false);
	}

}
