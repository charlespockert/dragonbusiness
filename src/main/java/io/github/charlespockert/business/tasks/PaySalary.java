package io.github.charlespockert.business.tasks;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.spongepowered.api.scheduler.Task;
import com.google.inject.Inject;

import io.github.charlespockert.business.PaymentManagement;

public class PaySalary implements Consumer<Task> {

	PaymentManagement paymentManagement;

	Logger logger;

	@Inject
	public PaySalary(PaymentManagement paymentManagement, Logger logger) {
		this.paymentManagement = paymentManagement;
		this.logger = logger;
	}

	@Override
	public void accept(Task arg0) {
		try {
			logger.info("Attempting to pay salaries to all players");

			paymentManagement.payAllSalaries();
		} catch (Exception e) {
			logger.error("Failed to pay salaries to all players: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
