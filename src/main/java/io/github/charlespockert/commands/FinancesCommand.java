package io.github.charlespockert.commands;

import java.math.BigDecimal;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;


public class FinancesCommand extends CommandBase {


	public static CommandSpec getCommandSpec() {
		return CommandSpec.builder()
				.description(Text.of("Enquire on company finances"))
				.extendedDescription(Text.of("Returns the financial position for any given company"))
				.arguments(GenericArguments.string(Text.of("companyname")))
				.executor(new FinancesCommand())
				.build();
	}

	@Override
	CommandResult executeCommand(CommandSource src, CommandContext args) throws CommandException {

		String companyname = args.<String>getOne("companyname").get();

		if(src instanceof Player) {
			Player player = (Player) src;

			if(economyService.hasAccount(companyname)) {
				Optional<Account> uOpt = economyService.getOrCreateAccount(companyname);

				if (uOpt.isPresent()) {
					Account acc = uOpt.get();
					BigDecimal balance = acc.getBalance(economyService.getDefaultCurrency());
					player.sendMessage(Text.of(balance.toString()));
				}
			} else {
				player.sendMessage(Text.of("That company does not exist!"));	
				return CommandResult.empty();
			}		
		}

		return CommandResult.success();
	}


}
