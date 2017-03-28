package co.uk.epicguru.entities;

import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.physics.JPhysicsBody;

public abstract class EntityBody extends Entity {

	private JPhysicsBody body;

	public EntityBody() {
		super();
		createBody(0, 0, 1, 1);
	}

	public EntityBody(float x, float y, String name) {
		super(x, y, name);
		createBody(x, y, 1, 1);
	}

	public EntityBody(String name) {
		super(name);
		createBody(0, 0, 1, 1);
	}

	public EntityBody(Vector2 position, String name) {
		super(position, name);
		createBody(position.x, position.y, 1, 1);
	}
	
	private void createBody(float x, float y, float width, float height){
		this.body = new JPhysicsBody(x, y, width, height);
	}
	
	/**
	 * Sets the size of this body.
	 * @param width The width of the body, in tiles.
	 * @param height The height of the body, in tiles.
	 * @see {@link #getBody()} for more body methods.
	 */
	public void setSize(float width, float height){
		this.body.setSize(width, height);
	}
	
	/**
	 * Gets the {@link JPhysicsBody} associated with this entity.
	 */
	public JPhysicsBody getBody(){
		return this.body;
	}


	/**
	 * Sets the position of this entity, but also the position of the body.
	 */
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.getBody().setPosition(x, y);
	}


	/**
	 * Gets the position of the body that this entity represents, but the bodies position and the entities
	 * position is always synchronised.
	 */
	public Vector2 getPosition() {
		return this.getBody().getPosition();
	}
}
