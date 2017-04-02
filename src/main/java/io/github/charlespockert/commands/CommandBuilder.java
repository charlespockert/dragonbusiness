package io.github.charlespockert.commands;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.PluginLifecycle;
import io.github.charlespockert.commands.implementations.CompanyCommand;
import io.github.charlespockert.commands.implementations.CreateCompanyCommand;
import io.github.charlespockert.commands.implementations.DatabaseCommand;
import io.github.charlespockert.commands.implementations.ListCompaniesCommand;
import io.github.charlespockert.commands.implementations.ReloadConfigCommand;

@Singleton
public class CommandBuilder implements PluginLifecycle {

	@Inject 
	private DragonBusinessPlugin plugin;

	@Inject
	private CompanyCommand companyCommand;

	@Inject
	private DatabaseCommand databaseCommand;

	private void buildDatabaseCommands(Builder parent) {
		// Cmd def - /c database recreate
		parent.child(CommandSpec.builder()
				.description(Text.of("Manage Dragon Business database"))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("operation"))))
				.executor(databaseCommand)
				.build(), "database");
	}

	@Inject
	private ReloadConfigCommand reloadConfigCommand;
	
	private void buildAdminCommands(Builder parent) {
		parent.child(CommandSpec.builder()
				.description(Text.of("Reload configuration files (messages etc)"))
				.executor(reloadConfigCommand)
				.build(), 
				"reloadconfig");
	}
	
	@Inject
	private CreateCompanyCommand createCompanyCommand;

	@Inject 
	private ListCompaniesCommand listCompaniesCommand;
	
	private void buildCompanyCommands(Builder parent) {
		// Cmd def - /c create <company_name>
		parent.child(CommandSpec.builder()
				.description(Text.of("Create a new company"))
				.extendedDescription(Text.of("Creates a new company making you the CEO (assuming you have enough in your account!)"))
				.arguments(GenericArguments.string(Text.of("companyname")))
				.executor(createCompanyCommand)
				.build(), "create");

		// Cmd def - /c list
		parent.child(CommandSpec.builder()
				.description(Text.of("List all companies"))
				.extendedDescription(Text.of("Lists all companies in the company index"))
				.executor(listCompaniesCommand)
				.build(), "list");
		}

	@Override
	public void start() throws Exception {
		// Build parent command
		Builder parentCommand = CommandSpec.builder()
				.description(Text.of("Enquire on company details"))
				.extendedDescription(Text.of("Shows help for Dragon Business"))
				.executor(companyCommand);

		buildCompanyCommands(parentCommand);
		buildAdminCommands(parentCommand);
		buildDatabaseCommands(parentCommand);

		// Register parent /c or /company command
		Sponge.getCommandManager().register(plugin, parentCommand.build(), "c", "company");	
	}

	@Override
	public void shutdown() throws Exception {		
	}

	@Override
	public void freeze() {
	}

	@Override
	public void unfreeze() {
	}
}
