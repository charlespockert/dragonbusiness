package io.github.charlespockert.data.h2.dao;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.h2.DatabaseMapper;

public class DaoBase {

	protected ConnectionManager connectionManager;

	protected Logger logger;

	protected DatabaseMapper mapper;

	public DaoBase(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		this.connectionManager = connectionManager;
		this.logger = logger;
		this.mapper = mapper;
	}
}
