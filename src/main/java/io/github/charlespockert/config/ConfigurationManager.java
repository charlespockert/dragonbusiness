package io.github.charlespockert.config;

import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.PluginLifecycle;
import io.github.charlespockert.assets.AssetGrabber;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMapper.BoundInstance;

@Singleton
public class ConfigurationManager implements PluginLifecycle {

	private Logger logger;

	private Path configDirectory;

	private MessagesConfig messagesConfig;

	private MainConfig mainConfig;

	private HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();

	@Inject
	public ConfigurationManager(@ConfigDir(sharedRoot = false) Path configDirectory, AssetGrabber assetGrabber,
			Logger logger, MessagesConfig messagesConfig, MainConfig mainConfig) {
		this.configDirectory = configDirectory;
		this.logger = logger;
		this.messagesConfig = messagesConfig;
		this.mainConfig = mainConfig;
	}

	public <T> void deserialise(T instance, String fileName) throws Exception {

		Path filePath = configDirectory.resolve(fileName);

		// Create a mapper to bind to the instance
		ObjectMapper<T> mapper = ObjectMapper.forClass((Class) instance.getClass());
		builder.setPath(filePath);
		ConfigurationLoader<CommentedConfigurationNode> loader = builder.build();

		logger.info("Attempting to load config file: " + fileName);

		BoundInstance bound = mapper.bind(instance);

		// Ensure the config exists
		if (!Files.exists(filePath)) {
			logger.info("Config file did not exist, generating default config");
			// Files.createDirectories(filePath);
			CommentedConfigurationNode node = loader.createEmptyNode();
			bound.serialize(node);
			loader.save(node);
		} else {
			bound.populate(loader.load());
		}
	}

	public <T> void serialize(T instance, String fileName) throws Exception {
		Path filePath = configDirectory.resolve(fileName);

		logger.info("Saving config to: " + filePath);

		builder.setPath(filePath);
		ConfigurationLoader<CommentedConfigurationNode> loader = builder.build();

		ObjectMapper<T> mapper = ObjectMapper.forClass((Class) instance.getClass());
		CommentedConfigurationNode node = loader.createEmptyNode();
		mapper.bind(instance).serialize(node);
		loader.save(node);
	}

	@Override
	public void start() throws Exception {
		// Load configuration files into their containers
		deserialise(messagesConfig, "messages.conf");
		deserialise(mainConfig, "dragonbusiness.conf");
	}

	@Override
	public void shutdown() throws Exception {
		// Save any configuration changes
		serialize(messagesConfig, "messages.conf");
		serialize(mainConfig, "dragonbusiness.conf");
	}

	@Override
	public void freeze() {
	}

	@Override
	public void unfreeze() {
	}

}
