package co.uk.epicguru.physics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
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
	private boolean statik; // Nice spelling
	
	private float dragTimer = 0f;

	/**
	 * Sets the colour to render shapes with. Has no effect if using default {@link JPhysics} render method.
	 * @param colour The new colour, not null please.
	 */
	public static void setShapeColour(Color colour){
		renderer.setColor(colour);
	}
	
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
		
		if(isStatic()) // Disabled
			return this;
		
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

	/**
	 * Gets a general number for velocity. This can be used as a gauge for speed.
	 */
	public float getGeneralVelocity(){
		return this.getVelocity().len();
	}
	
	/**
	 * Gets the surface area of this body.
	 */
	public float getArea(){
		return this.rect.area();
	}
	
	/**
	 * Gets the density of this body. This is how much mass is within one unit as got from {@link #getArea()}.
	 * Used to calculate mass in {@link #getMass()}.
	 * @return
	 */
	public float getDensity(){
		return this.density;
	}
	
	/**
	 * Sets the density, which is the mass per unit of {@link #getArea()}.
	 * @param density The density. It should be in the range 1 - 10, but can be any value. The default value is 1.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setDensity(float density){
		
		if(density < 0)
			density = 0;
		
		this.density = density;
		return this;
	}
	
	/**
	 * Gets the bounciness of this body.
	 */
	public float getBounciness(){
		return this.bounciness;
	}
	
	/**
	 * Sets the bounciness of this body, this is a multiplier and therefore should be between 0 and 1.
	 * Zero means no bouncing, 0.5 means bouncing half of the initial velocity and 1 means bouncing with as much force
	 * as the initial velocity. 
	 * @return This object for chaining.
	 */
	public JPhysicsBody setBounciness(float bounciness){		
		this.bounciness = bounciness;
		return this;
	}
	
	/**
	 * Gets the mass of this body. This is calculated using {@link #getArea()} and {@link #getDensity()}.
	 * @return The mass of this body. Value depends on units used.
	 */
	public float getMass(){
		return this.getDensity() * this.getArea();
	}
	
	/**
	 * Called when this body enters collision with another. 
	 * This will be called after the collision has been resolved using {@link #handleCollision(JPhysicsBody, boolean)}.
	 * @param data Some info about the collision.
	 */
	public void collisionEvent(JCollisionData data){
		// Override for handling
	}

	/**
	 * Sets the drag multipliers. These values should be between 0 and 1.
	 * A value of 0.5 means that every X times per second the velocity of this object will be halved.
	 * X is equal to the value returned by {@link JPhysics.getDragsPerSecond()}. By default, this value will be whatever
	 * {@link JPhysics.getDefaultDrag()} returns at the time of creation of this body.
	 * @return This object for chaining.
	 */
	public JPhysicsBody setDrag(float xScale, float yScale){
		
		this.drag.set(xScale, yScale);
		return this;
	}

	/**
	 * Applies drag and moves this object by its current velocity.
	 * @param delta The time, in seconds, between calls to this method.
	 * @see 
	 * <li>{@link #getVelocity()} for velocity.
	 * <li>{@link #getGravityScale()} for the gravity scale.
	 * <li>{@link JPhysics.getGravity()} for the world gravity.
	 */
	protected void updateVelocity(float delta){
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

	/**
	 * Called when another body enters collision with this object. This method is expected to resolve and update anything
	 * necessary for a smooth and responsive physics simulation. 
	 * @param other The other body that we have entered collision with.
	 * @param original True if this is a call from the main {@link #updatePhysics(float)} method. False if
	 * this is a call from another body to resolve collision. Normally if this is true you are moving faster than the other object
	 * and you have priority.
	 */
	protected void handleCollision(JPhysicsBody other, boolean original){
		
		// If static ignore completely
		if(this.isStatic())
			return;
		
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

	/**
	 * Updates physics by responding to all collisions encountered.
	 * The order of execution if this method depends on the velocity of 
	 * the bodies, if highest first and if slowest, last.
	 * @param delta The time, in seconds, between calls to this method.
	 * @see 
	 * <li>{@link #render(OrthographicCamera)} for a method to render this object to the screen, debug style.
	 * <li>{@link #compareTo(JPhysicsBody)} on how execution is timed.
	 * <li>{@link #getGeneralVelocity()} for a measure of general velocity.
	 * <li>{@link #update(float)} for a method to update other aspects of the body.
	 * <li>{@link #handleCollision(JPhysicsBody, boolean)} to see how collision is handled, or add a custom implementation.
	 */
	protected void updatePhysics(float delta){
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

	/**
	 * Updates movement and applies gravity for this body. Does not resolve collisions.
	 * @param delta The time, in seconds, between calls to this method.
	 */
	public void update(float delta){

		// Apply gravity
		applyForce(JPhysics.getGravity().x * this.getGravityScale() * delta, JPhysics.getGravity().y * this.getGravityScale() * delta, ForceMode.FORCE);

		// Apply
		if(!isStatic())
			updateVelocity(delta);	
	}

	/**
	 * Renders a debug style view of this body. You should end any running batches before calling this.
	 * @param camera The camera to project the image with. This will affect how the scale and the image compare.
	 * @see {@link JPhysics.getPPM()} for more info on scaling and cameras.
	 */
	public void render(OrthographicCamera camera){	

		renderer.setAutoShapeType(true);
		renderer.setProjectionMatrix(camera.combined);
		if(!renderer.isDrawing())
			renderer.begin();

		renderer.box(this.getX(), this.getY(), 0, this.getWidth(), this.getHeight(), 0);


	}

	/**
	 * Sets the static mode of this body. If a body is static then is cannot be moved by collisions with other objects,
	 * and applying forces to it is disabled. Otherwise, if it is not static, it can mode and collide with other bodies normally.
	 * Note that it is possible to manually update velocity on this object, but it is not recommended.
	 * @param flag The new static mode, true of false.
	 */
	public void setStatic(boolean flag){
		this.statik = flag;
	}
	
	/**
	 * Checks the static state of this body. See {@link #setStatic(boolean)} for more info.
	 * @return True if static, false if not.
	 */
	public boolean isStatic(){
		return this.statik;
	}

	/**
	 * Used to sort bodies ready for collision. By default uses {@link #getGeneralVelocity()} to calculate priority.
	 */
	public int compareTo(JPhysicsBody other) {
		
		boolean larger = this.getGeneralVelocity() > other.getGeneralVelocity();
		
		return larger ? -1 : 1;
	}
}
