package co.uk.epicguru.tiles.instances;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class Dirt extends Tile {
	
	public Dirt(TileFactory parent) {
		super(parent);
	}

	public void render(Batch batch){		
		batch.draw(getParent().getDefaultTexture(), getX(), getY(), ONE, ONE);
	}
	
}
