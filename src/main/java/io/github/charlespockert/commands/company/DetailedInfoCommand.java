package io.github.charlespockert.commands.company;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.business.Enquiry;
import io.github.charlespockert.config.MessagesConfig;

public class DetailedInfoCommand implements CommandExecutor {

	private Enquiry enquiry;

	private Logger logger;

	private MessagesConfig messagesConfig;

	@Inject
	public DetailedInfoCommand(Logger logger, Enquiry enquiry, MessagesConfig messagesConfig) {
		this.enquiry = enquiry;
		this.logger = logger;
		this.messagesConfig = messagesConfig;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		String companyName = args.<String>getOne("companyname").get();

		try {
			enquiry.companyDetailedInformation(src, companyName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messagesConfig.general.error));
		}

		return CommandResult.empty();

	}
}
