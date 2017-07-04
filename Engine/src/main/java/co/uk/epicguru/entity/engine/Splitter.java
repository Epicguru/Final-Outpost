package co.uk.epicguru.entity.engine;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;

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
	
	public void placeEntity(Entity e){
		// Get coord.
		int x = (int)(e.getX() / this.sectorSize);
		int y = (int)(e.getY() / this.sectorSize);
		
		Rectangle found = null;
		
		// Get rectangle.
		for(Rectangle r : this.e.keySet()){
			if((int)(r.x / this.sectorSize) == x && (int)(r.y / this.sectorSize) == y){
				found = r;
				break;
			}
		}
		
		if(found == null){
			Log.error("Splitter", "Unable to map entity to system with entity at " + e.getPosition() + " (" + x + "," + y + ")");
			return;
		}
		
		this.e.get(found).add(e);
	}
}
