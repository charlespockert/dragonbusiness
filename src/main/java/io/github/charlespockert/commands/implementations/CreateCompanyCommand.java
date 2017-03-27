package io.github.charlespockert.commands.implementations;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CreateCompanyCommand implements CommandExecutor {
	public static CommandSpec getCommandSpec() {
		return CommandSpec.builder()
				.description(Text.of("Create a new company"))
				.extendedDescription(Text.of("Creates a new company making you the CEO. If you are short on funds to pay Companies House for the registration, you will be given a loan to cover the amount. "))
				.arguments(GenericArguments.string(Text.of("companyname")))
				.executor(new CreateCompanyCommand())
				.build();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		return CommandResult.success();
	}
}
