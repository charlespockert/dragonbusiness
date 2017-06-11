package io.github.charlespockert.commands.elements;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.PatternMatchingCommandElement;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.github.charlespockert.data.dao.CompanyDao;
import io.github.charlespockert.data.dao.DaoContainer;

public class CompanyNameElement extends PatternMatchingCommandElement {

	private DaoContainer daoContainer;

	private Logger logger;

	@Inject
	public CompanyNameElement(@Assisted Text key, DaoContainer daoContainer, Logger logger) {
		super(key);
		this.daoContainer = daoContainer;
		this.logger = logger;
	}

	@Override
	protected Iterable<String> getChoices(CommandSource src) {
		try {
			return daoContainer.companies().getIdentifiers().stream().map(company -> company.name)
					.collect(Collectors.toList());
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warn("Could not get company names due to database error: " + e.getMessage());
			return new ArrayList<String>();
		}
	}

	@Override
	protected Object getValue(String choice) throws IllegalArgumentException {
		try {
			return daoContainer.companies().getByName(choice);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.warn("Could not get company name due to database error: " + e.getMessage());
			return null;
		}
	}

	public Text getUsage(CommandSource src) {
		return Text.EMPTY;
	}
}
