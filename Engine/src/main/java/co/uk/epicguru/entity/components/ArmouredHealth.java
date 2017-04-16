package co.uk.epicguru.entity.components;

public class ArmouredHealth extends Health {

	private float armour;
	
	public ArmouredHealth(float start, float max, float armour) {
		super(start, max);
		this.setArmour(armour);
	}
	
	public void setArmour(float newArmour){
		if(newArmour <= 0){
			this.armour = 0;
		}else if(newArmour >= 1){
			this.armour = 1;
		}else{
			this.armour = newArmour;
		}
	}
	
	public void changeArmour(float change){
		this.setArmour(this.getArmour() + change);
	}
	
	public void changeArmourPercentage(float percentageChange){
		this.changeArmour(percentageChange / 100f);
	}
	
	public float getNewDamage(float damage){
		// 100 - (100 * 0.3) = 70
		// 100 - (100 * 0.7) = 30
		// 100 - (100 * 1.0) = 0
		return damage - (damage * this.getArmour());
	}
	
	public float getArmour(){
		return this.armour;
	}
	
	public float getPercentage(){
		return this.getArmour() * 100f;
	}
	
	public String getReadablePercentage(){
		return String.format("%.1f", this.getPercentage()) + '%';
	}
	
	/*
	 * OVERRIDE BELOW!
	 */
	
	/**
	 * If offset is negative, then the damage is reduced my the amount of armour present.
	 * If positive, it is considered healing and is not affected.
	 * All of this uses methods from this class and also the {@link Position} class, so overriding them will have an 
	 * effect on this.
	 * @see
	 * <li> {@link #getArmour()} for the percentage of armour, as a float from 0 - 1.
	 * <li> {@link #getPercentageReadable()} for the user friendly percentage of this armour.
	 * <li> {@link #getNewDamage(float)} for the method used to calculate new damage (It's a multiplier).
	 */
	public void changeHealth(float offset) {
		if(offset < 0f){
			super.changeHealth(-this.getNewDamage(-offset));
		}else{			
			super.changeHealth(offset);
		}
	}
	
	/**
	 * Gets a String representation of this armour component, displaying the percentage of armour held.
	 * @see {@link #getReadablePercentage()} for the method that gets readable armour percentage.
	 */
	public String toString(){
		StringBuilder str = new StringBuilder();
		
		str.append("Armoured Health : ");
		str.append(this.getReadablePercentage());
		str.append(" damage reduction.");
		
		return str.toString();
	}
}
