package co.uk.epicguru.entity;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.plugins.Serializable;

/**
 * The base class that all entity components extend. This class is empty as serves only as a starting point.
 * REMEMBER - Just like entities, components MUST have a no-args constructor, for serialization.
 * @author James Billy
 */
@Serializable
public abstract class Component extends Base {

	public void loaded(){
		
	}
	
}
