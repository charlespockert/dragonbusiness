package io.github.charlespockert.commands.implementations;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.data.BusinessRepository;
import io.github.charlespockert.entities.Company;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.formatting.Formatter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class CreateCompanyCommand implements CommandExecutor {

	@Inject 
	private DragonBusinessPlugin plugin;

	@Inject 
	private BusinessRepository businessRepository;

	@Inject 
	private Formatter formatter;

	@Inject
	private ConfigurationManager configurationManager;

	@Inject 
	private Logger logger;

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		CommentedConfigurationNode messages = configurationManager.getConfiguration(ConfigurationManager.MESSAGES).getNode("messages");

		try {

			EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();

			Optional<String> companyName = args.<String>getOne("companyname");

			if(src instanceof Player) {
				Player player = (Player)src;

				// Check the player isn't already employed
				Employee emp = businessRepository.employeeGet(player.getUniqueId());

				if(emp != null)
					throw new CommandException(formatter.formatText(null, messages.getNode("create", "already_employed").getString()));

				// Try to take the cash for company setup
				Account account = service.getOrCreateAccount(player.getUniqueId()).get();
				// TODO: 1000 for testing, move to config variable
				TransactionResult result = account.withdraw(service.getDefaultCurrency(), new BigDecimal(1000), Cause.source(plugin).build());

				switch(result.getResult()) {
				case SUCCESS:
					int newCompanyId = businessRepository.companyCreate(companyName.get(), player.getUniqueId(), player.getName());
					Company company = businessRepository.companyGet(newCompanyId);
					formatter.sendText(src, company, messages.getNode("create", "success").getString());
					break;
				default:
					throw new CommandException(formatter.formatText(null, messages.getNode("create", "failed").getString()));
				}		

			} else {
				throw new CommandException(Text.of(messages.getNode("general", "player_only_command").getString()));
			}
		} catch(CommandException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messages.getNode("general", "error").getString()));
		}

		return CommandResult.success();
	}
}
