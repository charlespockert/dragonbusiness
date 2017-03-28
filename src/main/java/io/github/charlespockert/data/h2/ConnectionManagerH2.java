package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import io.github.charlespockert.data.ConnectionManager;

public class ConnectionManagerH2 implements ConnectionManager {

	SqlService sqlService;
	DataSource dataSource;
	
	public ConnectionManagerH2() throws SQLException {	
		sqlService = Sponge.getServiceManager().provide(SqlService.class).get();
		dataSource = sqlService.getDataSource("jdbc:h2:~/dragonbusiness");
	}
		
	public Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();		
		return conn;
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
}
