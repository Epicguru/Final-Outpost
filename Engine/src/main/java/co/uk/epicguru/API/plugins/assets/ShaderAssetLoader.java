package co.uk.epicguru.API.plugins.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.assets.ShaderAssetLoader.SPP;
import co.uk.epicguru.shaders.Shaders;

public class ShaderAssetLoader extends AsynchronousAssetLoader<ShaderProgram, SPP> {

	public ShaderAssetLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, SPP parameter) {
		
	}

	@Override
	public ShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, SPP parameter) {
		ShaderProgram s = Shaders.load(file.file().getName(), U.normalize(Gdx.files.getExternalStoragePath() + fileName));
		if(s == null){
			throw new IllegalStateException("Error loading shader, exiting...");
		}
		return s;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SPP parameter) {
		return null;
	}
	
	static public class SPP extends AssetLoaderParameters<ShaderProgram>{ }

}
