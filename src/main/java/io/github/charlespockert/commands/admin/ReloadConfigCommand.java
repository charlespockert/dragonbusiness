package io.github.charlespockert.commands.admin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.config.ConfigurationManager;

public class ReloadConfigCommand implements CommandExecutor {

	private ConfigurationManager configurationManager;

	@Inject
	public ReloadConfigCommand(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		try {
			configurationManager.start();
			return CommandResult.success();
		} catch (Exception e) {
			throw new CommandException(Text.of(e.getMessage()));
		}
	}

}
