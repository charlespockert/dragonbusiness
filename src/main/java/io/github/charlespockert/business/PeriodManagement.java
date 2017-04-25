package io.github.charlespockert.business;

import java.sql.SQLException;

import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.Player;

import com.google.inject.Inject;

import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.CompanyPerformanceDto;
import io.github.charlespockert.formatting.Formatter;

public class PeriodManagement {

	private DaoContainer daoContainer;

	private Server server;

	private Formatter formatter;

	private MessagesConfig messagesConfig;

	@Inject
	public PeriodManagement(DaoContainer daoContainer, Server server, Formatter formatter,
			MessagesConfig messagesConfig) {
		this.daoContainer = daoContainer;
		this.server = server;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
	}

	public void closePeriod() throws SQLException {

		// Create the new period - this also closes the previous period and
		// gathers stats
		int periodId = daoContainer.periods().create();

		// Message all players that the current period has been closed
		for (Player player : server.getOnlinePlayers()) {
			formatter.sendText(player, periodId - 1, messagesConfig.period.period_closed);

			/*
			 * if (topCompany != null) { formatter.sendText(player, topCompany,
			 * messagesConfig.period.top_company); } else {
			 * formatter.sendText(player, null,
			 * messagesConfig.period.no_top_company); }
			 */
		}
	}
}
