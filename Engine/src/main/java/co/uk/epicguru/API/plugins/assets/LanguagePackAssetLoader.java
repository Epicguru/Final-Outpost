package co.uk.epicguru.API.plugins.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import co.uk.epicguru.API.plugins.assets.LanguagePackAssetLoader.LPP;
import co.uk.epicguru.languages.utils.LanguagePack;

public class LanguagePackAssetLoader extends AsynchronousAssetLoader<LanguagePack, LPP> {

	public LanguagePackAssetLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	public void loadAsync(AssetManager manager, String fileName, FileHandle file, LPP parameter) {
		
	}

	public LanguagePack loadSync(AssetManager manager, String fileName, FileHandle file, LPP parameter) {
		return new LanguagePack(file.nameWithoutExtension(), file);
	}

	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, LPP parameter) {
		return null;
	}
	
	static public class LPP extends AssetLoaderParameters<LanguagePack>{ }
}
