package io.github.charlespockert.commands;
import org.spongepowered.api.Sponge;

public class CommandBuilder {

	public void BuildCommands(Object plugin) throws Exception {
		Sponge.getCommandManager().register(plugin, FinancesCommand.getCommandSpec(), "finances");	
	}
}
