package io.github.charlespockert.commands.implementations;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.data.BusinessRepository;
import io.github.charlespockert.formatting.Formatter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ListCompaniesCommand implements CommandExecutor {

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
			formatter.sendList(src, businessRepository.companyGetSummary(), 
					messages.getNode("list", "heading").getString(),
					messages.getNode("list", "item").getString());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messages.getNode("general", "error").getString()));
		}

		return CommandResult.success();
	}

}
