package co.uk.epicguru.shaders;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public class Shaders {

	private static final String TAG = "Shaders";
	private static HashMap<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();
	
	public static ShaderProgram load(String key, String shader){
		// Where path is something like:
		// .../Extracted/PluginName/assets/
		// and shader is:
		// Shaders/ShaderFolder
		
		// Note that there is no ending. However we will allow endings, which will be chopped off.
		
		String vertexName = null;
		String fragmentName = null;
		
		Log.info(TAG, "Loading " + shader);
		
		File[] files = new File(shader).listFiles();
		for(File f : files){
			if(f.isDirectory()){
				Log.error(TAG, "Unknown folder in shader folder : " + f.getName());
				continue;
			}
			
			if(f.getName().endsWith(FOE.vertexShaderExtension)){
				vertexName = f.getName();
				continue;
			}
			if(f.getName().endsWith(FOE.fragmentShaderExtension)){
				fragmentName = f.getName();
			}
		}
		
		if(fragmentName == null){
			Log.error(TAG, "Did not find fragment shader in shader folder! (" + shader + ")");
			return null;
		}
		if(vertexName == null){
			Log.error(TAG, "Did not find vertex shader in shader folder! (" + shader + ")");
			return null;
		}
		
		String vertex = shader + "\\" + vertexName;
		String fragment = shader + "\\" + fragmentName;
		
		File v = new File(vertex);
		File f = new File(fragment);
		
		String vShader = null;
		String fShader = null;
		
		try {
			vShader = FileUtils.readFileToString(v, Charset.defaultCharset());
			fShader = FileUtils.readFileToString(f, Charset.defaultCharset());
		} catch (IOException e) {
			Log.error(TAG, "Failed to load shader '" + shader + "!", e);
			return null;
		}
		
		ShaderProgram s = new ShaderProgram(vShader, fShader);
		
		if(!s.isCompiled()){
			Log.error(TAG, "Shader " + shader + " (" + key + ") failed to compile!");
			Log.error(TAG, "Error:\n" + s.getLog());
			return null;
		}
		
		boolean changed = false;
		while(shader.contains(key)){
			key += "*";
			changed = true;
		}
		if(changed){
			Log.error(TAG, "Key was already registered, saving as '" + key + "'. This WILL NOT affect getting the shader using Plugin.get(asset).");
		}
		shaders.put(key, s);
		
		return s;
	}
	
}
