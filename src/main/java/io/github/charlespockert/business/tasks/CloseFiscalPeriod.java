package io.github.charlespockert.business.tasks;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;

import com.google.inject.Inject;

import io.github.charlespockert.business.CompanyManagement;
import io.github.charlespockert.business.PeriodManagement;

public class CloseFiscalPeriod implements Consumer<Task> {

	Logger logger;

	CompanyManagement companyManagement;

	PeriodManagement periodManagement;

	@Inject
	public CloseFiscalPeriod(Logger logger, CompanyManagement companyManagement, PeriodManagement periodManagement) {
		this.logger = logger;
		this.companyManagement = companyManagement;
		this.periodManagement = periodManagement;
	}

	@Override
	public void accept(Task arg0) {
		// Closes a fiscal period by marking it as closed and opening a new
		// period. This also marks any businesses with a negative balance as
		// bankrupt.

		try {
			logger.info("Attempting to close the current fiscal period");
			try {
				companyManagement.bankruptCompanies();
			} catch (Exception e) {
				logger.warn("Failed to bankrupt all companies: " + e.getMessage());
			}

			try {
				periodManagement.closePeriod();
			} catch (Exception e) {
				logger.error("Failed to close the current fiscal period: " + e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Failed to pay salaries to all players: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
