package co.uk.epicguru.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Image extends UI {

	private TextureRegion texture;
	
	public Image(Texture texture){
		this.setTexture(texture == null ? null : new TextureRegion(texture));
	}
	
	public Image(TextureRegion texture){
		this.setTexture(texture);
	}
	
	public void setTexture(TextureRegion texture){
		this.texture = texture;
	}
	
	public TextureRegion getTexture(){
		return this.texture;
	}

	public void render(Observer obs, float delta) {
		if(this.getTexture() == null)
			return;
		
		Batch batch = obs.getBatch();
		TextureRegion texture = this.getTexture();
		
		float x = bounds.x;
		float y = bounds.y;
		
		float width = bounds.width;
		float height = bounds.height;
		width *= super.localScale;
		width *= obs.getScale();
		height *= super.localScale;
		height *= obs.getScale();
		
		Color old = batch.getColor();
		batch.setColor(UI.mul(super.colour, obs.getColour()));
		batch.draw(texture, x, y, width, height);
		batch.setColor(old);
	}
}