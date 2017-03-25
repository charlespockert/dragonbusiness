package io.github.charlespockert.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

public abstract class CommandBase implements CommandExecutor {

	protected EconomyService economyService;
		
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);
		
		if(service.isPresent()) {
			economyService = service.get();
		} else {
			throw new CommandException(Text.of("No economy plugin present"));
		}
		
		return executeCommand(src, args);
	}

	abstract CommandResult executeCommand(CommandSource src, CommandContext args) throws CommandException;
}
