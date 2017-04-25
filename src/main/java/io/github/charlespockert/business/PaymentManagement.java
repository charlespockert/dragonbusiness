package io.github.charlespockert.business;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
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

import com.google.inject.Inject;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.CompanyUtil;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.TransactionType;
import io.github.charlespockert.formatting.Formatter;

@Singleton
public class PaymentManagement {

	private Server server;

	private DaoContainer daoContainer;

	private EconomyService economyService;

	private MainConfig mainConfig;

	private Logger logger;

	private Formatter formatter;

	private MessagesConfig messagesConfig;

	@Inject
	public PaymentManagement(Server server, DaoContainer daoContainer, EconomyService economyService,
			MainConfig mainConfig, Logger logger, Formatter formatter, MessagesConfig messagesConfig,
			CompanyManagement companyManagement, DragonBusinessPlugin plugin) {
		this.server = server;
		this.daoContainer = daoContainer;
		this.economyService = economyService;
		this.mainConfig = mainConfig;
		this.logger = logger;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
	}

	public void payAllSalaries() throws Exception {
		BigDecimal salary = new BigDecimal(mainConfig.business.salary_amount);

		for (Player player : server.getOnlinePlayers()) {
			// Check if the player is employed
			EmployeeDto employee = daoContainer.employees().getById(player.getUniqueId());

			if (employee == null)
				continue;

			ResultType result = payEmployee(employee, salary, TransactionType.Salary);

			// Send a message
			if (result == ResultType.SUCCESS) {
				player.sendMessage(
						formatter.formatText(mainConfig.business.salary_amount, messagesConfig.salary.salary_paid));
			} else if (result == ResultType.ACCOUNT_NO_FUNDS) {
				player.sendMessage(formatter.formatText(null, messagesConfig.salary.company_insufficient_funds));
			} else {
				player.sendMessage(formatter.formatText(null, messagesConfig.salary.payment_issue));
			}
		}
	}

	// Bonuses/dividends

	public void payBonusToEmployee(BigDecimal amount, UUID uuid) throws Exception {
		EmployeeDto employee = daoContainer.employees().getById(uuid);

		ResultType result = payEmployee(employee, amount, TransactionType.Bonus);

		if (result == ResultType.SUCCESS) {

		} else {

		}
	}

	public ResultType payEmployee(EmployeeDto employee, BigDecimal amount, TransactionType type) throws Exception {
		CompanyDto company = daoContainer.companies().getByEmployeeId(employee.uuid);
		ResultType withdrawResult = withdrawFromCompany(employee.company_id, amount);

		if (withdrawResult == ResultType.SUCCESS) {
			ResultType depositResult = depositToEmployee(employee.uuid, amount);

			if (depositResult == ResultType.SUCCESS) {
				// Create transaction representing this payment
				daoContainer.transactions().create(employee.uuid, company.id, Timestamp.from(Instant.now()), amount,
						type);

				return ResultType.SUCCESS;
			} else {
				logger.warn("Error paying {} of {} to {}: {}", type, amount, employee.name, depositResult.toString());
				return ResultType.FAILED;
			}
		} else {
			logger.info("Could not make payment to {} in context {} from company {}: {}", employee.name, type,
					company.name, withdrawResult.toString());

			return ResultType.ACCOUNT_NO_FUNDS;
		}
	}

	public void payAllDividends(int companyId) {

	}

	public void payDividend(float amount, int companyId, UUID player) {

	}

	public ResultType withdrawFromCompany(int companyId, BigDecimal amount) throws Exception {
		CompanyDto company = daoContainer.companies().getById(companyId);

		String virtualAccountId = CompanyUtil.getAccountId(company.name, mainConfig.business.company_account_prefix);

		Optional<Account> accountOpt = economyService.getOrCreateAccount(virtualAccountId);

		// Not found or error creating..
		if (!accountOpt.isPresent()) {
			logger.warn("Failed to withdraw {} for company {} - the virtual account {} could not be located or created",
					amount, company.name, virtualAccountId);
			return ResultType.FAILED;
		}

		Account account = accountOpt.get();

		TransactionResult withdrawResult = account.withdraw(economyService.getDefaultCurrency(), amount,
				Cause.source(this).named("company", company).build());

		return withdrawResult.getResult();
	}

	public ResultType depositToCompany(UUID employeeId, BigDecimal amount) throws Exception {

		EmployeeDto employee = daoContainer.employees().getById(employeeId);
		CompanyDto company = daoContainer.companies().getById(employee.company_id);

		String accountName = CompanyUtil.getAccountId(company.name, mainConfig.business.company_account_prefix);
		Optional<Account> accountOpt = economyService.getOrCreateAccount(accountName);

		// Not found or error creating..
		if (!accountOpt.isPresent()) {
			logger.warn(
					"Failed to deposit {} for company {} on behalf of employee {} the account {} could not be located or created",
					amount, company.name, employee.name, accountName);
			return ResultType.FAILED;
		}

		Account account = accountOpt.get();

		TransactionResult depositResult = account.deposit(economyService.getDefaultCurrency(), amount,
				Cause.source(this).named("employee", employee).named("company", company).build());

		if (depositResult.getResult() == ResultType.SUCCESS) {
			daoContainer.transactions().create(employee.uuid, company.id, Timestamp.from(Instant.now()), amount,
					TransactionType.EmployeeIncome);
		}

		return depositResult.getResult();
	}

	public ResultType depositToEmployee(UUID employeeId, BigDecimal amount) throws Exception {

		EmployeeDto employee = daoContainer.employees().getById(employeeId);

		Optional<UniqueAccount> accountOpt = economyService.getOrCreateAccount(employeeId);

		// Not found or error creating..
		if (!accountOpt.isPresent()) {
			logger.warn("Failed to deposit {} for employee {} the unique account {} could not be located or created",
					amount, employee.name, employeeId);
			return ResultType.FAILED;
		}

		Account account = accountOpt.get();

		TransactionResult depositResult = account.deposit(economyService.getDefaultCurrency(), amount,
				Cause.source(this).named("employee", employee).build());

		return depositResult.getResult();
	}
}
