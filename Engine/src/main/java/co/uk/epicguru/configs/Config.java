package co.uk.epicguru.configs;

import co.uk.epicguru.IO.JLineReader;

public abstract class Config {

	public abstract void load(JLineReader reader);
	public abstract void save(JLineReader reader);
	
}
