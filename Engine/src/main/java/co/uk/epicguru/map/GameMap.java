package co.uk.epicguru.map;

import com.badlogic.gdx.graphics.OrthographicCamera;

import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.tiles.Tile;

public class GameMap extends TiledMap {

	private int renderCalls = 0;
	private int renderLoops = 0;
	
	public GameMap(int width, int height) {
		super(width, height);
	}
	
	public int getRenderCalls(){
		return renderCalls;
	}
	
	public int getRenderLoops(){
		return renderLoops;
	}
	
	public void render(){
		
		renderCalls = 0;
		renderLoops = 0;
		
		// Get camera
		OrthographicCamera camera = FOE.camera;
		
		float x = camera.position.x - camera.viewportWidth / 2f;
		x -= 2;	
		float y = camera.position.y - camera.viewportHeight / 2f;
		y -= 2;
		
		float width = camera.viewportWidth + 4;
		float height = camera.viewportHeight + 4;
		
		// Cast variables
		int X = (int) x;
		int Y = (int) y;
		int Width = (int) width;
		int Height = (int) height;
		
		for(int i = X; i < X + Width; i++){	
			for(int j = Y; j < Y + Height; j++){
				renderLoops++;
				Tile tile = getTile(i, j);
				if(tile == null)
					continue;
				renderCalls++;
				
				tile.render(FOE.batch);
			}
		}
	}
}
