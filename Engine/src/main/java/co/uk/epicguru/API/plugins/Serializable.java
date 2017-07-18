package co.uk.epicguru.API.plugins;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ro.fortsoft.pf4j.Extension;

/**
 * When added to a class declaration, indicates that this class can be used with JSON serialization.
 * For this to work, you MUST pair this with the {@link Extension} annotation.
 * @author James Billy
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Serializable {
	
}
