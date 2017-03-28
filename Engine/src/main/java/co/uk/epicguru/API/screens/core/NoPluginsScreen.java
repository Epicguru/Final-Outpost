package co.uk.epicguru.API.screens.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.FOE;

public final class NoPluginsScreen extends GameScreen {

	private BitmapFont font = new BitmapFont();
	private float y = 0;
	private float timer = 0;
	public boolean noPlugins = true;
	
	public void renderUI(float delta, Batch batch){
		
		timer += delta;
		y = MathUtils.cosDeg(timer * 100f) * 50f;
		
		font.setColor(Color.WHITE);
		FOE.BG_Colour = Color.RED;
		if(noPlugins){
			font.draw(batch, "No plugins loaded!\nPlease install at least the Core plugin & restart.", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + y, 0, 1, false);					
		}else{
			font.draw(batch, "Could not find the Core plugin!\nPlease install the Core plugin & restart.", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + y, 0, 1, false);							
		}
	}
	
}
