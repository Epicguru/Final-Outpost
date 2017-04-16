package co.uk.epicguru.entity.components;

import co.uk.epicguru.entity.Component;

public class Health extends Component {

	private float max;
	private float health;
	
	public Health(float start, float max){
		this.health = start;
		this.max = max;
	}
	
	public float getHealth(){
		return this.health;
	}
	
	public float getMaxHealth(){
		return this.max;
	}
	
	public float getMinHealth(){
		return 0f;
	}
	
	public void setHealth(float health){
		if(health > this.getMaxHealth()){
			health = this.getMaxHealth();
		}
		if(health < this.getMinHealth()){
			health = this.getMinHealth();
		}
		this.health = health;
	}
	
	public void changeHealth(float offset){
		this.setHealth(this.getHealth() + offset);
	}
	
	public boolean isDead(){
		return this.getHealth() <= this.getMinHealth();
	}
	
	public String toString(){
		return new StringBuilder()
				.append("Health : ")
				.append(String.format("%.2f", this.getHealth()))
				.append('/')
				.append(String.format("%.2f", this.getMaxHealth()))
				.toString();
	}
}
