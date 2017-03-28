package io.github.charlespockert;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import com.google.inject.Injector;

import io.github.charlespockert.commands.CommandBuilder;
import io.github.charlespockert.data.DatabaseManager;
import io.github.charlespockert.data.EmployeeRepository;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.events.EventManager;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;


@Plugin(id = "dragonbusiness", name = "Dragon Business", version = "0.1")
public class DragonBusinessPlugin {

	@Inject
	private Logger logger;

	private PluginManager pluginManager;
	
	@Inject
	public DragonBusinessPlugin(Injector injector) {
		// Create child injector
		Injector childInjector = injector.createChildInjector(new DragonBusinessModule());
		pluginManager = new PluginManager(childInjector);
	}

	@Listener
	public void onServerStarting(GameStartingServerEvent event) throws Exception {
		this.logger.info("Dragon Business is starting up");
		pluginManager.startPlugin();
	}

	public void onServerStopping(GameStoppingServerEvent event) {
		this.logger.info("Dragon Business is shutting down");
		pluginManager.stopPlugin();
	}
}