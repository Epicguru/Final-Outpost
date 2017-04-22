package co.uk.epicguru.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.main.FOE;

public class Observer extends Base {

	private OrthographicCamera camera = this.getDefaultCamera();
	private Color colour = this.getDefaultColour();
	private float scale = this.getDefaultScale();
	private Batch batch = this.getDefaultBatch();
	
	public Color getDefaultColour(){
		return Color.WHITE;
	}
	
	public float getDefaultScale(){
		return 1f;
	}
	
	public OrthographicCamera getDefaultCamera(){
		return FOE.UIcamera;
	}
	
	public Batch getDefaultBatch(){
		return FOE.batch;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
	
	public float getScale(){
		return this.scale;
	}
	
	public void setColour(Color colour){
		if(colour == null){
			this.colour = this.getDefaultColour();
			return;
		}
		this.colour = colour;
	}

	public Color getColour(){
		return this.colour;
	}
	
	public void setCamera(OrthographicCamera camera){
		if(camera == null){
			this.camera = this.getDefaultCamera();
			return;
		}
		this.camera = camera;
	}
	
	public OrthographicCamera getCamera(){
		return this.camera;
	}
	
	public void setBatch(Batch batch){
		this.batch = batch;
	}
	
	public Batch getBatch(){
		return this.batch;
	}
}
