package io.github.charlespockert.assets;

import java.net.URL;
import java.util.Optional;

import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import com.google.inject.Inject;

public class AssetGrabber {

	private PluginContainer pluginContainer;

	@Inject
	public AssetGrabber(PluginContainer pluginContainer) {
		this.pluginContainer = pluginContainer;
	}

	public String getTextFile(String fileName) throws Exception {
		Optional<Asset> assetOpt = pluginContainer.getAsset(fileName);

		if (!assetOpt.isPresent()) {
			throw new Exception(String.format("Asset file %s not found", fileName));
		}

		Asset asset = assetOpt.get();
		return asset.readString();
	}

	public URL getURL(String fileName) throws Exception {
		Optional<Asset> assetOpt = pluginContainer.getAsset(fileName);

		if (!assetOpt.isPresent()) {
			throw new Exception(String.format("Asset file %s not found", fileName));
		}

		Asset asset = assetOpt.get();
		return asset.getUrl();

	}
}
