package io.github.charlespockert.commands.company;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import io.github.charlespockert.business.Enquiry;
import io.github.charlespockert.config.MessagesConfig;

public class ListEmployeesCommand implements CommandExecutor {

	Enquiry enquiry;

	MessagesConfig messagesConfig;

	Logger logger;

	public ListEmployeesCommand(Enquiry enquiry, MessagesConfig messagesConfig, Logger logger) {
		this.enquiry = enquiry;
		this.messagesConfig = messagesConfig;
		this.logger = logger;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		try {
			String companyName = args.<String>getOne("companyname").get();
			enquiry.companyEmployeeListing(src, companyName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messagesConfig.general.error));
		}

		return CommandResult.success();
	}

}
