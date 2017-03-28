package io.github.charlespockert.data;

import java.sql.SQLException;

import io.github.charlespockert.PluginLifecycle;

public interface DatabaseManager extends PluginLifecycle {
	public boolean databaseExists() throws SQLException;
	
	public void createDatabase() throws Exception, SQLException;
	
	public void deleteDatabase() throws Exception, SQLException;
}
