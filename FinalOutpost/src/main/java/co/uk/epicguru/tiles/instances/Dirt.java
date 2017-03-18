package co.uk.epicguru.tiles.instances;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.main.Main;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class Dirt extends Tile {
	
	public static TextureRegion texture;
	
	public Dirt(TileFactory parent) {
		super(parent);
		if(texture == null){
			texture = Main.INSTANCE.getAsset("Textures/Map/Dirt.png", TextureRegion.class);
		}
	}

	public void render(Batch batch){
		
		
		batch.draw(texture, getX(), getY(), 1, 1);
	}
	
}
