package co.uk.epicguru.API.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.main.FOE;

/**
 * The GameScreen class extends Base (and therefore is a plugin extension point).
 * A screen is set using FOE.setScreen(new MyScreen()).
 * @author James Billy
 */
public class GameScreen extends Base implements Screen {

	@Override
	public void show() {
		
	}

	/**
	 * Calls {@link #render(float, Batch)}. Override is not recomended.
	 */
	@Override
	public void render(float delta) {
		render(delta, FOE.batch);
	}
	
	/**
	 * Called 60 times a second when this screen is active.
	 * @param delta The time in seconds between this frame and the previous one.
	 */
	public void update(float delta){}
	
	/**
	 * Called 60 times a second when active, after {@link #update(float)}.
	 * Use this to render objects.
	 * @param delta The time in seconds between this frame and the previous one.
	 * @param batch The SpriteBatch to use when rendering.
	 */
	public void render(float delta, Batch batch){}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}

	/**
	 * Gets the current width in pixels of the screen.
	 */
	public int getScreenWidth(){
		return Gdx.graphics.getBackBufferWidth();
	}
	
	/**
	 * Gets the current height in pixels of the screen.
	 */
	public int getScreenHeight(){
		return Gdx.graphics.getBackBufferHeight();
	}
}
