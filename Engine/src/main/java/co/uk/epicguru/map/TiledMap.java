package co.uk.epicguru.map;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;

public class TiledMap{

	private Tile[][] tiles;
	private int width, height;
	
	/**
	 * Creates a blank 2D TiledMap and calls {@link #create(int, int)} to start the map.
	 */
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
	 * Creates a new 2D array of tiles given a starting position and a size.
	 * As always the tiles returned may be null, but the returned value will never be null.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 * @param width The width of the range, in tiles.
	 * @param height The height of the range, int tiles.
	 * @return The 2D array of tiles. Note that to find the coordinates of the tiles, use tile.getX() and getY().
	 */
	public Tile[][] getTiles(int x, int y, int width, int height){		
		Tile[][] tiles = new Tile[width][height];
		
		for(int i = x; i < x + width; i++){
			for(int j = y; j < y + height; j++){
				tiles[i][j] = getTile(i, j);
			}
		}
		
		return tiles;
	}
	
	/**
	 * Creates a new 2D array of tiles given a starting position and a size.
	 * As always the tiles returned may be null, but the returned value will never be null.
	 * @param start The starting posisition. All values are truncated to an int.
	 * @param size The size of the range. All values are truncated to an int.
	 * @return The 2D array of tiles. Note that to find the coordinates of the tiles, use tile.getX() and getY().
	 */
	public Tile[][] getTiles(Vector2 start, Vector2 size){
		return getTiles((int)start.x, (int)start.y, (int)size.x, (int)size.y);
	}
	
	/**
	 * Creates a new 2D array of tiles given a rectangular range.
	 * As always the tiles returned may be null, but the returned value will never be null.
	 * @param rectangle The range to get. The x and y represent the start and the width and height are the size.
	 * @return The 2D array of tiles. Note that to find the coordinates of the tiles, use tile.getX() and getY().
	 */
	public Tile[][] getTiles(Rectangle rectangle){
		return getTiles((int)rectangle.x, (int)rectangle.y, (int)rectangle.width, (int)rectangle.height);
	}
	
	/**
	 * Gets all tiles in a circular range, where the starting position in the centre. Note that this calls {@link #getTiles(int, int, int, int)}
	 * which takes time and also uses tiles distanceTo() method which uses square root. 
	 * Overall this is not very fast, so avoid necessary repeat calls. 
	 * @param x The centre X position, in tiles.
	 * @param y The centre Y position, in tiles.
	 * @param radius The radius (half width) to get tiles within.
	 * @return The one dimensional array. Unlike other methods, the tiles within this array will NOT be null.
	 */
	public Tile[] getTilesRange(float x, float y, float radius){
		
		int extra = 1;
		int size = (int)radius;
		int ri = extra + size;
		int X = (int)x - ri / 2;
		int Y = (int)y - ri / 2;
		X -= extra;
		Y -= extra;
		
		Tile[][] tiles = getTiles(X, Y, ri, ri);
		ArrayList<Tile> tilesTemp = new ArrayList<>();
		
		for(Tile[] array : tiles){
			for(Tile tile : array){
				if(tile == null)
					continue;
				if(tile.distanceTo(x, y) <= radius){
					tilesTemp.add(tile);
				}
			}
		}
		
		return tilesTemp.toArray(new Tile[tilesTemp.size()]);
	}
	
	/**
	 * Gets all tiles in a circular range, where the starting position in the centre. Note that this calls {@link #getTiles(int, int, int, int)}
	 * which takes time and also uses tiles distanceTo() method which uses square root. 
	 * Overall this is not very fast, so avoid necessary repeat calls. 
	 * @param centre The centre of the range, units are tiles.
	 * @param radius The radius (half width) to get tiles within.
	 * @return The one dimensional array. Unlike other methods, the tiles within this array will NOT be null.
	 */
	public Tile[] getTilesRange(Vector2 centre, float radius){
		return getTilesRange(centre.x, centre.y, radius);
	}
	
	/**
	 * Gets all tiles in a circular range, where the starting position in the centre. Note that this calls {@link #getTiles(int, int, int, int)}
	 * which takes time and also uses tiles distanceTo() method which uses square root. 
	 * Overall this is not very fast, so avoid necessary repeat calls. 
	 * @param circle The 2D circle shape for which to get tiles within. Note that all dimensions of this circle are in tiles.
	 * @return The one dimensional array. Unlike other methods, the tiles within this array will NOT be null.
	 */
	public Tile[] getTilesRange(Circle circle){
		return getTilesRange(circle.x, circle.y, circle.radius);
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
	 * Returns true if the coordinates are within the world boundaries. That means that if X or Y is equal to the width or height,
	 * it will be out of bounds. Similarly if X or Y are smaller that 0, it will be out of bounds.
	 * @param x The X coordinate, in tiles.
	 * @param y The Y coordinate, in tiles.
	 */
	boolean inBounds(int x, int y){
		return (x >=0 && x < getWidth()) && (y >= 0 && y < getHeight());
	}
}