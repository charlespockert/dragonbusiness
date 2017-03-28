package io.github.charlespockert.assets;

import java.net.URL;
import java.util.Optional;

import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

public class AssetManager {

	@Inject 
	PluginContainer pluginContainer;
	
	public String getTextFile(String fileName) throws Exception {
		Optional<Asset> assetOpt = pluginContainer.getAsset(fileName);
		
		if(!assetOpt.isPresent()) {
			throw new Exception(String.format("Asset file %1 not found", fileName));
		}
		
		Asset asset = assetOpt.get();		
		return asset.readString();
	}
	
	public URL getURL(String fileName) throws Exception {
		Optional<Asset> assetOpt = pluginContainer.getAsset(fileName);
	
		if(!assetOpt.isPresent()) {
			throw new Exception(String.format("Asset file %1 not found", fileName));
		}
		
		Asset asset = assetOpt.get();		
		return asset.getUrl();
		
	}
}
