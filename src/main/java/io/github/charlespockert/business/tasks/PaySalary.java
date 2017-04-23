package io.github.charlespockert.business.tasks;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import com.google.inject.Inject;

import io.github.charlespockert.business.CompanyManagement;
import io.github.charlespockert.business.PaymentManagement;
import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.BusinessRepository;
import io.github.charlespockert.data.CompanyUtil;
import io.github.charlespockert.entities.Company;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.formatting.Formatter;

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
			logger.error("Failed to pay salaries to all players");
			e.printStackTrace();
		}
	}
}
