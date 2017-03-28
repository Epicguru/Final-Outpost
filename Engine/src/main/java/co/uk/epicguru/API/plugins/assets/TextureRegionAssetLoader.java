package co.uk.epicguru.API.plugins.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TextureRegionAssetLoader extends AsynchronousAssetLoader<TextureRegion, TextureRegionAssetLoader.TRP> {

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
	
	private String getAssetsPath(String fileName){
		String ext = "Extracted/";
		String path = fileName.substring(0, fileName.indexOf(ext)) + ext;
		String otherPart = fileName.substring(fileName.indexOf(ext)).substring(ext.length());
		String pluginName = otherPart.substring(0, otherPart.indexOf('/'));
		
		String packedPath = path + pluginName + "/assets/";
		return packedPath;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TRP parameter) {
		
		FileHandle handle = new FileHandle(Gdx.files.getExternalStoragePath() + getAtlasPath(fileName));
		AssetDescriptor desc = new AssetDescriptor(handle, TextureAtlas.class);
		
		array.clear();
		array.add(desc);
		
		return array;
	}
	
	static public class TRP extends AssetLoaderParameters<TextureRegion>{ }

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TRP parameter) {
		
	}

	@Override
	public TextureRegion loadSync(AssetManager manager, String fileName, FileHandle file, TRP parameter) {
		String path = Gdx.files.getExternalStoragePath().replace('\\', '/') + getAtlasPath(fileName);
		String assets = getAssetsPath(fileName);
		
		return manager.get(path, TextureAtlas.class).findRegion(fileName.substring(assets.length(), fileName.length() - ".png".length()));
	}
}
