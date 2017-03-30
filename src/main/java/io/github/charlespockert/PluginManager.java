package io.github.charlespockert;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;

import com.google.inject.Injector;

import io.github.charlespockert.commands.CommandBuilder;
import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.DatabaseManager;
import io.github.charlespockert.events.EventManager;

public class PluginManager implements PluginLifecycle {

	private Injector injector;
	private Logger logger;
	private boolean frozen = false;

	private Set<PluginLifecycle> components;

	public PluginManager(Injector injector) {
		this.injector = injector;
	}

	private void populatePlugins(Injector injector) {
		
		// Could use Guice Multibindings here but why bother :D
		// Order is important so we are using LinkedHashSet.
		components = new LinkedHashSet<PluginLifecycle>();

		// Manager registers the DB so that must be first, database next then the rest
		components.add((PluginLifecycle) injector.getInstance(ConfigurationManager.class));
		components.add((PluginLifecycle) injector.getInstance(ConnectionManager.class));
		components.add((PluginLifecycle) injector.getInstance(DatabaseManager.class));
		components.add((PluginLifecycle) injector.getInstance(EventManager.class));
		components.add((PluginLifecycle) injector.getInstance(CommandBuilder.class));
	}

	@Override
	public void freeze() {
		if(!frozen){
			for(PluginLifecycle component : components) {
				component.freeze();
			}	
		}
	}

	@Override
	public void unfreeze() {
		if(!frozen){
			for(PluginLifecycle component : components) {
				component.unfreeze();
			}	
		}
	}

	@Override
	public void start() throws Exception {
		logger = injector.getInstance(Logger.class);
		populatePlugins(injector);

		try {
			// Check that an economy service is present
			Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);
			if(!service.isPresent()) {
				logger.error("Dragon Business could not start - there was no economy plugin installed. Dragon Business requires an economy plugin.");
				return;
			}

			for(PluginLifecycle component : components) {
				component.start();
			}
		}
		catch(Exception e) {
			logger.error("Fatal error starting plugin: " + e.getMessage());
			throw e;
		}
	}

	@Override
	public void shutdown() {
		try {
			for(PluginLifecycle component : components) {
				component.shutdown();
			}
		} catch(Exception e) {
			logger.error("Failed to gracefully shutdown, your database might explode?");
		}
	}
}
