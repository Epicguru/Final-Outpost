package co.uk.epicguru.input;

import com.badlogic.gdx.Input.Keys;

import co.uk.epicguru.IO.Dataset;

public class KeysIODataset extends Dataset<String> {

	public KeysDataset toRealDataset(){
		
		KeysDataset d = new KeysDataset();
		
		for(String key : super.keys()){
			d.put(key, Keys.valueOf(super.get(key)));
		}
		
		return d;		
	}
	
}
