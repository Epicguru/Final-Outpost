package co.uk.epicguru.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import co.uk.epicguru.API.Base;

public abstract class UI extends Base {
	
	public Rectangle bounds = new Rectangle(0, 0, 100, 100);
	public float localScale = 1f;
	public Color colour = Color.WHITE;
	
	public abstract void render(Observer obs, float delta);
	
	public String toString(){
		return "Generic UI element";
	}
	
	private static Color tempColour = new Color();
	public static Color mul(Color a, Color b){
		tempColour.set(a);
		tempColour.mul(b);
		
		return tempColour;
	}
}
