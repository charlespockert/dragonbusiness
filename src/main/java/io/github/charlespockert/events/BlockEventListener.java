package io.github.charlespockert.events;

import org.slf4j.Logger;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

import com.google.inject.Inject;

public class BlockEventListener implements EventListener<ChangeBlockEvent> {

	@Inject
	private Logger logger;
	
	@Override
	public void handle(ChangeBlockEvent event) throws Exception {
		logger.info("block event in dragon " + event.getCause().toString());
	}
}
