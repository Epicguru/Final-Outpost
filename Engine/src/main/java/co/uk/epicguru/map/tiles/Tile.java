package co.uk.epicguru.map.tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	
  private static HashMap<Character, Integer> alpha;
	public static float ONE = 1.001f;
  
	public static void clearUpdates() {
		toUpdate.clear();	
		toBin.clear();
	}
	
	public static void updateAll(float delta){
		for(Tile tile : toUpdate){
			tile.update(delta);
		}
		
		for(Tile tile : toBin){
			toUpdate.remove(tile);
		}
		toBin.clear();
	}

	public static boolean addTile(TileFactory factory){

		if(factory == null)
			throw new IllegalArgumentException("The tile factory cannot be null!");
		if(factory.getName() == null)
			throw new IllegalArgumentException("The name of the tile, as returned by getName(), cannot be null.");
		if(factory.getName().isEmpty())
			throw new IllegalArgumentException("The name of the tile, as returned by getName(), cannot be empty.");
		if(factory.getPlugin() == null)
			throw new IllegalArgumentException("The plugin of the tile, as returned by getPlugin(), cannot be null.");


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

	public static void doAdvancedSort(){

		if(factories.isEmpty())
			return;

		Log.info(TAG, "Sorting all tiles...");
		
		// Sort alphabetically
		alpha = new HashMap<>();
		alpha.clear();
		TileFactory[] sorted = factories.toArray(new TileFactory[0]);
		Arrays.sort(sorted);
		char current = factories.get(0).getName().charAt(0);
		alpha.put(current, 0);
		int index = 0;
		for(TileFactory tile : factories){
			if(tile == null || tile.getName() == null || tile.getName().isEmpty())
				throw new IllegalArgumentException("Tile '" + tile == null ? "Null (Tile)" : tile.getName() + " (Name)" + "' was invalid for sorting!");
			char start = tile.getName().charAt(0);
			if(start != current){
				current = start;
				alpha.put(current, index);
			}
			index++;
		}
		
		Log.info(TAG, "Sorted into " + alpha.size() + " letters.");
		for(TileFactory tile : factories){
			Log.info(TAG, ">>> " + tile.getName());
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

		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be null or empty : " + name);

		if(alpha != null){
			// Get start index
			Integer index = alpha.get(name.charAt(0));
			if(index == null){
				Log.error(TAG, "Failed to get tile of name '" + name + "'");
				return null;
			}

			// Get tile
			for(int i = index; i < factories.size(); i++){
				if(factories.get(i).getName().equals(name))
					return factories.get(i);
			}
			Log.error(TAG, "Failed to get tile of name '" + name + "'");
			return null;
		}else{
			// Loop through all
			for(TileFactory tile : factories){
				if(tile.getName().equals(name))
					return tile;
			}
			Log.error(TAG, "Failed to get tile of name '" + name + "'");
			return null;
		}
	}

	public static void registerTiles(){
		reset();

		for(FinalOutpostPlugin plugin : FOE.pluginsLoader.getAllPlugins()){
			List<TileFactory> tiles = FOE.pluginsLoader.getExtensions(TileFactory.class, plugin.getWrapper().getPluginId());
			Log.info(TAG, "Plugin '" + plugin.getWrapper().getPluginId() + "' registered " + tiles.size() + " tiles!");

			for(TileFactory tile : tiles){
				addTile(tile);
			}			
		}	

		doAdvancedSort();
	}
	
	public static void reset(){
		factories.clear();
		alpha = null;
		
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
	private static Vector2 centre = new Vector2();

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

	public void requestUpdate(){
		if(!toUpdate.contains(this))
			toUpdate.add(this);
	}
	
	public void cancelUpdate(){
		toBin.add(this);
	}
	
	public Vector2 getCentre(){
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
	
	public void update(float delta){

	}

	public void render(Batch batch){

	}
}
