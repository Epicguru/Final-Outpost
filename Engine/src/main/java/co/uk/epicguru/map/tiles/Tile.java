package co.uk.epicguru.map.tiles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public abstract class Tile extends Base {
		
	// STATIC
	private static final String TAG = "Tile";
	private static ArrayList<TileFactory> factories = new ArrayList<>();
	private static ArrayList<Tile> toUpdate = new ArrayList<>();
	private static ArrayList<Tile> toBin = new ArrayList<>();
	private static boolean sort;
	public static float ONE = 1.001f;
	
	public static void clearUpdates() {
		toUpdate.clear();	
		toBin.clear();
	}
	
	public static void updateAll(float delta){
		for(Tile tile : toUpdate){
			if(tile.needsToUpdate()){
				tile.update(delta);				
			}else{
				toBin.add(tile);
			}
		}
		
		for(Tile tile : toBin){
			toUpdate.remove(tile);
		}
		toBin.clear();
	}
	
	public static boolean addTile(TileFactory factory){
		if(!containsTile(factory.getName())){
			factories.add(factory);
			Log.info(TAG, "Registered '" + factory.getName() + "'");
			return true;
		}else{
			// Error
			Log.error(TAG, "A tile with the name '" + factory.getName() + "' is already registered!");
			return false;
		}
	}
	
	public void setAdvancedSort(boolean enabled){
		if(sort == enabled)
			return;
		
		if(sort){
			
		}else{
			
		}
	}
	
	public static ArrayList<TileFactory> getAllTileFactories(){
		return factories;
	}
	
	public static Tile getNewTile(String name){
		TileFactory t = getTile(name);
		return t == null ? null : t.getInstance();
	}
	
	public static TileFactory getTile(String name){
		for(TileFactory tile : factories){
			if(tile.getName().equals(name))
				return tile;
		}
		Log.error(TAG, "Failed to get tile of name '" + name + "'");
		return null;
	}
	
	public static void registerTiles(){
		factories.clear();
		
		for(FinalOutpostPlugin plugin : FOE.pluginsLoader.getAllPlugins()){
			List<TileFactory> tiles = FOE.pluginsLoader.getExtensions(TileFactory.class, plugin.getWrapper().getPluginId());
			Log.info(TAG, "Plugin '" + plugin.getWrapper().getPluginId() + "' registered " + tiles.size() + " tiles!");
			
			for(TileFactory tile : tiles){
				addTile(tile);
			}			
		}		
	}
	
	private static boolean containsTile(String name){
		for(TileFactory tile : factories){
			if(tile.getName().equals(name))
				return true;
		}
		return false;
	}
	
	// INSTANCE
	
	private TileFactory parent;
	private int x, y;
	private Vector2 centre;
	
	public Tile(TileFactory parent){
		this.parent = parent;
	}
	
	public TileFactory getParent(){
		return parent;
	}
	
	public String getName(){
		return parent.getName();
	}
	
	public FinalOutpostPlugin getPlugin(){
		return parent.getPlugin();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Vector2 getCentre(){
		if(centre == null){
			centre = new Vector2();
		}
		centre.set(x + 0.5f, y + 0.5f);
		return centre;
	}
	
	public float distanceTo(Tile tile){
		return distanceTo(tile.getCentre());
	}
	
	public float distanceTo(Vector2 position){
		return distanceTo(position.x, position.y);
	}
	
	public float distanceTo(float x, float y){
		float xDst = x - getCentre().x;
		float yDst = y - getCentre().y;
		float sum = (xDst * xDst) + (yDst * yDst);
		float dst = (float) Math.sqrt(sum);
		
		return dst;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean needsToUpdate(){
		return false;
	}
	
	public void update(float delta){
		
	}
	
	public void render(Batch batch){
		
	}
}
