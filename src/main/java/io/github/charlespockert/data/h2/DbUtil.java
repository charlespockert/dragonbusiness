package io.github.charlespockert.data.h2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public class DbUtil {
	public static PreparedStatement prepareStatement(Connection conn, String sql, Object... params)
			throws SQLException {

		PreparedStatement statement = conn.prepareStatement(sql);
		ParameterMetaData metadata = statement.getParameterMetaData();
		int ix = 1;

		for (Object param : params) {

			int type = metadata.getParameterType(ix);

			switch (type) {
			case Types.BIGINT:
			case Types.INTEGER:
			case Types.SMALLINT:
				// Handle enums
				if (param.getClass().isEnum()) {
					statement.setInt(ix, ((Enum<?>) param).ordinal());
				} else {
					statement.setInt(ix, (int) param);
				}
				break;
			case Types.DOUBLE:
			case Types.NUMERIC:
				statement.setDouble(ix, (double) param);
				break;
			case Types.DECIMAL:
				statement.setBigDecimal(ix, (BigDecimal) param);
				break;
			case Types.FLOAT:
				statement.setFloat(ix, (float) param);
				break;
			case Types.VARCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
			case Types.NCHAR:
			case Types.CHAR:
				statement.setString(ix, (String) param);
				break;
			case Types.VARBINARY:
				statement.setBytes(ix, (byte[]) param);
				break;
			case Types.TIMESTAMP:
				statement.setTimestamp(ix, new Timestamp(((Date) param).getTime()));
				break;
			default:
				throw new SQLException("Unknown java.sql.Type type ID: " + type);
			}

			ix++;
		}

		return statement;
	}
}
