package co.uk.epicguru.entity.quadtree;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

import co.uk.epicguru.entity.Entity;

public class Quadtree {
	
	private int maxLevel;
	private int level;
	private int maxEntities;
	private Rectangle rect;
	private ArrayList<Entity> entities;
	private Quadtree[] branches;
	
	public Quadtree(int level, Rectangle rect, ArrayList<Entity> entities){

		maxLevel = 4;
		this.level = level;
		this.rect = rect;
		this.entities = entities;
	}
	
	public Rectangle getRect(){
		// OHOHOHOHOHOH! MUM GET THE CAMERA!!!
		
		return this.rect;
	}
	
	public void subdivide(){		
		int i = 0;
		for(Rectangle r : splitQuad()){
			Quadtree b = new Quadtree(this.level + 1, r, new ArrayList<Entity>());
			this.branches[i++] = b;
		}		
	}
	
	public void subdivideEntities(){
		
		for(Entity e : entities){
			for(Quadtree q : branches){
				if(q.getRect().contains(e.))
			}
		}
		
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public Rectangle[] splitQuad(){
		float width = this.getRect().getWidth() / 2f;
		float height = this.getRect().getHeight() / 2f;
		
		Rectangle[] rects = new Rectangle[4];
		
		rects[0] = new Rectangle(rect.x, rect.y, width, height);
		rects[1] = new Rectangle(rect.x + width, rect.y, width, height);
		rects[2] = new Rectangle(rect.x, rect.y + height, width, height);
		rects[3] = new Rectangle(rect.x + width, rect.y + height, width, height);
		
		return rects;
	}
	
	
}
