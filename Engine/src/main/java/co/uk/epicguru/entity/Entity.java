package co.uk.epicguru.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;

public class Entity extends Base{

	private String name;
	private ArrayList<Component> components = new ArrayList<Component>();
	private Component[] componentsArray = new Component[0];
	
	public Entity(String name){
		this.setName(name);
	}
	
	public void setName(String name){
		if(name == null){
			this.name = "Null-Name";
			return;
		}
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public <E extends Component> E getComponent(Class<? extends E> component){
		return (E)this.getComponent(component, true);
	}
	
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
	
	public boolean removeComponent(Class<? extends Component> component){
		int index = 0;
		int fI = -1;
		
		for(Component c : this.components){
			if(c.getClass() == component){
				fI = index;
			}
			index++;
		}
		
		if(fI != -1){
			this.components.remove(fI);
			return true;
		}else{
			return false;
		}
	}
	
	public void addComponents(Component... components){
		
		for(Component c : components){
			Component comp = this.getComponent(c.getClass(), false);
			if(comp == null){
				this.components.add(c);
			}else{
				this.components.remove(comp);
				this.components.add(0, c);
			}
		}
	}

	public Component[] getComponents(){
		
		if(componentsArray.length != components.size()){
			componentsArray = this.components.toArray(new Component[0]);
		}
		
		return componentsArray;
	}
	
	public Component[] getComponentsOrdered(){
		return this.components.toArray(new Component[0]);
	}
	
	public void added(){
		// Callback
	}
	
	public void removed(){
		// Callback
	}
	
	public void update(float delta){
		// NONE
	}
	
	public void render(Batch batch, float delta){
		// NONE
	}
	
	public void renderUI(Batch batch, float delta){
		// NONE
	}
	
	public String getComponentDetails(){
		StringBuilder s = new StringBuilder();
		for(Component c : this.components){
			s.append(c.toString());
			s.append('\n');
		}
		
		return s.toString();
	}
	
	public String toString(){
		return name;
	}
}
