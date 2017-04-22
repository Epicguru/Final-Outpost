package co.uk.epicguru.API.plugins.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class NinePatchAssetLoader extends AsynchronousAssetLoader<NinePatch, NinePatchAssetLoader.NPP> {

	@SuppressWarnings("rawtypes")
	private static Array<AssetDescriptor> array = new Array<AssetDescriptor>();
	
	public NinePatchAssetLoader(FileHandleResolver resolver) {
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
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, NPP parameter) {
		
		FileHandle handle = new FileHandle(Gdx.files.getExternalStoragePath() + getAtlasPath(fileName));
		AssetDescriptor desc = new AssetDescriptor(handle, TextureAtlas.class);
		
		array.clear();
		array.add(desc);
		
		return array;
	}
	
	static public class NPP extends AssetLoaderParameters<NinePatch>{ }

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, NPP parameter) {
		
	}

	@Override
	public NinePatch loadSync(AssetManager manager, String fileName, FileHandle file, NPP parameter) {
		String path = Gdx.files.getExternalStoragePath().replace('\\', '/') + getAtlasPath(fileName);
		String assets = getAssetsPath(fileName);
		
		return manager.get(path, TextureAtlas.class).createPatch(fileName.substring(assets.length(), fileName.length() - ".9.png".length()));
	}
}
