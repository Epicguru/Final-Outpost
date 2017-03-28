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
import co.uk.epicguru.map.TiledMap;

/**
 * A square or rectangular object that makes up the world map. Works along with {@link TileFactory} and {@link TiledMap}.
 * @author James Billy
 */
public abstract class Tile extends Base {

	// STATIC
	private static final String TAG = "Tile";
	private static ArrayList<TileFactory> factories = new ArrayList<>();

	private static ArrayList<Tile> toAdd = new ArrayList<>();
	private static ArrayList<Tile> toUpdate = new ArrayList<>();
	private static ArrayList<Tile> toBin = new ArrayList<>();
	
	private static HashMap<Character, Integer> alpha;
	/**
	 * Almost 1, but not quite. 1.001f. This is used when rendering tiles, as a value of exactly 1
	 * leads to "bars" between tiles when scrolling.
	 */
	public static float ONE = 1.001f;
  
	/**
	 * Clears all tiles from the update list, and all other associated lists.
	 */
	public static void clearUpdates() {
		toUpdate.clear();	
		toBin.clear();
	}
	
	/**
	 * Updates all tiles that have been requested to update.
	 * @param delta The delta time value.
	 */
	public static void updateAll(float delta){
		for(Tile tile : toAdd){
			toUpdate.add(tile);
		}
		toAdd.clear();
		for(Tile tile : toUpdate){
			tile.update(delta);
		}
		
		for(Tile tile : toBin){
			toUpdate.remove(tile);
		}
		toBin.clear();
	}

	/**
	 * Adds (registers) a tile factory to the entire game. This is required for map saving and multiplayer integration.
	 * @param factory The TileFactory value. May not be null.
	 */
	public static void addTile(TileFactory factory){

		if(factory == null)
			throw new IllegalArgumentException("The tile factory cannot be null!");
		if(factory.getName() == null)
			throw new IllegalArgumentException("The name of the tile, as returned by getName(), cannot be null.");
		if(factory.getName().isEmpty())
			throw new IllegalArgumentException("The name of the tile, as returned by getName(), cannot be empty.");
		if(factory.getPlugin() == null)
			throw new IllegalArgumentException("The plugin of the tile, as returned by getPlugin(), cannot be null.");

		if(factories.contains(factory)){
			throw new IllegalStateException("That tile has already been registered!");
		}
		
		if(containsTile(factory.getName())){
			// Duplicate
			Log.error(TAG, "Duplicate tile name '" + factory.getName() + "'. The tile will still be added and must be accessed using plugin parameter.");
			Log.info(TAG, "Registered tile '" + factory.getName() + "' of plugin " + factory.getPluginID());
			
			factories.add(factory);
		}else{
			// No problem
			Log.info(TAG, "Registered tile '" + factory.getName() + "' of plugin " + factory.getPluginID());
			
			factories.add(factory);
		}
	}

	/**
	 * Sorts all tiles alphabetically and indexes the result for faster access later. This is only efficient
	 * with large amounts of tiles.
	 */
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

	/**
	 * Gets all tiles known to mankind.
	 */
	public static ArrayList<TileFactory> getAllTileFactories(){
		return factories;
	}

	/**
	 * Gets a new instance of a {@link Tile} from a {@link TileFactory}.
	 * @param name The name of the tile factory.
	 * @return A new tile instance.
	 */
	public static Tile getNewTile(String name, FinalOutpostPlugin plugin){
		TileFactory t = getTile(name, plugin);
		return t == null ? null : t.getInstance();
	}
	
	public static Tile getNewTile(String name){
		TileFactory t = getTile(name);
		return t == null ? null : t.getInstance();
	}

