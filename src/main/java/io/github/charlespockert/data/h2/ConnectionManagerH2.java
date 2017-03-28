package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.config.SqlConfiguration;
import io.github.charlespockert.data.ConnectionManager;

@Singleton
public class ConnectionManagerH2 implements ConnectionManager {

	SqlService sqlService;
	DataSource dataSource;
	SqlConfiguration sqlConfig;

	@Inject
	ConfigurationManager configurationManager;

	@Inject
	Logger logger;
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();		
	}

	public PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(sql);
		ParameterMetaData metadata = statement.getParameterMetaData();
		int ix = 1;

		for(Object param : params) {
			int type = metadata.getParameterType(ix);

			switch(type) {
			case Types.BIGINT:
			case Types.INTEGER:
			case Types.SMALLINT:
				statement.setInt(ix, (int)param);
				break;
			case Types.DOUBLE:
			case Types.NUMERIC:
				statement.setDouble(ix, (double)param);
				break;
			case Types.DECIMAL:
			case Types.FLOAT:
				statement.setFloat(ix, (float)param);
				break;
			case Types.VARCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
			case Types.NCHAR:
			case Types.CHAR:
				statement.setString(ix, (String)param);
				break;
			default:
				throw new SQLException("Unknown java.sql.Type type ID: " + type);
			}

			ix ++;
		}

		return statement;
	}

	@Override
	public void start() throws Exception {
		logger.info("Getting SQL service");
		sqlService = Sponge.getServiceManager().provide(SqlService.class).get();		
		logger.info("Getting config");
		sqlConfig = configurationManager.loadSqlConfiguration();
		logger.info("Connecting to " + sqlConfig.dbname);
		dataSource = sqlService.getDataSource(String.format("jdbc:h2:~/%s;AUTO_SERVER=TRUE", sqlConfig.dbname));
	}

	@Override
	public void shutdown() {		
	}

	@Override
	public void freeze() {
	}

	@Override
	public void unfreeze() {
	}
}
