package co.uk.epicguru.map.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.map.TiledMap;

public abstract class Tile extends Base {
	
	private TileFactory parent;
	private TiledMap map;
	private int x, y;
	private Vector2 centre;
	
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
		map.setTile(this, x, y);
		this.x = x;
		this.y = y;
	}

	public void update(float delta){
		
	}
	
	public void render(Batch batch){
		
	}
}
