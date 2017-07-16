package co.uk.epicguru.configs;

import java.io.File;
import java.util.HashMap;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.Dataset;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.main.FOE;

/**
 * A config local to one plugin that is saved and loaded using the IO system.
 */
public class Config extends Dataset<Object> {
	
	private String name;
	
	public Config(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
}
