package co.uk.epicguru.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class TextButton extends Button {

	private static BitmapFont font;
	private String text = "Default text";
	private Color colour = Color.BLACK;
	
	public TextButton(NinePatch patch) {
		super(patch);
		
		if(font == null){
			font = new BitmapFont();
		}
		
	}
	
	public Color getColour(){
		return this.colour;
	}
	
	public void setColour(Color colour){
		this.colour = colour;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public void render(Observer obs, float delta) {
		super.render(obs, delta);
		
		float x = super.bounds.x + super.bounds.width / 2f;
		float y = super.bounds.y + super.bounds.height / 2f + 5;
		
		// Render text here
		font.setColor(UI.mul(this.getColour(), obs.getColour()));
		font.draw(obs.getBatch(), this.getText(), x, y, -1, 1, false);
	}	
}
