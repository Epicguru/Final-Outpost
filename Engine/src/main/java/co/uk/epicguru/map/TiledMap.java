package co.uk.epicguru.map;

import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;

public class TiledMap{

	private Tile[][] tiles;
	private int width, height;
	
	public TiledMap(int width, int height){
		create(width, height);
	}
	
	/**
	 * Creates (or re-creates) this tiled map. Sets width and height and creates new 2D array.
	 * @param width The width of the map in tiles.
	 * @param height The height of the map in tiles.
	 */
	public void create(int width, int height){
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
	}
	
	/**
	 * Gets the width of the map in tiles.
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Gets the height of the map in tiles.
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Sets the tile at the given coordinates if the coordinates are within the bounds of the map.
	 * @param tile The Tile object to set. It can be null.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 * @see {@link #inBounds(int, int)} to check if is in world bounds.
	 */
	public void setTile(Tile tile, int x, int y){
		if(inBounds(x, y)){
			Tile t = tiles[x][y] = tile;
			t.setPosition(x, y);
		}
	}
	
	/**
	 * Sets the tile at the given coordinates if the coordinates are within the bounds of the map.
	 * The tile is got using factory.getInstance().
	 * @param tile The TileFactory object to get a tile from. It CANNOT be null.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 * @see {@link #inBounds(int, int)} to check if is in world bounds.
	 */
	public void setTile(TileFactory factory, int x, int y){
		if(factory == null){			
			return;
		}
		setTile(factory.getInstance(), x, y);
	}
	
	/**
	 * Gets the tile at a specific coordinate. This may return null.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 * @return The Tile at the position, or null if out of bounds or if tile is null.
	 */
	public Tile getTile(int x, int y){
		if(inBounds(x, y))
			return tiles[x][y];
		else
			return null;
	}
	
	/**
	 * Checks to see if a specific coordinate contains a tile or not.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 * @return True if the tile IS NOT NULL, false if it is null.
	 */
	public boolean isTileEmpty(int x, int y){ 
		return getTile(x, y) == null; 
	}
	
	/**
	 * Returns true if the coordinates are within the world boundaries. That means that if X or Y is equal 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean inBounds(int x, int y){
		return (x >=0 && x < getWidth()) && (y >= 0 && y < getHeight());
	}
}
