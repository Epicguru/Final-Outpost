package co.uk.epicguru.map.tiles;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;

public abstract class TileFactory extends Base implements Comparable<TileFactory>{

	private String name;
	private FinalOutpostPlugin plugin;
	
	public TileFactory(FinalOutpostPlugin plugin, String name){
		this.plugin = plugin;
		this.name = name;
	}
	
	public abstract Tile getInstance();
	
	public String getName(){
		return name;
	}
	
	public FinalOutpostPlugin getPlugin(){
		return plugin;
	}
	
	public String getPluginID(){
		return plugin.getWrapper().getPluginId();
	}

	@Override
	public int compareTo(TileFactory o) {
		if(o == null)
			return 1;
		
		return o.getName().compareTo(this.getName());
	}	
}
