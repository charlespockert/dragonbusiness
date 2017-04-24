package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.github.charlespockert.assets.AssetGrabber;
import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.DatabaseManager;

@Singleton
public class DatabaseManagerH2 implements DatabaseManager {

	private ConnectionManager connectionManager;

	private AssetGrabber assetGrabber;

	private Logger logger;

	@Inject
	public DatabaseManagerH2(ConnectionManager connectionManager, AssetGrabber assetGrabber, Logger logger) {
		this.connectionManager = connectionManager;
		this.assetGrabber = assetGrabber;
		this.logger = logger;
	}

	@Override
	public boolean databaseExists() throws SQLException {
		logger.info("Checking DB schema exists...");

		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "SELECT * FROM Employee");
			statement.execute();
		} catch (SQLException ex) {
			return false;
		} finally {
			conn.close();
		}

		return true;
	}

	@Override
	public void createDatabase() throws Exception {
		String createScript = assetGrabber.getTextFile("h2/create-database.sql");
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = DbUtil.prepareStatement(conn, createScript);
			statement.execute();
		} finally {
			conn.close();
		}
	}

	@Override
	public void deleteDatabase() throws Exception {
		String createScript = assetGrabber.getTextFile("h2/delete-database.sql");
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = DbUtil.prepareStatement(conn, createScript);
			statement.execute();
		} finally {
			conn.close();
		}
	}

	@Override
	public void start() throws Exception {
		if (!databaseExists()) {
			createDatabase();
		}
	}

	@Override
	public void shutdown() throws Exception {
	}

	@Override
	public void freeze() {
	}

	@Override
	public void unfreeze() {
	}

}
