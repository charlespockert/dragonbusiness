package io.github.charlespockert.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.DragonBusinessPlugin;
import io.github.charlespockert.PluginLifecycle;
import io.github.charlespockert.commands.admin.DatabaseCommand;
import io.github.charlespockert.commands.admin.ReloadConfigCommand;
import io.github.charlespockert.commands.company.RootCommand;
import io.github.charlespockert.commands.company.DetailedInfoCommand;
import io.github.charlespockert.commands.company.CreateCompanyCommand;
import io.github.charlespockert.commands.company.ListCompaniesCommand;

@Singleton
public class CommandBuilder implements PluginLifecycle {

	@Inject
	private DragonBusinessPlugin plugin;

	@Inject
	private RootCommand companyCommand;

	@Inject
	private DatabaseCommand databaseCommand;

	private void buildDatabaseCommands(Builder parent) {
		// Cmd def - /c database recreate
		parent.child(CommandSpec.builder().description(Text.of("Manage Dragon Business database"))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("operation"))))
				.executor(databaseCommand).build(), "database");
	}

	@Inject
	private ReloadConfigCommand reloadConfigCommand;

	private void buildAdminCommands(Builder parent) {
		parent.child(CommandSpec.builder().description(Text.of("Reload configuration files (messages etc)"))
				.executor(reloadConfigCommand).build(), "reloadconfig");
	}

	@Inject
	private CreateCompanyCommand createCompanyCommand;

	@Inject
	private ListCompaniesCommand listCompaniesCommand;

	@Inject
	private DetailedInfoCommand companyInfoCommand;

	private void buildCompanyCommands(Builder parent) {
		// Cmd def - /c create <companyname>
		parent.child(CommandSpec.builder().description(Text.of("Create a new company"))
				.extendedDescription(
						Text.of("Creates a new company making you the CEO (assuming you have enough in your account!)"))
				.arguments(GenericArguments.string(Text.of("companyname"))).executor(createCompanyCommand).build(),
				"create");

		// Cmd def - /c list <opt:companyname>
		parent.child(CommandSpec.builder().description(Text.of("List all companies"))
				.extendedDescription(Text.of("Lists all companies in the company index")).executor(listCompaniesCommand)
				.arguments(GenericArguments.optionalWeak(GenericArguments.string(Text.of("companyname")))).build(),
				"list");

		// Cmd def - /c info <companyname>
		parent.child(CommandSpec.builder().description(Text.of("Detailed company information"))
				.extendedDescription(Text.of("Shows detailed information about a company")).executor(companyInfoCommand)
				.arguments(GenericArguments.string(Text.of("companyname"))).build(), "info");
	}

	@Override
	public void start() throws Exception {
		// Build parent command
		Builder parentCommand = CommandSpec.builder().description(Text.of("Enquire on company details"))
				.extendedDescription(Text.of("Shows help for Dragon Business")).executor(companyCommand);

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
