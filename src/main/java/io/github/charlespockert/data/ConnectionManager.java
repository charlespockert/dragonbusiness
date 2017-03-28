package io.github.charlespockert.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.charlespockert.PluginLifecycle;

public interface ConnectionManager extends PluginLifecycle {
	
	public Connection getConnection() throws SQLException;
	
	public PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException;
}
