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
	
	private int sectorSize = 16; // Lets try to keep both this and the world size Po2.
	private HashMap<Rectangle, ArrayList<Entity>> e;
	private ArrayList<Entity> returnValue = new ArrayList<Entity>();
	private ArrayList<Entity> buffer = new ArrayList<Entity>();
	private Rectangle bounds = new Rectangle();
	
	public Splitter(Engine engine){
		this.engine = engine;
	}
	
	public int getSectorSize(){
		return this.sectorSize;
	}
	
	public int getWidth(){
		return this.map == null ? 0 : (int)(this.map.getWidth() / this.getSectorSize());
	}
	
	public int getHeight(){
		return this.map == null ? 0 : (int)(this.map.getHeight() / this.getSectorSize());
	}
	
	public void setMap(TiledMap map){
		this.map = map;
		//if(map != null){
			makeEntityMap();
		//}
	}
	
	public void makeEntityMap(){
		
		// We need to make rectangles first.
		int width = (int)(map.getWidth() / sectorSize);
		int height = (int)(map.getHeight() / sectorSize);
		
		Log.info("Splitter", "Map is " + map.getWidth() + " by " + map.getHeight() + ", so we will have " + width + " by " + height + " regions. (" + width * height + " regions)");
		
		Rectangle[] rects = new Rectangle[width * height];
		int i = 0;
		
		this.e = new HashMap<Rectangle, ArrayList<Entity>>();
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				
				// TODO make dimensions for world borders.
				float w = sectorSize;
				float h = sectorSize;
				
				float X = x * sectorSize;
				float Y = y * sectorSize;
				
				Rectangle rect = new Rectangle(X, Y, w, h);
				rects[i++] = rect;
				
				Log.info("Splitter", "Region " + (int)(rect.x / this.sectorSize) + ", " + (int)(rect.y / this.sectorSize) + " size " + rect.width);
			}
		}
		
		for(Rectangle r : rects){
			
			e.put(r, new ArrayList<Entity>());
			
		}
	}	
	
	public void clearEntities(){
		for(ArrayList<Entity> a : this.e.values()){
			a.clear();
		}
	}
	
	public void placeEntities(){
		for(Entity e : engine.getAllEntities()){
			placeEntity(e);
		}
	}
	
	public Rectangle getRectFromPos(float posX, float posY){
		int x = (int)(posX / this.sectorSize);
		int y = (int)(posY / this.sectorSize);
		
		Rectangle found = null;
		
		// Get rectangle.
		for(Rectangle r : this.e.keySet()){
			if((int)(r.x / this.sectorSize) == x && (int)(r.y / this.sectorSize) == y){
				found = r;
				break;
			}
		}
		
		return found;
	}
	
	public ArrayList<Entity> getEntitiesIn(Rectangle rect){
		return this.e.get(rect);
	}
	
	public ArrayList<Entity> getEntitiesIn(Vector2 position){
		return this.getEntitiesIn(getRectFromPos(position.x, position.y));
	}
	
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
		
		int X = (int)(x / this.sectorSize);
		int Y = (int)(y / this.sectorSize);
		int topX = (int)((x + width) / this.sectorSize);
		int topY = (int)((y + height) / this.sectorSize);
		
		this.returnValue.clear();
		
		for(int i = X; i < topX; i++){
			for(int j = Y; j < topY; j++){
				FINDING : for(Rectangle r : this.e.keySet()){
					if((int)(r.x / this.sectorSize) == i && (int)(r.y / this.sectorSize) == j){
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
	
	public ArrayList<Entity> getInRange(float x, float y, float radius){
		
		float bottomX = x - radius;
		float bottomY = y - radius;
		
		getInRect(bottomX, bottomY, radius * 2f + this.sectorSize, radius * 2f + this.sectorSize);
		
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
