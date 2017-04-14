package co.uk.epicguru.tiles.instances;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import ro.fortsoft.pf4j.Extension;

@Extension
public class Stone extends Tile {
	
	private Body body;
	
	public Stone(TileFactory parent) {
		super(parent);
	}
	
	public void added(){
		// Create body
		BodyDef def = FOE.engine.newBodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(getX(), getY());
		
		PolygonShape shape = FOE.engine.boxOfSize(1, 1);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		
		Body b = FOE.engine.getWorld().createBody(def);
		b.createFixture(fixture);
		
		this.body = b;
	}
	
	public void removed(){
		FOE.engine.removeBody(body, null);
	}
	
	public void render(Batch batch){
		batch.draw(getParent().getDefaultTexture(), getX(), getY(), ONE, ONE);
	}
}
