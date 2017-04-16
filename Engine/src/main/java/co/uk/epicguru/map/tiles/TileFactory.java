package co.uk.epicguru.map.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;

/**
 * Represents a tile factory that pumps out {@link Tile}. 
 * @author James Billy
 */
public abstract class TileFactory extends Base implements Comparable<TileFactory>{

	private String name;
	private FinalOutpostPlugin plugin;
	private TextureRegion defaultTexture;
	
	/**
	 * Creates a new instance of a tile factory, given a plugin and a name.
	 * @param plugin The plugin that this tile comes from.
	 * @param name The name of this tile, should be user friendly but as short as possible for performance reasons.
	 */
	public TileFactory(FinalOutpostPlugin plugin, String name){
		this.plugin = plugin;
		this.name = name;
	}
	
	/**
	 * Sets the default texture for this tile factory, which should graphically represent the tile.
	 * Must be 32x32 pixels.
	 * @param texture The new texture.
	 * @see {@link #getDefaultTexture()} to get the default texture.
	 */
	public void setDefaultTexture(TextureRegion texture){
		this.defaultTexture = texture;
	}
	
	/**
	 * Gets the default texture of this tile factory, which is used to display icons and tooltips.
	 * Should be exactly 32x32 pixels (1x1 tile).
	 * @return The texture region that represents this texture.
	 * @see {@link #setDefaultTexture(TextureRegion)} to set the texture.
	 */
	public TextureRegion getDefaultTexture(){
		return this.defaultTexture;
	}
	
	/**
	 * Called when a game world is loaded. This may be called MORE THAN ONCE ONE ONE OBJECT.
	 * Here you should load the default texture ({@link #setDefaultTexture(TextureRegion)}) and
	 * any other assets required by this tile.
	 */
	public abstract void gameStart();
	
	/**
	 * Should return a instance of the {@link Tile} that this factory produces.
	 * @return A new, fresh copy of the tile that this factory produced.
	 */
	public abstract Tile getInstance();
	
	/**
	 * Gets the name of this factory, which is the name of the tile it produces.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the plugin that owns this factory.
	 */
	public FinalOutpostPlugin getPlugin(){
		return plugin;
	}
	
	/**
	 * Gets the plugin ID of this factory.
	 * @see {@link #getPlugin()} & {@link #getPluginName()}
	 */
	public String getPluginID(){
		return this.getPlugin().getWrapper().getPluginId();
	}

	/**
	 * Gets the display, user friendly name of this plugin.
	 */
	public String getPluginName(){
		return this.getPlugin().getDisplayName();
	}
	
	@Override
	public int compareTo(TileFactory o) {
		if(o == null)
			return 1;
		
		return o.getName().compareTo(this.getName());
	}	
}
