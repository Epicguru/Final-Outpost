package co.uk.epicguru.map;

import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;

public interface TiledMap{

	int getWidth();
	
	int getHeight();
	
	void setTile(Tile tile, int x, int y);
	void setTile(TileFactory factory, int x, int y);
	
	Tile getTile(int x, int y);
	default boolean isTileEmpty(int x, int y) { return getTile(x, y) == null; }
	
	boolean inBounds(int x, int y);	
}
