package io.github.charlespockert.commands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec.Builder;

import io.github.charlespockert.commands.implementations.CompanyCommand;
import io.github.charlespockert.commands.implementations.CreateCompanyCommand;

public class CommandBuilder {

	public void BuildCommands(Object plugin) throws Exception {
		// Build parent command
		Builder parentCommand = CompanyCommand.getCommandSpecBuilder();

		// Add children
		parentCommand.child(CreateCompanyCommand.getCommandSpec());

		// Register
		Sponge.getCommandManager().register(plugin, parentCommand.build(), "c", "company");	
	}
}
