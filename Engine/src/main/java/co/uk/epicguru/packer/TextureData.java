package co.uk.epicguru.packer;

import java.io.File;

import com.badlogic.gdx.graphics.Texture;

public class TextureData {

	public File file;
	public Texture texture;
	
	public TextureData(File file, Texture texture){
		this.file = file;
		this.texture = texture;
	}
	
	public void dispose(){
		texture.dispose();
		file = null;
	}
	
}
