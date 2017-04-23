package io.github.charlespockert.commands.company;

import java.util.Optional;

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

public class ListCompaniesCommand implements CommandExecutor {

	private Enquiry enquiry;

	private MessagesConfig messagesConfig;

	private Logger logger;

	@Inject
	public ListCompaniesCommand(MessagesConfig messagesConfig, Logger logger, Enquiry enquiry) {
		this.logger = logger;
		this.messagesConfig = messagesConfig;
		this.enquiry = enquiry;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		try {
			Optional<String> filterOpt = args.getOne("companyname");
			enquiry.companyListing(src, filterOpt.orElseGet(null));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messagesConfig.general.error));
		}

		return CommandResult.success();
	}

}
