package co.uk.epicguru.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.main.Main;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import co.uk.epicguru.tiles.instances.Stone;
import ro.fortsoft.pf4j.Extension;

@Extension
public class StoneFactory extends TileFactory{

	public StoneFactory() {
		super(Main.INSTANCE, "Stone");
	}
	
	public void gameStart(){
		super.setDefaultTexture(Main.INSTANCE.getAsset("Textures/Map/Stone.png", TextureRegion.class));		
	}

	public Tile getInstance() {
		return new Stone(this);
	}

}
