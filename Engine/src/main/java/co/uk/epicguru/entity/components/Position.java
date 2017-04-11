package co.uk.epicguru.entity.components;

import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.entity.Component;

public class Position extends Component {

	private float x;
	private float y;
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void set(float x, float y){
		this.setX(x);
		this.setY(y);
	}
	
	public void set(Position position){
		if(position == null)
			return;
		
		this.set(position.getX(), position.getY());
	}
	
	public void set(Vector2 position){
		if(position == null)
			return;
		
		this.set(position.x, position.y);
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public void addX(float offset){
		this.setX(this.getX() + offset);
	}
	
	public void addY(float offset){
		this.setY(this.getY() + offset);
	}
	
	public void add(float x, float y){
		this.addX(x);
		this.addY(y);
	}
	
	public void fromVector(Vector2 vector){
		this.setX(vector.x);
		this.setY(vector.y);
	}
	
	public Vector2 toVector(Vector2 vector){
		return vector.set(this.getX(), this.getY());
	}
	
	public String toString(){
		return new StringBuilder()
				.append("Position : (").append(String.format("%.2f", this.getX()))
				.append(", ")
				.append(String.format("%.2f", this.getY()))
				.append(')')
				.toString();
	}
}
