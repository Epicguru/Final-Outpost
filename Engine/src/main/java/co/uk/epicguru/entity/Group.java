package co.uk.epicguru.entity;

/**
 * Both a utility class and a way to represent groups of entities that share the same {@link Component}s.
 * @author James Billy
 *
 */
public class Group {

	@SafeVarargs
	/**
	 * Gets a new Group object were all entities have those components.
	 * @return The new Group object. See {@link Engine} on how to use this Group.
	 */
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
