package io.github.charlespockert.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConnectionManager {
	public Connection getConnection() throws SQLException;
	
	public PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException;
}
