package io.github.charlespockert.config;

import java.nio.file.Path;

import org.spongepowered.api.config.ConfigDir;
import com.google.inject.Inject;

import io.github.charlespockert.assets.AssetManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigurationManager {

	private ConfigurationLoader<CommentedConfigurationNode> sqlConfigLoader;
	private HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();

	@Inject
	public ConfigurationManager(AssetManager assetManager, @ConfigDir(sharedRoot = false) Path configDir) throws Exception {
		// Load configuration files
		if(configDir == null)
			throw new Exception("Config dir null");
		
		if(assetManager == null)
			throw new Exception("Asset mgr null");
		
		Path sqlConfigFile = configDir.resolve("sql.conf");

		if(!sqlConfigFile.toFile().exists()) {
			builder.setURL(assetManager.getURL("sql.conf"));
		} else {
			builder.setPath(sqlConfigFile);
		}

		sqlConfigLoader = builder.build();		
	}
	
	public SqlConfiguration loadSqlConfiguration() throws Exception {	
		return new SqlConfiguration(sqlConfigLoader.load());
	}
	
}
