package io.github.charlespockert.commands.company;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.business.CompanyManagement;
import io.github.charlespockert.config.MessagesConfig;

public class EmploymentApplicationCommand implements CommandExecutor {

	private MessagesConfig messagesConfig;

	private CompanyManagement companyManagement;

	private Logger logger;

	@Inject
	public EmploymentApplicationCommand(CompanyManagement companyManagement, MessagesConfig messagesConfig,
			Logger logger) {
		this.companyManagement = companyManagement;
		this.messagesConfig = messagesConfig;
		this.logger = logger;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		try {
			String companyName = args.<String>getOne("companyname").get();

			if (src instanceof Player) {
				// Build a cause from the player
				Cause cause = Cause.builder().owner((Player) src).named("companyname", companyName).build();

				// Try to create
				companyManagement.applyForPosition(cause);
			} else {
				throw new CommandException(Text.of(messagesConfig.general.player_only_command));
			}
		} catch (CommandException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CommandException(Text.of(messagesConfig.general.error));
		}

		return CommandResult.success();
	}

}
