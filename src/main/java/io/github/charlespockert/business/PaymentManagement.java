package io.github.charlespockert.business;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.BusinessRepository;
import io.github.charlespockert.data.CompanyUtil;
import io.github.charlespockert.entities.Company;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.formatting.Formatter;

@Singleton
public class PaymentManagement {

	private DragonBusinessPlugin plugin;

	private Server server;

	private BusinessRepository businessRepository;

	private EconomyService economyService;

	private MainConfig mainConfig;

	private Logger logger;

	private Formatter formatter;

	private MessagesConfig messagesConfig;

	private CompanyManagement companyManagement;

	public PaymentManagement(Server server, BusinessRepository businessRepository, EconomyService economyService,
			MainConfig mainConfig, Logger logger, Formatter formatter, MessagesConfig messagesConfig,
			CompanyManagement companyManagement, DragonBusinessPlugin plugin) {
		this.server = server;
		this.businessRepository = businessRepository;
		this.economyService = economyService;
		this.mainConfig = mainConfig;
		this.logger = logger;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
		this.companyManagement = companyManagement;
		this.plugin = plugin;
	}

	public void payAllSalaries() throws Exception {
		// Get all online players
		for (Player player : server.getOnlinePlayers()) {

			// Check if the player is employed
			Employee employee = businessRepository.employeeGet(player.getUniqueId());

			// Skip if not employed
			if (employee == null)
				continue;

			Company company = businessRepository.companyGetByEmployeeId(employee.getUuid());
			int salary = mainConfig.business.salary_amount;

			ResultType withdrawResult = withdrawFromCompany(employee.getCompanyId(), salary);

			if (withdrawResult == ResultType.SUCCESS) {
				ResultType depositResult = depositToEmployee(employee.getUuid(), salary);

				if (depositResult == ResultType.SUCCESS) {
					player.sendMessage(
							formatter.formatText(mainConfig.business.salary_amount, messagesConfig.salary.salary_paid));
				} else {
					logger.warn("Error paying salary to %s: %s", employee.getName(), depositResult.toString());
					player.sendMessage(formatter.formatText(null, messagesConfig.salary.payment_issue));
				}
			} else {
				logger.info("Could not withdraw funds for salary from company %s: %s", company.getName(),
						withdrawResult.toString());
				player.sendMessage(formatter.formatText(null, messagesConfig.salary.company_insufficient_funds));

				// Bankrupt the company and exit
				companyManagement.bankrupt(employee.getCompanyId());
				return;
			}
		}
	}

	public void payAllDividends(int companyId) {

	}

	public ResultType withdrawFromCompany(int companyId, int amount) throws Exception {
		Company company = businessRepository.companyGet(companyId);

		String virtualAccountId = CompanyUtil.getAccountId(company.getName(),
				mainConfig.business.company_account_prefix);

		Optional<Account> accountOpt = economyService.getOrCreateAccount(virtualAccountId);

		// Not found or error creating..
		if (!accountOpt.isPresent()) {
			logger.warn("Failed to withdraw %d for company %s - the virtual account %s could not be located or created",
					amount, company.getName(), virtualAccountId);
			return ResultType.FAILED;
		}

		Account account = accountOpt.get();

		TransactionResult withdrawResult = account.withdraw(economyService.getDefaultCurrency(),
				new BigDecimal(mainConfig.business.salary_amount),
				Cause.source(this).named("company", company).build());

		return withdrawResult.getResult();
	}

	public ResultType depositToEmployee(UUID employeeId, int amount) throws Exception {

		Employee employee = businessRepository.employeeGet(employeeId);

		Optional<UniqueAccount> accountOpt = economyService.getOrCreateAccount(employeeId);

		// Not found or error creating..
		if (!accountOpt.isPresent()) {
			logger.warn("Failed to deposit %d for employee %s the unique account %s could not be located or created",
					amount, employee.getName(), employeeId);
			return ResultType.FAILED;
		}

		Account account = accountOpt.get();

		TransactionResult depositResult = account.deposit(economyService.getDefaultCurrency(),
				new BigDecimal(mainConfig.business.salary_amount),
				Cause.source(this).named("employee", employee).build());

		return depositResult.getResult();
	}

	// Bonuses/dividends

	public void payBonusToEmployee(float amount, int companyId, UUID player) {

	}

	public void paySalaryToEmployee(float amount, int companyId, UUID player) {

	}

	public void payDividend(float amount, int companyId, UUID player) {

	}
}
