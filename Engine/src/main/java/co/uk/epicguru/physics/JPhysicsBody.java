package co.uk.epicguru.physics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The class that manages, controls and responds to physics events within the game world.
 * @author James Billy
 *
 */
public class JPhysicsBody implements Comparable<JPhysicsBody>{

	private static ShapeRenderer renderer = new ShapeRenderer();
	
	private Rectangle rect;
	private Vector2 velocity = new Vector2(0, 0);
	private Vector2 drag = JPhysics.getDefaultDrag();
	private float gravityScale = 1f;
	private float density = 1;
	private float bounciness = 0.65f; // Yes, this is the correct spelling :D
	
	private float dragTimer = 0f;

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
		addToWorld();
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
		addToWorld();
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
		addToWorld();
	}

	private void addToWorld(){
		JPhysics.addBody(this);
	}

	/**
	 * Removes this body from the world. It is simply a call to {@link JPhysics.removeBody(this)}.
	 */
	public void destroy(){
		JPhysics.removeBody(this);
	}

	/**
	 * Gets the underlying rectangle that powers this physics body.
	 * This has many useful methods, such as area, aspect ration and collision detection.
	 */
	public Rectangle getBounds(){
		return rect;
	}

	/**
	 * Checks to see if this physics body is touching (inside) another one.
	 * @param body The other body.
	 * @return True if overlapping. False if not overlapping or if body or the value of {@link body.getBounds()} returns null.
	 */
	public boolean overlaps(JPhysicsBody body){

		if(body == null){
			return false;
		}
		if(body.getBounds() == null){
			return false;
		}

		return this.getBounds().overlaps(body.getBounds());
	}

	/**
	 * Resizes this body in order to fit inside the other body.
	 * @param body The other body.
	 * @return This object for chaining.
	 */
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

	/**
	 * Resizes this body in order to fit inside the other body.
	 * @param body The other body.
	 * @return This object for chaining.
	 */
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

	/**
	 * Gets the x position of this object. The position starts at the bottom left of bodies.
	 */
	public float getX(){
		return rect.getX();
	}

	/**
	 * Gets the y position of this object. The position starts at the bottom left of bodies.
	 */
	public float getY(){
		return rect.getY();
	}

	/**
	 * Gets the width of this object.
	 */
	public float getWidth(){
		return rect.getWidth();
	}

	/**
	 * Gets the height of this object.
	 */
	public float getHeight(){
		return rect.getHeight();
	}

	/**
	 * Sets the x position of this object.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setX(float x){
		this.rect.setX(x);
		return this;
	}

	/**
	 * Sets the y position of this object.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setY(float y){
		this.rect.setY(y);
		return this;
	}

	/**
	 * Sets the width of this object.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setWidth(float width){
		this.rect.setWidth(width);
		return this;
	}

	/**
	 * Sets the height of this object.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setHeight(float height){
		this.rect.setHeight(height);
		return this;
	}

	/**
	 * Scales the width and height of this object by the parameter passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody scale(float scale){
		return scale(scale, scale);
	}

	/**
	 * Scales the width and height by the x and y parameters, separately.
	 * @return This object for chaining.
	 */
	public JPhysicsBody scale(float x, float y){
		this.rect.setSize(this.getWidth() * x, this.getHeight() * y);
		return this;
	}

	/**
	 * Scales the width and height by the x and y fields of the vector passed, separately.
	 * @return This object for chaining.
	 */
	public JPhysicsBody scale(Vector2 scale){

		if(scale == null)
			return this;

		return this.scale(scale.x, scale.y);
	}

	/**
	 * Offsets the position of this body by the vector passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody translate(Vector2 offset){
		if(offset == null)
			return this;

		return this.translate(offset.x, offset.y);
	}

	/**
	 * Offsets the position of this body by the vector passed, multiplied by the scale value.
	 * @return This object for chaining.
	 */
	public JPhysicsBody translate(Vector2 offset, float scale){
		if(offset == null)
			return this;

		return this.translate(offset.x * scale, offset.y * scale);
	}

	/**
	 * Offsets the position of this body by the values passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody translate(float x, float y){
		return this.setPosition(this.getX() + x, this.getY() + y);
	}

	/**
	 * Sets the position of this body to the vector passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setPosition(Vector2 position){		
		if(position == null){
			throw new IllegalArgumentException("Position cannot be set to a null value!");
		}

		return this.setPosition(position.x, position.y);
	}

	/**
	 * Sets the position of this body to the x and y coordinates.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setPosition(float x, float y){
		this.rect.setPosition(x, y);
		return this;
	}

	/**
	 * Sets the width and height of this body to the size. This means that the body will have a box shape.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setAsBox(float size){
		this.rect.setSize(size);
		return this;
	}

	/**
	 * Sets the width and height to the x and y fields of the vector passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setSize(Vector2 size){
		if(size == null){
			throw new IllegalArgumentException("Size cannot be set to a null value!");
		}

		return this.setSize(size.x, size.y);
	}

	/**
	 * Sets the width and height to the x and y fields of the vector passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setSize(float width, float height){
		this.rect.setSize(width, height);
		return this;
	}

	/**
	 * Moves this body so that the centre of this body is at the coordinate passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody centerOn(Vector2 position){
		if(position == null){
			throw new IllegalArgumentException("Cannot center on a null value!");
		}

		return centerOn(position.x, position.y);
	}

	/**
	 * Moves this body so that the centre of this body is at the coordinates passed.
	 * @return This object for chaining.
	 */
	public JPhysicsBody centerOn(float x, float y){
		this.rect.setCenter(x, y);
		return this;
	}

	/**
	 * Gets the linear velocity, this is the speed at which this body is moving over a second.
	 * So if the velocity is (5, 0) then this body is moving at 5 units per second.
	 */
	public Vector2 getVelocity(){
		return this.velocity;
	}

	/**
	 * Applies a force to this body. The result of this operation depends on the force mode.
	 */
	public JPhysicsBody applyForce(Vector2 force, ForceMode mode){
		if(force == null)
			return this;

		this.applyForce(force.x, force.y, mode);

		return this;
	}

	/**
	 * Applies a force to this body. The result of this operation depends on the force mode.
	 */
	public JPhysicsBody applyForce(float x, float y, ForceMode mode){
		switch(mode){
		case FORCE:
			this.velocity.add(x, y);
			break;

		case IMPULSE:
			this.velocity.set(x, y);
			break;

		}

		return this;
	}

	/**
	 * Sets the scale at which gravity is applied to this body.
	 * The value of gravity is retrieved from {@link JPhysics.getGravity()}.
	 * @param scale The scale, where 0.5 means that gravity affects this body 2x less than other bodies.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setGravityScale(float scale){
		this.gravityScale = scale;
		return this;
	}

	/**
	 * Gets the scale at which gravity is applied to this body. By default this is one.
	 */
	public float getGravityScale(){
		return this.gravityScale;
	}

	/**
	 * Gets the linear drag for this object. This value is a multiplier that is applied every update.
	 * @return
	 */
	public Vector2 getDrag(){
		return this.drag;
	}

	public float getGeneralVelocity(){
		return this.getVelocity().len();
	}
	
	public float getArea(){
		return this.rect.area();
	}
	
	public float getDensity(){
		return this.density;
	}
	
	public JPhysicsBody setDensity(float density){
		this.density = density;
		return this;
	}
	
	public float getBounciness(){
		return this.bounciness;
	}
	
	public JPhysicsBody setBounciness(float bounciness){
		this.bounciness = bounciness;
		return this;
	}
	
	public float getMass(){
		return this.getDensity() * this.getArea();
	}
	
	public void collisionEvent(JCollisionData data){
		// Override for handling
	}

	public JPhysicsBody setDrag(float xScale, float yScale){
		this.drag.set(xScale, yScale);
		return this;
	}

	private void updateVelocity(float delta){
		// Translate by velocity
		this.translate(this.getVelocity(), delta);

		// Reduce velocity by drag.
		this.dragTimer += delta;
		float time = 1f / JPhysics.getDragsPerSecond();
		while(this.dragTimer >= time){
			this.velocity.scl(this.getDrag());
			this.dragTimer -= time;
		}

	}

	private void handleCollision(JPhysicsBody other, boolean original){
		// Find shortest path out of the other body : then apply velocity

		float left, right, down, up;

		left = Math.abs((this.getX() + this.getWidth()) - other.getX());
		right = Math.abs((other.getX() + other.getWidth()) - this.getX());

		down = Math.abs((this.getY() + this.getHeight()) - other.getY());
		up = Math.abs((other.getY() + other.getHeight()) - this.getY());

		float changes[] = {left, right, down, up};
		float smallest = Float.MAX_VALUE;
		int index = -1;

		for(int i = 0; i < changes.length; i++){
			if(changes[i] < smallest){
				index = i;
				smallest = changes[i];
			}
		}

		if(index == -1)
			return; // Ummm. ? 

		// 0 LEFT
		// 1 RIGHT
		// 2 DOWN
		// 3 UP

		float bounce = this.getBounciness();
		
		float thisMass = this.getMass();
		float otherMass = other.getMass();
		float dif = thisMass / otherMass;
		float influence = dif < 1 ? 1 : dif;

		float barge = -1 + ((influence / 100f) > 1f ? 1f : (influence / 100f)) * 2;
		
		switch(index){
		case 0:		
			// Tell other
			if(original){
				other.handleCollision(this, false);
			}

			// Move out
			this.setX(other.getX() - this.getWidth());

			float addX = !original ? other.getVelocity().x / influence : 0;
			float addY = !original ? other.getVelocity().y / influence : 0;
			
			// Bounce
			this.applyForce((this.getVelocity().x * barge * bounce) + addX, this.getVelocity().y + addY, ForceMode.IMPULSE);
			break;
		case 1:
			// Tell other
			if(original){
				other.handleCollision(this, false);
			}

			// Move out
			this.setX(other.getX() + other.getWidth());

			addX = !original ? other.getVelocity().x / influence : 0;
			addY = !original ? other.getVelocity().y / influence : 0;
			
			// Bounce
			this.applyForce((this.getVelocity().x * barge * bounce) + addX, this.getVelocity().y + addY, ForceMode.IMPULSE);
			break;
		case 2:
			// Tell other
			if(original){
				other.handleCollision(this, false);
			}

			// Move out
			this.setY(other.getY() - this.getHeight());

			addX = !original ? other.getVelocity().x / influence : 0;
			addY = !original ? other.getVelocity().y / influence : 0;
			
			// Bounce
			this.applyForce(this.getVelocity().x + addX, (this.getVelocity().y * barge * bounce) + addY, ForceMode.IMPULSE);
			break;
		case 3:
			// Tell other
			if(original){
				other.handleCollision(this, false);
			}

			// Move out
			this.setY(other.getY() + other.getHeight());

			addX = !original ? other.getVelocity().x / influence : 0;
			addY = !original ? other.getVelocity().y / influence : 0;
			
			// Bounce
			this.applyForce(this.getVelocity().x + addX, (this.getVelocity().y * barge * bounce) + addY, ForceMode.IMPULSE);
			break;
		}
	}

	public void updatePhysics(float delta){
		// 1. Check for collisions - get all - and store in arraylist;
		// 2. Respond to collisions by first moving out of other collider;
		// 3. Then apply (add) force opposite to the one that we were moving at, multiplied by a bounce value.

		ArrayList<JCollisionData> data = JPhysics.getAllCollisions(this);
		if(data.size() == 0)
			return;


		// Loop time
		for(JCollisionData c : data){		
			JPhysicsBody other = c.bodyB;

			if(this.overlaps(other)){
				this.handleCollision(other, true);
			}

			// Callback
			this.collisionEvent(c);
		}
	}

	public void update(float delta){

		// Apply gravity
		applyForce(JPhysics.getGravity().x * this.getGravityScale() * delta, JPhysics.getGravity().y * this.getGravityScale() * delta, ForceMode.FORCE);

		// Apply
		updateVelocity(delta);	
	}

	public void render(OrthographicCamera camera){	

		renderer.setAutoShapeType(true);
		renderer.setProjectionMatrix(camera.combined);
		if(!renderer.isDrawing())
			renderer.begin();

		renderer.box(this.getX(), this.getY(), 0, this.getWidth(), this.getHeight(), 0);


	}


	public int compareTo(JPhysicsBody other) {
		
		boolean larger = this.getGeneralVelocity() > other.getGeneralVelocity();
		
		return larger ? -1 : 1;
	}
}
