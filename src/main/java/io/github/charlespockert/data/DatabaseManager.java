package io.github.charlespockert.data;

import java.sql.SQLException;

public interface DatabaseManager {
	public boolean databaseExists() throws SQLException;
	
	public void createDatabase() throws Exception, SQLException;
	
	public void deleteDatabase() throws Exception, SQLException;
}
