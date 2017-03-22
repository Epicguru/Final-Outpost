package co.uk.epicguru.physics;

import com.badlogic.gdx.math.Rectangle;

public class JPhysicsBody {

	private Rectangle rect;
	
	public JPhysicsBody(Rectangle rectangle){
		this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}
	
	public JPhysicsBody(float x, float y, float width, float height){
		this.rect = new Rectangle(x, y, width, height);
	}
	
}
