package io.github.charlespockert.commands.implementations;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import io.github.charlespockert.commands.CommandBase;


public class CompanyCommand extends CommandBase {

	public static Builder getCommandSpecBuilder() {
		return CommandSpec.builder()
				.description(Text.of("Enquire on company details"))
				.extendedDescription(Text.of("Returns the financial position for any given company"))
				.executor(new CompanyCommand());
	}

	@Override
	protected CommandResult executeCommand(CommandSource src, CommandContext args) throws CommandException {
		
		return CommandResult.success();
	}


}
