package io.github.charlespockert.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class SqlConfiguration {
	public String user;
	public String pass;
	public String dbname;
	
	public SqlConfiguration(CommentedConfigurationNode node) {
		user = node.getNode("database", "user").getString();
		pass = node.getNode("database", "pass").getString();
		dbname = node.getNode("database", "dbname").getString();
	}

	public void toNode(CommentedConfigurationNode node) {
		node.getNode("database", "user").setValue(user);
		node.getNode("database", "pass").setValue(pass);		
		node.getNode("database", "dbname").setValue(dbname);
	}
}
