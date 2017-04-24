package io.github.charlespockert.business.tasks;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;

import com.google.inject.Inject;

import io.github.charlespockert.data.dao.DaoContainer;

public class CloseFiscalPeriod implements Consumer<Task> {

	Logger logger;

	DaoContainer daoContainer;

	@Inject
	public CloseFiscalPeriod(Logger logger, DaoContainer daoContainer) {
		this.logger = logger;
		this.daoContainer = daoContainer;
	}

	@Override
	public void accept(Task arg0) {
		// Closes a fiscal period by marking it as closed and opening a new
		// period. This also marks any businesses with a negative balance as
		// bankrupt.

		logger.info("Attempting to close the current fiscal period");

		// First create a new open period
		// businessRepository.periodUpdate();
	}

}
