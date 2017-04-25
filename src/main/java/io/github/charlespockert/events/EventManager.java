package io.github.charlespockert.events;

import java.math.BigDecimal;
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
import io.github.charlespockert.business.PaymentManagement;

@Singleton
public class EventManager implements PluginLifecycle {

	private Logger logger;

	private DragonBusinessPlugin plugin;

	private PaymentManagement paymentManagement;

	@Inject
	public EventManager(DragonBusinessPlugin plugin, Logger logger, PaymentManagement paymentManagement) {
		this.logger = logger;
		this.plugin = plugin;
		this.paymentManagement = paymentManagement;
	}

	@Listener
	public void ChangeBlockEvent(ChangeBlockEvent.Break event) {

		Cause cause = event.getCause();

		// If a player broke a block
		Optional<Player> playerOpt = cause.first(Player.class);

		if (playerOpt.isPresent()) {
			logger.info(String.format("A player %s broke a block and earned some cash", playerOpt.get().getName()));
			try {
				paymentManagement.depositToCompany(playerOpt.get().getUniqueId(), new BigDecimal(50));
			} catch (Exception e) {
				logger.warn("Could not action ChangeBlockEvent: " + e.getMessage());
				e.printStackTrace();
			}
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
