package co.uk.epicguru.entity.engine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.map.TiledMap;

/**
 * Splits the world into chunks that are used to access entities.
 * This will mainly be a helper class that will be in charge of separating entities into regions for faster access.
 * The idea is that this will work completely behind the scenes, apart from some small code in the {@link Entity entity} class.
 * @author James Billy
 */
public class Splitter {
	
	private TiledMap map;
	private Engine engine;
	
	private int regionSize = 16; // Lets try to keep both this and the world size Po2.
	private HashMap<Rectangle, ArrayList<Entity>> e;
	private ArrayList<Entity> returnValue = new ArrayList<Entity>();
	private ArrayList<Entity> buffer = new ArrayList<Entity>();
	private Rectangle bounds = new Rectangle();
	
	public Splitter(Engine engine){
		this.engine = engine;
	}
	
	/**
	 * Gets the region size in tiles.
	 * Therefore a region contains (x*x) tiles where x is the value returned by this.
	 */
	public int getRegionSize(){
		return this.regionSize;
	}
	
	/**
	 * Gets the amount of horizontal regions in the current world.
	 */
	public int getWidth(){
		return this.map == null ? 0 : (int)(this.map.getWidth() / this.getRegionSize());
	}
	
	/**
	 * Gets the amount of vertical regions in the current world.
	 */
	public int getHeight(){
		return this.map == null ? 0 : (int)(this.map.getHeight() / this.getRegionSize());
	}
	
	/**
	 * Internal method.
	 */
	public void setMap(TiledMap map){
		this.map = map;
		//if(map != null){
			makeEntityMap();
		//}
	}
	
	/**
	 * Internal method.
	 */
	public void makeEntityMap(){
		
		// We need to make rectangles first.
		int width = (int)(map.getWidth() / regionSize);
		int height = (int)(map.getHeight() / regionSize);
		
		Log.info("Splitter", "Map is " + map.getWidth() + " by " + map.getHeight() + ", so we will have " + width + " by " + height + " regions. (" + width * height + " regions)");
		
		Rectangle[] rects = new Rectangle[width * height];
		int i = 0;
		
		this.e = new HashMap<Rectangle, ArrayList<Entity>>();
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				// TODO make dimensions for world borders.
				float w = regionSize;
				float h = regionSize;
				
				float X = x * regionSize;
				float Y = y * regionSize;
				
				Rectangle rect = new Rectangle(X, Y, w, h);
				rects[i++] = rect;
				
				// Log.info("Splitter", "Region " + (int)(rect.x / this.regionSize) + ", " + (int)(rect.y / this.regionSize) + " size " + rect.width);
			}
		}
		
		for(Rectangle r : rects){			
			e.put(r, new ArrayList<Entity>());			
		}
	}	
	
	/**
	 * Clears all entities from the splitting system.
	 */
	public void clearEntities(){
		for(ArrayList<Entity> a : this.e.values()){
			a.clear();
		}
	}
	
	/**
	 * Places all entities in the world into the splitting system.
	 * @see {@link #placeEntity(Entity)} to place idividual entities.
	 */
	public void placeEntities(){
		for(Entity e : engine.getAllEntities()){
			placeEntity(e);
		}
	}
	
	/**
	 * Gets the bounding region for a position in the world.
	 * The region size is defined {@link #getRegionSize() here}.
	 * @param posX The world space position, in tiles.
	 * @param posY The world space position, in tiles.
	 * @return The rectangle object that is the bounds of the region. All values are in world space values (in tiles).
	 * Note that the same object is returned every time, so copy the rectangle if you intend to store it for later.
	 */
	public Rectangle getRectFromPos(float posX, float posY){
		int x = (int)(posX / this.regionSize);
		int y = (int)(posY / this.regionSize);
		
		Rectangle found = null;
		
		// Get rectangle.
		for(Rectangle r : this.e.keySet()){
			if((int)(r.x / this.regionSize) == x && (int)(r.y / this.regionSize) == y){
				found = r;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Places an entity in the splitting system. This is does automagically every frame for all entities.
	 */
	public void placeEntity(Entity e){
		// Get coord.
		float posX = e.getX();
		float posY = e.getY();
		
		Rectangle found = getRectFromPos(posX, posY);
		
		if(found == null){
			Log.error("Splitter", "Unable to map entity to system with entity at " + e.getPosition());
			return;
		}
		
		this.e.get(found).add(e);
	}


	// ACCESSING METHODS BELOW
	
	/**
	 * Gets all entities in the same bounding region as the point.
	 * @param position The world space position, in tiles.
	 * @return The list of entities. The same object is returned every time.
	 * @see {@link #getRegionSize() Region size.}
	 */
	public ArrayList<Entity> getInPoint(Vector2 position){
		return this.e.get(getRectFromPos(position.x, position.y));
	}	
	
	/**
	 * Gets all entities within a bounding box.
	 * @param x The bottom left x position within the world, in tiles.
	 * @param y The bottom left y position within the world, in tiles.
	 * @param width The width in tiles of the area.
	 * @param height The height in tiles of the area.
	 * @return The list of entities within the area. The same object is returned every time.
	 */
	public ArrayList<Entity> getInRect(float x, float y, float width, float height){
		
		// Checks
		if(x < 0)
			x = 0;
		if(y < 0)
			y = 0;
		if(x > map.getWidth())
			x = map.getWidth();
		if(y > map.getHeight())
			y = map.getHeight();
		if(x + width > map.getWidth())
			width = map.getWidth() - x;
		if(y + height > map.getHeight())
			height = map.getHeight() - y;
		
		bounds.set(x, y, width, height);
		
		int X = (int)(x / this.regionSize);
		int Y = (int)(y / this.regionSize);
		int topX = (int)((x + width) / this.regionSize);
		int topY = (int)((y + height) / this.regionSize);
		
		this.returnValue.clear();
		
		for(int i = X; i < topX; i++){
			for(int j = Y; j < topY; j++){
				FINDING : for(Rectangle r : this.e.keySet()){
					if((int)(r.x / this.regionSize) == i && (int)(r.y / this.regionSize) == j){
						for(Entity entity : this.e.get(r)){
							if(bounds.contains(entity.getPosition()))
								returnValue.add(entity);
						}
						break FINDING;
					}
				}
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Gets all entities within a certain range of a point.
	 * @param x The x position of the center of the area, in tiles.
	 * @param y The y position of the center of the area, in tiles.
	 * @param radius The radius of the area, which is a circle.
	 * @return All entities within the range.
	 */
	public ArrayList<Entity> getInRange(float x, float y, float radius){
		
		float bottomX = x - radius;
		float bottomY = y - radius;
		
		getInRect(bottomX, bottomY, radius * 2f + this.regionSize, radius * 2f + this.regionSize);
		
		buffer.clear();
		for(Entity e : this.returnValue){
			if(e.distanceTo(x, y) > radius){
				buffer.add(e);
			}
		}
		
		for(Entity e : buffer){
			this.returnValue.remove(e);
		}
		
		buffer.clear();
		
		return this.returnValue;
	}
}
