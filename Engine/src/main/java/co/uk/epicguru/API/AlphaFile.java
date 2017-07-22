package co.uk.epicguru.API;

import java.io.File;
import java.net.URI;

public class AlphaFile extends File {

	private static final long serialVersionUID = -7787958989730457086L;

	public AlphaFile(File file){
		super(file.getAbsolutePath());
	}
	
	public AlphaFile(File parent, String child) {
		super(parent, child);
	}

	public AlphaFile(String parent, String child) {
		super(parent, child);
	}

	public AlphaFile(String pathname) {
		super(pathname);
	}

	public AlphaFile(URI uri) {
		super(uri);
	}
	
	public int compareTo(File other){
		if(other == null)
			return -1;
		return this.getName().compareTo(other.getName());
	}

}
