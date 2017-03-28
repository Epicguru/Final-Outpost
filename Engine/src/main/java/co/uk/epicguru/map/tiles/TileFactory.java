package co.uk.epicguru.map.tiles;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;

/**
 * Represents a tile factory that pumps out {@link Tile}. 
 * @author James Billy
 */
public abstract class TileFactory extends Base implements Comparable<TileFactory>{

	private String name;
	private FinalOutpostPlugin plugin;
	
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
