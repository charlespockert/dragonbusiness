package io.github.charlespockert.commands.implementations;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.command.spec.CommandSpec.Builder;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import io.github.charlespockert.commands.CommandBase;
import io.github.charlespockert.data.CompanyRepository;
import io.github.charlespockert.entities.Company;

public class InfoCommand extends CommandBase {

	private CompanyRepository companyRepository;

	public static Builder getCommandSpecBuilder() {
		return CommandSpec.builder()
				.description(Text.of("Show company information"))
				.extendedDescription(Text.of("Returns full details for any given company"))
				.arguments(GenericArguments.optional(GenericArguments.string(Text.of("companyname"))))
				.executor(new InfoCommand());
	}

	public InfoCommand() {
		companyRepository = Sponge.getServiceManager().getRegistration(CompanyRepository.class).get().getProvider();
	}

	@Override
	protected CommandResult executeCommand(CommandSource src, CommandContext args) throws CommandException {
		Optional<String> companynameOpt = args.<String>getOne("companyname");

		if(src instanceof Player) {
			Player player = (Player) src;

			try {

				if(companynameOpt.isPresent()) {
					Company company = companyRepository.getCompany(companynameOpt.get());

					if(company == null) 
					{
						player.sendMessage(Text.of(String.format("The company '%1' does not exist!", companynameOpt.get())));
						return CommandResult.empty();
					}
					else		
					{
						company.printInfo(player);
						return CommandResult.success();
					}
				} else {
					List<Company> companies = companyRepository.getByEmployeeId(player.getUniqueId().toString());

					for(Company company : companies) {
						company.printInfo(player);
					}

					return CommandResult.success();
				}				
			} catch(SQLException ex){
				throw new CommandException(Text.of(ex.getMessage()));
			}
		}

		return CommandResult.empty();
	}

}
