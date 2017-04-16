package co.uk.epicguru.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;

/**
 * An object that populates the game world that contains components. Examples include a player, an enemy, a projectile or even a particle.
 * All entities contain {@link Components}, which are simply holders of information. This allows entities to be grouped
 * using {@link Group}s to manipulate all entities that share characteristics. Also see {@link PhysicalEntity} for a physics
 * based implementation of entity. This class is abstract because it is meant to be overridden to make use of methods such as 
 * {@link #update(float)} and {@link #render(Batch, float)}.
 * Please note that most of FOE's entity system is based almost entirely of Ashley, but is fine tuned for FOE. Thanks Ashley!
 * Find Ashley on Github for more info.
 * @author James Billy
 */
public class Entity extends Base{

	private String name;
	private ArrayList<Component> components = new ArrayList<Component>();
	private Component[] componentsArray = new Component[0];
	
	/**
	 * Creates a new Entity given a name. The name can be anything you want, but preferably will be user friendly.
	 * @param name The name of this entity, can be set using {@link #setName(String)}.
	 */
	public Entity(String name){
		this.setName(name);
	}
	
	/**
	 * Sets the name of this entity.
	 * @param name The new name of this entity. If this is null, the name will be set to 'Null-Name' (without quotation marks, duh).
	 * @see {@link #getName()} to get the name of an entity.
	 */
	public void setName(String name){
		if(name == null){
			this.name = "Null-Name";
			return;
		}
		this.name = name;
	}
	
	/**
	 * Gets the name of this entity. It will never be null.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets a component within this entity, given the class of that component. When you access a component using this method, it will
	 * automatically be sorted to the top of the component list, meaning that subsequent calls to the same component will be faster. To
	 * disable this feature use {@link #getComponent(Class, boolean)}.
	 * If the component does not exist on this entity, null will be returned.
	 * @param component The class of the component to get. For example to get the position component, use <code>getComponent(Position.class);</code>.
	 * @return The component of the class specified, no casting required, or null if the component does not exist.
	 * @see
	 * <li> {@link #addComponents(Component...)} to add one or more components and
	 * <li> {@link #removeComponent(Class)}
	 */
	public <E extends Component> E getComponent(Class<? extends E> component){
		return (E)this.getComponent(component, true);
	}
	
	/**
	 * See {@link #getComponent(Class)} for full documentation.
	 * @param component The component class to look for.
	 * @param sort If true, will be sorted if component is found, if false, no changes to array will be made.
	 * @return The component found.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Component> E getComponent(Class<? extends E> component, boolean sort){
		Component found = null;
		int index = 0;
		int fI = -1;
		for(Component c : this.components){
			if(c.getClass() == component){
				found = c;
				fI = index;
			}
			index++;
		}
		
		if(found != null && sort){
			this.components.remove(fI);
			this.components.add(0, found);
		}
		
		return (E)found;
	}
	
	/**
	 * Removes one or more components that exist in this entity. If one or more does not exist within the entity, this will return false.
	 * @param components The components to remove. If any are null they are ignored.
	 * @return True if ALL were removed successfully.
	 */
	@SuppressWarnings("unchecked")
	public boolean removeComponents(Class<? extends Component>... components){
		boolean allGood = true;
		for(Class<? extends Component> search : components){
			
			if(search == null)
				continue;
			
			int index = 0;
			int fI = -1;
			
			for(Component c : this.components){
				if(c.getClass() == search){
					fI = index;
				}
				index++;
			}
			
			if(fI != -1){
				this.components.remove(fI);
			}else{
				allGood = false;
			}
		}
		
		return allGood;
	}
	
	/**
	 * Adds one or more components to this entity. If a component of the same class already exists on this entity, the old one is removed
	 * and replaced with this new one. 
	 * @param components The components to add. If any are null they are ignored.
	 */
	public void addComponents(Component... components){
		
		for(Component c : components){
			
			if(c == null)
				continue;
			
			Component comp = this.getComponent(c.getClass(), false);
			if(comp == null){
				this.components.add(c);
			}else{
				this.components.remove(comp);
				this.components.add(0, c);
			}
		}
	}

	/**
	 * Gets an array of all component objects that exist on this entity. Please note that depending on the circumstances,
	 * a new array object may be created which may slow java down. Use this method only if necessary and chache the result to avoid
	 * a slow game!
	 */
	public Component[] getComponents(){
		
		if(componentsArray.length != components.size()){
			componentsArray = this.components.toArray(new Component[0]);
		}
		
		return componentsArray;
	}
	
	/**
	 * A more direct version of {@link #getComponents()} that always creates a new array object.
	 */
	public Component[] getComponentsOrdered(){
		return this.components.toArray(new Component[0]);
	}
	
	/**
	 * Called when this entity is added to the world, and is ready for use.
	 * By default does nothing.
	 */
	public void added(){
		// Callback
	}
	
	/**
	 * Called when this entity is removed from the world. See {@link Engine}.
	 */
	public void removed(){
		// Callback
	}
	
	/**
	 * Called once per frame when this entity is active, before {@link #render(Batch, float)}. 
	 * Please use this to do all logic and movement.
	 * @param delta The delta-time value.
	 */
	public void update(float delta){
		// NONE
	}
	
	/**
	 * Called once per frame when this entity is active, before {@link #renderUI(Batch, float)}. 
	 * Use this to render (display) your entity.
	 * @param batch The batch to draw with.
	 * @param delta The delta-time value.
	 */
	public void render(Batch batch, float delta){
		// NONE
	}
	
	/**
	 * Called once per frame when this entity is active. Use this to render UI elements, because
	 * in this method one unit is equal to one pixel, unlike in the {@link #render(Batch, float)} method.
	 * @param batch The batch to draw with.
	 * @param delta The delta-time value.
	 */
	public void renderUI(Batch batch, float delta){
		// NONE
	}
	
	/**
	 * Simply gets all components and calls .toString() on all of them and returns the final string.
	 * This creates a new StringBuilder every call.
	 */
	public String getComponentDetails(){
		StringBuilder s = new StringBuilder();
		for(Component c : this.components){
			s.append(c.toString());
			s.append('\n');
		}
		
		return s.toString();
	}
	
	/**
	 * Equivalent to {@link #getName()}.
	 */
	public String toString(){
		return this.getName();
	}
}
