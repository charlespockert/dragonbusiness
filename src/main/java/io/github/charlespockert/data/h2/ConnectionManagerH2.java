package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.spongepowered.api.service.sql.SqlService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.data.ConnectionManager;

@Singleton
public class ConnectionManagerH2 implements ConnectionManager {

	private SqlService sqlService;
	private DataSource dataSource;
	private MainConfig mainConfig;
	private Logger logger;

	@Inject
	public ConnectionManagerH2(Logger logger, SqlService sqlService, MainConfig mainConfig) {
		this.logger = logger;
		this.sqlService = sqlService;
		this.mainConfig = mainConfig;
	}

	public Connection getConnection() throws SQLException {
		try {
			ensureDatasource();
		} catch (Exception e) {
			logger.error("Error initialising database: " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return dataSource.getConnection();
	}

	public void ensureDatasource() throws Exception {
		if (dataSource == null)
			dataSource = sqlService
					.getDataSource(String.format("jdbc:h2:~/%s;AUTO_SERVER=TRUE", mainConfig.database.database_name));
	}
}
