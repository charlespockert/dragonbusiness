package io.github.charlespockert.events;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.cause.Cause;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.PluginLifecycle;

@Singleton
public class EventManager implements PluginLifecycle {

	@Inject
	private Logger logger;

	@Inject
	private DragonBusinessPlugin plugin;

	@Listener
	public void ChangeBlockEvent(ChangeBlockEvent.Break event) {

		Cause cause = event.getCause();

		// If a player broke a block
		Optional<Player> playerOpt = cause.first(Player.class);

		if (playerOpt.isPresent()) {
			logger.info(String.format("A player %s broke a block", playerOpt.get().getName()));
		}
	}

	public void registerEvents() {
		Sponge.getEventManager().registerListeners(plugin, this);
	}

	public void unregisterEvents() {
		Sponge.getEventManager().unregisterListeners(this);
	}

	@Override
	public void start() throws Exception {
		registerEvents();
	}

	@Override
	public void shutdown() throws Exception {

	}

	@Override
	public void freeze() {
		unregisterEvents();
	}

	@Override
	public void unfreeze() {
		registerEvents();
	}
}
