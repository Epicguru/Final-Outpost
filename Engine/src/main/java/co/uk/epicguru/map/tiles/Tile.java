package co.uk.epicguru.map.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.map.TiledMap;

public abstract class Tile extends Base {
	
	private TileFactory parent;
	private TiledMap map;
	private int x, y;
	
	public TileFactory getParent(){
		return parent;
	}
	
	public String getName(){
		return parent.getName();
	}
	
	public FinalOutpostPlugin getPlugin(){
		return parent.getPlugin();
	}
	
	public TiledMap getMap(){	
		return map;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setPosition(int x, int y){
		map.setTile(this, x, y);
		this.x = x;
		this.y = y;
	}

	public void update(float delta){
		
	}
	
	public void render(Batch batch){
		
	}
}
