package io.github.charlespockert.commands.implementations;

import java.sql.SQLException;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.data.DatabaseManager;

public class DatabaseCommand implements CommandExecutor {

	@Inject
	private DatabaseManager databaseManager;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		try {

			if(src instanceof ConsoleSource) {
				databaseManager.deleteDatabase();
				databaseManager.createDatabase();
			}
			
			return CommandResult.success();
		} catch(SQLException e) {
			throw new CommandException(Text.of(e.getMessage()));
		} catch (Exception e) {
			throw new CommandException(Text.of(e.getMessage()));
		}
	}

}
