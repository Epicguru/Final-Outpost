package co.uk.epicguru.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.main.Main;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import co.uk.epicguru.tiles.instances.Dirt;
import ro.fortsoft.pf4j.Extension;

@Extension
public class DirtFactory extends TileFactory {

	public DirtFactory() {
		super(Main.INSTANCE, "Dirt");
	}
	
	public void gameStart(){
		super.setDefaultTexture(Main.INSTANCE.getAsset("Textures/Map/Dirt.png", TextureRegion.class));		
	}

	public Tile getInstance() {
		return new Dirt(this);
	}

}
