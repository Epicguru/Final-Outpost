package co.uk.epicguru.input;

import com.badlogic.gdx.Input.Keys;

import co.uk.epicguru.IO.Dataset;

public class KeysDataset extends Dataset<Integer> {

	public KeysIODataset toIODataset(){
		
		KeysIODataset d = new KeysIODataset();
		
		for(String key : super.keys()){
			d.put(key, Keys.toString(super.get(key)));
		}
		
		return d;		
	}
	
}
