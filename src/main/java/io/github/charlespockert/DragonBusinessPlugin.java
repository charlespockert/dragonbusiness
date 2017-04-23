package io.github.charlespockert;

import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;

@Plugin(id = "dragonbusiness", name = "Dragon Business", version = "0.1")
public class DragonBusinessPlugin {

	private Logger logger;

	private PluginManager pluginManager;

	private Injector injector;

	@Inject
	public DragonBusinessPlugin(Injector injector, Logger logger) {
		// Create child injector
		this.injector = injector;
		this.logger = logger;
	}

	@Listener
	public void onServerPreInit(GamePreInitializationEvent event) throws Exception {
		this.logger.info("Dragon Business is starting");
		Injector childInjector = injector.createChildInjector(new DragonBusinessModule());
		pluginManager = new PluginManager(childInjector);
	}

	@Listener
	public void onServerStarting(GameStartingServerEvent event) throws Exception {
		this.logger.info("Dragon Business is finalising start-up");
		pluginManager.start();
	}

	public void onServerStopping(GameStoppingServerEvent event) {
		this.logger.info("Dragon Business is shutting down");
		pluginManager.shutdown();
	}
}
