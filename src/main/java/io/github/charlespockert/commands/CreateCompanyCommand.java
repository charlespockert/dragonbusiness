package io.github.charlespockert.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CreateCompanyCommand {
	public static CommandSpec getCommandSpec() {
		return CommandSpec.builder()
				.description(Text.of("Create a new company"))
				.extendedDescription(Text.of("Creates a new company making you the CEO. If you are short on funds to pay Companies House for the registration, you will be given a loan to cover the amount. "))
				.arguments(GenericArguments.string(Text.of("companyname")))
				.executor(new FinancesCommand())
				.build();
	}
}
