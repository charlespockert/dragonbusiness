package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.inject.Inject;

import io.github.charlespockert.assets.AssetManager;
import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.DatabaseManager;

public class DatabaseManagerH2 implements DatabaseManager {

	@Inject
	private ConnectionManager connectionManager;
	
	@Inject
	private AssetManager assetManager;

	@Override
	public boolean databaseExists() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "SELECT * FROM Employee");
			statement.execute();
		} catch(SQLException ex) {
			return false;
		} finally {
			conn.close();
		}

		return true;
	}

	@Override
	public void createDatabase() throws Exception {
		String createScript = assetManager.getTextFile("create-database.sql");
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, createScript);
			statement.execute();
		} finally {
			conn.close();
		}
	}

	@Override
	public void deleteDatabase() throws Exception {
		String createScript = assetManager.getTextFile("delete-database.sql");
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, createScript);
			statement.execute();
		} finally {
			conn.close();
		}
	}

}
