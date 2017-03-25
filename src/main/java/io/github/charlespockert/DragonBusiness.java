package io.github.charlespockert;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import com.google.inject.Inject;

import io.github.charlespockert.commands.CommandBuilder;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;


@Plugin(id = "dragonbusiness", name = "Dragon Business", version = "1.0")
public class DragonBusiness {

	@Inject
	private Logger logger;
	private EconomyService economyService;
	private CommandBuilder commandManager;

	public DragonBusiness() {
		this.commandManager = new CommandBuilder();
	}

	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
		if (event.getService().equals(EconomyService.class)) {
			economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
		}
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		// Hey! The server has started!
		// Try instantiating your logger in here.
		// (There's a guide for that)
		this.logger.info("Dragon Business has started");

		if(this.economyService == null) {
			this.logger.error("No economy plugin available, Dragon Business will not be activated");
		}
		else {
			this.commandManager.BuildCommands(this);
		}
	}

	@Listener
	public void onServerEnd(GameStoppingServerEvent event){

	}
}
