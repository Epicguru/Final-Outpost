package co.uk.epicguru.physics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The class that manages, controls and responds to physics events within the game world.
 * @author James Billy
 *
 */
public class JPhysicsBody {

	private Rectangle rect;
	
	/**
	 * Creates a new physics body given a position and a texture. The size of this body will be set to
	 * the width and height of the texture divided by the PPM value seen in {@link JPhysics.getPPM()}.
	 * @param x The X position to create the physics body at. This should be in world values, in other words scaled to whatever the PPM is.
	 * @param y The Y position to create the physics body at. This should be in world values, in other words scaled to whatever the PPM is.
	 * @param texture The texture region to base the width and the height of the body on. Cannot be null.
	 */
	public JPhysicsBody(float x, float y, TextureRegion texture){
		this(x, y, texture, true);
	}
	
	/**
	 * Creates a new physics body given the position, texture and scaling option.
	 * If scale is true, the width and height of this body will be equal to the size of the texture scaled by {@link JPhysics.getPPM()}.
	 * If false, the size of this body will be exactly equal to the size of the texture region.
	 * @param x The X position to create the physics body at. This should be in world values, in other words scaled to whatever the PPM is.
	 * @param y The Y position to create the physics body at. This should be in world values, in other words scaled to whatever the PPM is.
	 * @param texture The texture region to base the body on. Cannot be null.
	 * @param scale Whether to scale the size of the texture by PPM or not.
	 */
	public JPhysicsBody(float x, float y, TextureRegion texture, boolean scale){
		if(texture == null){
			throw new IllegalArgumentException("The texture passed to this constructor cannot be null!");
		}
		
		float w, h;
		w = scale ? texture.getRegionWidth() / JPhysics.getPPM() : texture.getRegionWidth();
		h = scale ? texture.getRegionHeight() / JPhysics.getPPM() : texture.getRegionHeight();
		
		this.rect = new Rectangle(x, y, w, h);
	}
	
	/**
	 * Creates a physics body based on a rectangle. All dimensions of the rectangle should be scaled in world units.
	 * See {@link JPhysics.getPPM()} for more info.
	 * @param rectangle The rectangle to create a body based off. Cannot be null.
	 */
	public JPhysicsBody(Rectangle rectangle){
		if(rectangle == null){
			throw new IllegalArgumentException("The rectangle passed to this constructor cannot be null!");
		}
		
		this.rect = new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	/**
	 * Constructs a new physics body. The position and the size are passed as floats.
	 * Note that all values passed must be scaled in world units. See {@link JPhysics.getPPM()} fore more info.
	 * @param x The X position.
	 * @param y The Y position.
	 * @param width The width of the body.
	 * @param height The height of the body.
	 */
	public JPhysicsBody(float x, float y, float width, float height){
		this.rect = new Rectangle(x, y, width, height);
	}
	
	public Rectangle getBounds(){
		return rect;
	}
	
	public boolean overlaps(JPhysicsBody body){
		
		if(body == null){
			return false;
		}
		if(body.getBounds() == null){
			return false;
		}
		
		return this.getBounds().overlaps(body.getBounds());
	}
	
	public JPhysicsBody fitInside(JPhysicsBody body){
		if(body == null){
			return this;
		}
		if(body.getBounds() == null){
			return this;
		}
		
		this.rect.fitInside(body.getBounds());
		return this;
	}
	
	public JPhysicsBody fitOutside(JPhysicsBody body){
		if(body == null){
			return this;
		}
		if(body.getBounds() == null){
			return this;
		}
		
		this.rect.fitOutside(body.getBounds());
		return this;
	}
	
	public float getX(){
		return rect.getX();
	}
	
	public float getY(){
		return rect.getY();
	}
	
	public float getWidth(){
		return rect.getWidth();
	}
	
	public float getHeight(){
		return rect.getHeight();
	}
	
	public JPhysicsBody setX(float x){
		this.rect.setX(x);
		return this;
	}
	
	public JPhysicsBody setY(float y){
		this.rect.setY(y);
		return this;
	}
	
	public JPhysicsBody setWidth(float width){
		this.rect.setWidth(width);
		return this;
	}
	
	public JPhysicsBody setHeight(float height){
		this.rect.setHeight(height);
		return this;
	}
	
	public JPhysicsBody scale(float scale){
		return scale(scale, scale);
	}
	
	public JPhysicsBody scale(float x, float y){
		this.rect.setSize(this.getWidth() * x, this.getHeight() * y);
		return this;
	}
	
	public JPhysicsBody setPosition(Vector2 position){		
		if(position == null){
			throw new IllegalArgumentException("Position cannot be set to a null value!");
		}
		
		return this.setPosition(position.x, position.y);
	}
	
	public JPhysicsBody setPosition(float x, float y){
		this.rect.setPosition(x, y);
		return this;
	}
	
	public JPhysicsBody setAsBox(float size){
		this.rect.setSize(size);
		return this;
	}
	
	public JPhysicsBody setSize(Vector2 size){
		if(size == null){
			throw new IllegalArgumentException("Size cannot be set to a null value!");
		}
		
		return this.setSize(size.x, size.y);
	}
	
	public JPhysicsBody setSize(float width, float height){
		this.rect.setSize(width, height);
		return this;
	}
	
	public JPhysicsBody centerOn(Vector2 position){
		if(position == null){
			throw new IllegalArgumentException("Cannot center on a null value!");
		}
		
		return centerOn(position.x, position.y);
	}
	
	public JPhysicsBody centerOn(float x, float y){
		this.rect.setCenter(x, y);
		return this;
	}
}
