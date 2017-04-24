package io.github.charlespockert.business;

import java.math.BigDecimal;
import java.util.UUID;

import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.formatting.Formatter;

public class CompanyManagement {

	DragonBusinessPlugin plugin;

	DaoContainer daoContainer;

	// BusinessRepository businessRepository;

	EconomyService economyService;

	Formatter formatter;

	MessagesConfig messagesConfig;

	MainConfig mainConfig;

	Server server;

	@Inject
	public CompanyManagement(DaoContainer daoContainer, EconomyService economyService, Formatter formatter,
			MessagesConfig messagesConfig, DragonBusinessPlugin plugin, MainConfig mainConfig, Server server) {
		this.daoContainer = daoContainer;
		this.economyService = economyService;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
		this.plugin = plugin;
		this.mainConfig = mainConfig;
		this.server = server;
	}

	// Company status
	public void companyCreate(Cause context) throws Exception {
		String companyName = context.get("companyname", String.class).get();
		Player player = context.first(Player.class).get();

		// Check an employee doesn't already exist
		EmployeeDto employee = daoContainer.employees().getById(player.getUniqueId());

		if (employee != null)
			throw new CommandException(formatter.formatText(null, messagesConfig.create.already_employed));

		// Try to take the cash for company setup
		Account account = economyService.getOrCreateAccount(player.getUniqueId()).get();
		TransactionResult result = account.withdraw(economyService.getDefaultCurrency(),
				new BigDecimal(mainConfig.business.company_setup_fee), Cause.source(plugin).build());

		switch (result.getResult()) {
		case SUCCESS:
			int newCompanyId = daoContainer.companies().create(companyName, player.getUniqueId(), player.getName());
			CompanyDto company = daoContainer.companies().getById(newCompanyId);
			formatter.sendText(player, company, messagesConfig.create.success);
			break;
		default:
			throw new CommandException(formatter.formatText(null, messagesConfig.create.failed));
		}
	}

	public void bankrupt(int id) throws Exception {
		CompanyDto company = daoContainer.companies().getById(id);

		Text allPlayersMessage = formatter.formatText(company, messagesConfig.company.company_bankrupt_broadcast);

		for (Player player : server.getOnlinePlayers()) {
			// Notify all users that a company went bankrupt
			player.sendMessage(allPlayersMessage);
		}
	}

	// Shares
	public void playerBuyShares(int companyId, UUID player, int amount) {
		// Check if there are any shares for sale
		// businessRepository.companyGetByEmployeeId(uuid)
	}

	public void playerSellShares(int companyId, UUID player, int amount) {

	}

	// Employees

	public void companyApplyForPosition() {

	}

	public void companyAcceptCandidate(int companyId, UUID player, UUID recruiter) {

	}

	public void companyFireEmployee(int companyId, UUID player) {

	}

	// Jobs

	public void employeeCompleteJob(UUID player, int companyId) {

	}

	public void companyReceiveCash(float amount, int companyId, UUID player) {

	}

}
