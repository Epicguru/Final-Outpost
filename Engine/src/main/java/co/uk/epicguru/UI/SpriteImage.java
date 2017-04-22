package co.uk.epicguru.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteImage extends Image {

	private Sprite spr = new Sprite();
	private float rot;
	
	public SpriteImage(Texture texture) {
		super(texture);
	}

	public SpriteImage(TextureRegion texture){
		super(texture);
	}
	
	public float getRotation(){
		return this.rot;
	}
	
	public void setRotation(float rotation){
		this.rot = rotation;
	}
	
	public Sprite getSprite(){
		return this.spr;
	}
	
	public void render(Observer obs, float delta){	
		if(this.getTexture() == null)
			return;
		
		spr.setRegion(super.getTexture());
		spr.setRotation(this.getRotation());
		
		Batch batch = obs.getBatch();
		
		float x = bounds.x;
		float y = bounds.y;
		
		float width = bounds.width;
		float height = bounds.height;
		width *= super.localScale;
		width *= obs.getScale();
		height *= super.localScale;
		height *= obs.getScale();
		
		spr.setSize(width, height);
		spr.setPosition(x, y);
		
		spr.draw(batch);
	}
}