	/**
	 * Gets a tile factory given a name and the plugin is belongs to.
	 * @param name The name of the tile to find. Cannot be null or empty.
	 * @param plugin The plugin that the tile belongs to, used when more that one plugin register the same tile name. If null, the first tile with name is returned.
	 * @return The {@link TileFactory} registered with {@link #addTile(TileFactory)}.
	 */
	public static TileFactory getTile(String name, FinalOutpostPlugin plugin){

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
				if(factories.get(i).getName().equals(name) && (plugin != null ? factories.get(i).getPlugin() == plugin : true))
					return factories.get(i);
			}
			Log.error(TAG, "Failed to get tile of name '" + name + "' and plugin " + plugin == null ? "Null" : plugin.getWrapper().getPluginId());
			return null;
		}else{
			// Loop through all
			for(TileFactory tile : factories){
				if(tile.getName().equals(name) && (plugin != null ? tile.getPlugin() == plugin : true))
					return tile;
			}
			Log.error(TAG, "Failed to get tile of name '" + name + "' and plugin " + plugin == null ? "Null" : plugin.getWrapper().getPluginId());
			return null;
		}
	}
	
	/**
	 * Gets a tile factory given a name.
	 * @param name The name of the tile to find. Cannot be null or empty.
	 * @return The {@link TileFactory} registered with {@link #addTile(TileFactory)}.
	 */
	public static TileFactory getTile(String name){
		return getTile(name, null);
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

	/**
	 * Checks to see if the tile is registered using {@link #addTile(TileFactory)}.
	 * @param name The name of the tile to find. Cannot be null or empty.
	 * @return True if present , false if not present.
	 */
	public static boolean containsTile(String name){
		return containsTile(name, null);
	}
	
	/**
	 * Checks to see if the tile is registered using {@link #addTile(TileFactory)}.
	 * @param name The name of the tile to find. Cannot be null or empty.
	 * @param plugin The plugin that the tile belongs to. If null returns the fist tile found.
	 * @return True if present (see both conditions), false if not present.
	 */
	public static boolean containsTile(String name, FinalOutpostPlugin plugin){
		
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be null or empty : " + name);
		
		for(TileFactory tile : factories){
			if(tile.getName().equals(name) && plugin != null ? tile.getPlugin() == plugin : true)
				return true;
		}
		return false;
	}

	// INSTANCE

	private TileFactory parent;
	private int x, y;
	private static Vector2 centre = new Vector2();

	/**
	 * Creates a new tile given a tile factory, its 'parent'.
	 * @param parent The parent {@link TileFactory}.
	 */
	public Tile(TileFactory parent){
		this.parent = parent;
	}

	/**
	 * Gets the {@link TileFactory} that this tile comes from. This can be used to check plugin and other details.
	 */
	public TileFactory getParent(){
		return parent;
	}

	/**
	 * Gets the name of this tile, as given by the parent {@link TileFactory}.
	 */
	public String getName(){
		return getParent().getName();
	}

	/**
	 * Gets the plugin that owns this tile, using the {@link TileFactory} parent.
	 * @see {@link #getParent()}.
	 */
	public FinalOutpostPlugin getPlugin(){
		return parent.getPlugin();
	}

	/**
	 * Gets the x position of this tile, in the world. Bottom left is the world origin, where the tile would be (0, 0).
	 */
	public int getX(){
		return x;
	}

	/**
	 * Gets the y position of this tile, in the world. Bottom left is the world origin, where the tile would be (0, 0).
	 */
	public int getY(){
		return y;
	}

	/**
	 * Requests for this tile to be updated from now on. This will make {@link #update(float)} call once a frame until
	 * {@link #cancelUpdate()}.
	 */
	public void requestUpdate(){
		if(!toAdd.contains(this))
			toAdd.add(this);
	}
	
	/**
	 * Requests that this tile is no longer updated, so is the opposite of {@link #requestUpdate()}.
	 * Please call this if possible to make game run as smoothly as possible.
	 */
	public void cancelUpdate(){
		toBin.add(this);
	}
	
	/**
	 * Gets the centre of this tile using {@link #getX()} and {@link #getY()}, and then adding half.
	 * This always returns the same vector object, so please make a copy if necessary.
	 */
	public Vector2 getCentre(){
		centre.set(x + 0.5f, y + 0.5f);
		return centre;
	}

	/**
	 * Gets the distance from the centre of this tile to another one, using pythagoras. This means that square root is needed 
	 * and may slow the program down if called is large amounts every frame.
	 * @param tile The other tile to check the distance to. If null this method will return -1.
	 * @return The distance from the centre of one tile to the other, or exactly -1 if the input is null.
	 */
	public float distanceTo(Tile tile){
		if(tile == null)
			return -1;
		return distanceTo(tile.getCentre());
	}

	/**
	 * Gets the distance from the centre of this tile to another one, using pythagoras. This means that square root is needed 
	 * and may slow the program down if called is large amounts every frame.
	 * @param position The position to check the distance to. If null this method will return -1.
	 * @return The distance from the centre of this tile to the position, or exactly -1 if the input is null.
	 */
	public float distanceTo(Vector2 position){
		if(position == null)
			return -1;
		return distanceTo(position.x, position.y);
	}

	/**
	 * Gets the distance from the centre of this tile to another one, using pythagoras. This means that square root is needed 
	 * and may slow the program down if called is large amounts every frame.
	 * @param x The x position to check the distance to. Must be in tiles.
	 * @param y The y position to check the distance to. Must be in tiles.
	 * @return The distance from the centre of one tile to the coordinates.
	 */
	public float distanceTo(float x, float y){
		float xDst = x - getCentre().x;
		float yDst = y - getCentre().y;
		float sum = (xDst * xDst) + (yDst * yDst);
		float dst = (float) Math.sqrt(sum);

		return dst;
	}

	/**
	 * Sets the LOCAL POSITION ONLY of this tile. This has NO EFFECT ON THE MAP. Please only use
	 * if you know exactly what you are doing!
	 */
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Called once per frame ONLY IF updates have been requested using {@link #requestUpdate()}.
	 * @param delta The delta time value.
	 */
	public void update(float delta){

	}

	/**
	 * Called once per frame, only if this tile is visible to the camera. Do not use this as an update method,
	 * see {@link #update(float)} for that.
	 * @param batch The batch to draw textures with. See {@link #ONE} for a value that represents the size of one tile.
	 */
	public void render(Batch batch){

	}
}
