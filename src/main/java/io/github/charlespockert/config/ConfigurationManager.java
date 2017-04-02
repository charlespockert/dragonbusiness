package io.github.charlespockert.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.PluginLifecycle;
import io.github.charlespockert.assets.AssetManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Singleton
public class ConfigurationManager implements PluginLifecycle {

	private HashMap<String, CommentedConfigurationNode> configurations;
	private HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();

	// Configuration section constants
	public static final String SQL_CONFIG = "sql";
	public static final String MESSAGES = "messages";

	@Inject
	private Logger logger;
	
	@Inject
	private AssetManager assetManager;

	@Inject
	@ConfigDir(sharedRoot = false) 
	private Path configDirectory;

	@Inject
	public ConfigurationManager() {		
		configurations = new HashMap<String, CommentedConfigurationNode>();
	}

	public void loadConfiguration(String key, String fileName) throws Exception {	
		Path filePath = configDirectory.resolve(fileName);
		
		boolean requiresSave = false;
		logger.info("Attempting to load " + key + " config");

		if(!filePath.toFile().exists()) {
			logger.info("No config found, using default config from jar");
			requiresSave = true;
			builder.setURL(assetManager.getURL(fileName));
		} else {
			builder.setPath(filePath);
		}

		ConfigurationLoader<CommentedConfigurationNode> loader = builder.build();		
		configurations.put(key, loader.load());
		
		// If we require a write back to the file system, save
		if(requiresSave) {
			saveConfiguration(key, fileName);
		}
	}

	public void saveConfiguration(String key, String fileName) throws IOException {
		Path filePath = configDirectory.resolve(fileName);

		logger.info("Setting file path on save to: " + filePath.toString());
		builder.setFile(filePath.toFile());
		builder.build().save(configurations.get(key));
	}

	public CommentedConfigurationNode getConfiguration(String key) {
		return configurations.get(key);
	}

	@Override
	public void start() throws Exception {
		loadConfiguration(SQL_CONFIG, "sql.conf");
		loadConfiguration(MESSAGES, "messages.conf");
	}

	@Override
	public void shutdown() throws Exception {
		// Save any configuration changes in case
		saveConfiguration(SQL_CONFIG, "sql.conf");
		saveConfiguration(MESSAGES, "messages.conf");
	}

	@Override
	public void freeze() {
	}

	@Override
	public void unfreeze() {
	}

}
