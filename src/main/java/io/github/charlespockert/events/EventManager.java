package io.github.charlespockert.events;

import java.util.HashMap;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventListener;

import com.google.inject.Inject;

import io.github.charlespockert.DragonBusinessPlugin;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventManager {
	
	private HashMap<Class, EventListener> map;
	
	@Inject 
	private Logger logger;
	
	@Inject
	private DragonBusinessPlugin plugin;
	
	@Inject
	public EventManager(BlockEventListener blockEventListener) {
		map = new HashMap<Class, EventListener>();

		map.put(BlockEventListener.class, blockEventListener);
	}
	
	public void registerEvents() {
		// Register all events
		for (Class key : map.keySet() ) {
			Sponge.getEventManager().registerListener(plugin, key, map.get(key));
			logger.info("Registered events on " + key.getName());
		}
	}

	public void unregisterEvents() {
		// Register all events
		for (Class key : map.keySet() ) {
			Sponge.getEventManager().unregisterListeners(map.get(key));
		}
	}
}
