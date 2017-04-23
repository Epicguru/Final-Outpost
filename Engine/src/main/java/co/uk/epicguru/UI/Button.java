package co.uk.epicguru.UI;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import co.uk.epicguru.input.Input;

public abstract class Button extends UI {

	private NinePatch patch;
	private ArrayList<Runnable> clickEventsLeft = new ArrayList<Runnable>();
	private ArrayList<Runnable> clickEventsRight = new ArrayList<Runnable>();
	
	public Button(NinePatch patch){
		this.setPatch(patch);
	}
	
	public void setPatch(NinePatch patch){
		this.patch = patch;
		if(patch != null){
			super.bounds.setSize(patch.getTotalWidth(), patch.getTotalHeight());			
		}
	}
	
	public NinePatch getPatch(){
		return this.patch;
	}
	
	public void addLeftClickListener(Runnable r){
		if(r == null)
			return;
		
		this.clickEventsLeft.add(r);
	}
	
	public void addRightClickListener(Runnable r){
		if(r == null)
			return;
		
		this.clickEventsRight.add(r);
	}
	
	public void clickedUponLeft(){
		for(Runnable r : this.clickEventsLeft){
			if(r != null)
				r.run();
		}
	}
	
	public void clickedUponRight(){
		for(Runnable r : this.clickEventsRight){
			if(r != null)
				r.run();
		}
	}
	
	public void update(Observer obs, float delta){
		
		String state = super.bounds.toString(); // Kinda cool right :d
		
		super.bounds.setSize(this.getFinalWidth(obs), this.getFinalHeight(obs));
		
//		obs.getBatch().end();
//		ShapeRenderer shapes = new ShapeRenderer();
//		shapes.setColor(Color.RED);
//		shapes.setAutoShapeType(true);
//		shapes.begin();
//		shapes.rect(super.bounds.x, super.bounds.y, super.bounds.width, super.bounds.height);
//		shapes.end();
//		obs.getBatch().begin();
		
		if(super.bounds.contains(Input.getMousePos())){
			if(Input.getMouseButtonJustDown(Buttons.LEFT)){
				// Left click event triggered
				
				this.clickedUponLeft();
				
			}else if(Input.getMouseButtonJustDown(Buttons.RIGHT)){
				// Right click event triggered
				
				this.clickedUponRight();
			}
		}
		
		super.bounds.fromString(state); // Great, finally some nice methods in libgdx!
	}
	
	public float getFinalWidth(Observer obs){
		float width = super.bounds.width * super.localScale * obs.getScale();
		return width;
	}
	
	public float getFinalHeight(Observer obs){
		float height = super.bounds.height * super.localScale * obs.getScale();
		return height;
	}
	
	public void render(Observer obs, float delta){
		
		this.update(obs, delta);
		
		if(this.getPatch() == null)
			return;
		
		float x = super.bounds.x;
		float y = super.bounds.y;
		
		this.getPatch().setColor(UI.mul(super.colour, obs.getColour()));
		this.getPatch().draw(obs.getBatch(), x, y, this.getFinalWidth(obs), this.getFinalHeight(obs));
	}	
}
