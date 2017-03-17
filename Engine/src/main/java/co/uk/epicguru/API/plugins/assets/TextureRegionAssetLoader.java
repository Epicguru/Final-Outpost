package co.uk.epicguru.API.plugins.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TextureRegionAssetLoader extends SynchronousAssetLoader<TextureRegion, TextureRegionAssetLoader.TRP> {

	@SuppressWarnings("rawtypes")
	private static Array<AssetDescriptor> array = new Array<AssetDescriptor>();
	
	public TextureRegionAssetLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	private String getAtlasPath(String fileName){
		String ext = "Extracted/";
		String path = fileName.substring(0, fileName.indexOf(ext)) + ext;
		String otherPart = fileName.substring(fileName.indexOf(ext)).substring(ext.length());
		String pluginName = otherPart.substring(0, otherPart.indexOf('/'));
		
		String packedPath = path + pluginName + "/Packed/Textures.atlas";
		return packedPath;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TRP parameter) {
		
		
		
		array.clear();
		array.add(new AssetDescriptor(new FileHandle(Gdx.files.getExternalStoragePath() + getAtlasPath(fileName)), TextureAtlas.class));
		
		return TextureRegionAssetLoader.array;
	}
	
	static public class TRP extends AssetLoaderParameters<TextureRegion>{
		
	}

	@Override
	public TextureRegion load(AssetManager assetManager, String fileName, FileHandle file, TRP parameter) {
		// TADA
		return assetManager.get(getAtlasPath(fileName), TextureAtlas.class).findRegion(file.nameWithoutExtension());
	}
}
