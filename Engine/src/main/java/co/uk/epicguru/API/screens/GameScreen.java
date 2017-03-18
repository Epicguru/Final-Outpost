package co.uk.epicguru.API.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.main.FOE;

/**
 * The GameScreen class extends Base (and therefore is a plugin extension point).
 * A screen is set using FOE.setScreen(new MyScreen()).
 * @author James Billy
 */
public class GameScreen extends Base implements Screen {

	private ArrayList<ScreenHook> hooks = new ArrayList<>();
	private ArrayList<ScreenHook> hooksBin = new ArrayList<>();
	private static Rectangle rect1 = new Rectangle();
	private static Rectangle rect2 = new Rectangle();
	
	/**
	 * Adds a screen hook to this screen instance.
	 */
	public void addHook(ScreenHook hook){
		if(!hooks.contains(hook)){
			hooks.add(hook);
		}
	}
	
	/**
	 * Adds an existing screen hook to this screen instance.
	 */
	public void removeHook(ScreenHook hook){
		hooksBin.add(hook);
	}
	
	/**
	 * Removes all screen hooks from this screen instance.
	 */
	public void clearHooks(){
		hooks.clear();
	}
	
	private void clearBin(){
		for(ScreenHook hook : hooksBin){
			hooks.remove(hook);
		}
		hooksBin.clear();
	}
	
	/**
	 * Gets all screen hooks that are in this screen instance.
	 */
	public ArrayList<ScreenHook> getHooks(){
		return hooks;
	}
	
	@Override
	public void show() {
		for(ScreenHook hook : hooks){
			hook.show();
		}
	}

	/**
	 * Calls {@link #render(float, Batch)}. Override is not recomended.
	 */
	@Override
	public void render(float delta) {
		render(delta, FOE.batch);
		
		clearBin();
	}
	
	/**
	 * Called 60 times a second when this screen is active.
	 * By default this calls hook methods.
	 * By default this calls hook methods.
	 * @param delta The time in seconds between this frame and the previous one.
	 */
	public void update(float delta){
		for(ScreenHook hook : hooks){
			hook.update(delta);
		}
	}
	
	/**
	 * Called 60 times a second when active, after {@link #update(float)}.
	 * Use this to render objects, because one unit here is equal to one meter (see Constants.PPM).
	 * By default this calls hook methods.
	 * @param delta The time in seconds between this frame and the previous one.
	 * @param batch The SpriteBatch to use when rendering.
	 */
	public void render(float delta, Batch batch){
		for(ScreenHook hook : hooks){
			hook.render(delta, batch);
		}
	}

	/**
	 * Called 60 times a second when active, after {@link #render(float, Batch)}.
	 * Use this to render UI elements as there is not scaling.
	 * By default this calls hook methods.
	 * @param delta The time in seconds between this frame and the previous one.
	 * @param batch The SpriteBatch to use when rendering.
	 */
	public void renderUI(float delta, Batch batch){
		for(ScreenHook hook : hooks){
			hook.renderUI(delta, batch);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		for(ScreenHook hook : hooks){
			hook.resize(width, height);
		}
	}

	@Override
	public void pause() {
		for(ScreenHook hook : hooks){
			hook.pause();
		}
	}

	@Override
	public void resume() {
		for(ScreenHook hook : hooks){
			hook.resume();
		}
	}

	@Override
	public void hide() {
		for(ScreenHook hook : hooks){
			hook.hide();
		}
	}

	@Override
	public void dispose() {
		for(ScreenHook hook : hooks){
			hook.dispose();
		}
	}

	/**
	 * Renders a texture region to a specific size, using a best-fit method where the aspect ratio of the image is not distorted. 
	 * @param batch The batch to render with.
	 * @param texture The texture to render.
	 * @param position The centre of the rendering.
	 * @param size The MINIMUM size of the rendered image.
	 * @see {@link #renderToSize(Batch, TextureRegion, float, float, float, float)}
	 */
	public void renderToSize(Batch batch, TextureRegion texture, Vector2 position, Vector2 size){
		renderToSize(batch, texture, position.x, position.y, size.x, size.y);
	}
	
	/**
	 * Renders a texture to a specific size, using a best-fit method where the aspect ratio of the image is not distorted. 
	 * @param batch The batch to render with.
	 * @param texture The texture to render.
	 * @param position The centre of the rendering.
	 * @param size The MINIMUM size of the rendered image.
	 * @see {@link #renderToSize(Batch, Texture, float, float, float, float)}
	 */
	public void renderToSize(Batch batch, Texture texture, Vector2 position, Vector2 size){
		renderToSize(batch, texture, position.x, position.y, size.x, size.y);
	}
	
	/**
	 * Renders a texture region to a specific size, using a best-fit method where the aspect ratio of the image is not distorted. 
	 * @param batch The batch to render with.
	 * @param texture The texture to render.
	 * @param x The x position, centred (centre point of rendering).
	 * @param y The y position, centred (centre point of rendering).
	 * @param width The MINIMUM width of the texture, it may be larger but it will never be smaller. The image will not be distorted.
	 * @param height The MINIMUM height of the texture, it may be larger but it will never be smaller. The image will not be distorted.
	 */
	public void renderToSize(Batch batch, TextureRegion texture, float x, float y, float width, float height){
		float tw = texture.getRegionWidth();
		float th = texture.getRegionHeight();
		
		rect1.set(x - (tw / 2f), y - (th / 2f), tw, th);
		rect2.set(x - (width / 2f), y - (height / 2f), width, height);
		
		rect1.fitOutside(rect2);
		
		batch.draw(texture, rect1.x, rect1.y, rect1.width, rect1.height);
	}
	
	/**
	 * Renders a texture to a specific size, using a best-fit method where the aspect ratio of the image is not distorted. 
	 * @param batch The batch to render with.
	 * @param texture The texture to render.
	 * @param x The x position, centred (centre point of rendering).
	 * @param y The y position, centred (centre point of rendering).
	 * @param width The MINIMUM width of the texture, it may be larger but it will never be smaller. The image will not be distorted.
	 * @param height The MINIMUM height of the texture, it may be larger but it will never be smaller. The image will not be distorted.
	 */
	public void renderToSize(Batch batch, Texture texture, float x, float y, float width, float height){
		float tw = texture.getWidth();
		float th = texture.getHeight();
		
		rect1.set(x - (tw / 2f), y - (th / 2f), tw, th);
		rect2.set(x - (width / 2f), y - (height / 2f), width, height);
		
		rect1.fitOutside(rect2);
		
		batch.draw(texture, rect1.x, rect1.y, rect1.width, rect1.height);
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
