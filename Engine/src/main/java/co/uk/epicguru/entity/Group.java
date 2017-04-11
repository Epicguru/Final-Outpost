package co.uk.epicguru.entity;

public class Group {

	@SafeVarargs
	public static Group of(Class<? extends Component>... classes){
		return new Group(classes);
	}
	
	private Class<? extends Component>[] classes;
	private Group(Class<? extends Component>[] classes){	
		this.classes = classes;
	}
	
	protected boolean hasAllComponents(Entity e){
		for(Class<? extends Component> clazz : this.classes){
			if(e.getComponent(clazz) == null)
				return false;
		}
		return true;
	}
	
	public boolean isOfGroup(Entity e){
		return this.hasAllComponents(e);
	}
}
