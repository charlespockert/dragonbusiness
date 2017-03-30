package io.github.charlespockert.commands.implementations;

import java.sql.SQLException;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.data.CompanyRepository;
import io.github.charlespockert.formatting.CompanyMessageFormatter;

public class ListCompaniesCommand implements CommandExecutor {

	@Inject 
	private CompanyRepository companyRepository;
	
	@Inject 
	private CompanyMessageFormatter companyFormatter;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		try {
			companyFormatter.formatCompanyList(src, companyRepository.getAllCompanies());
		} catch (SQLException e) {
			throw new CommandException(Text.of(e.getMessage()));
		}
		
		return CommandResult.success();
	}

}
