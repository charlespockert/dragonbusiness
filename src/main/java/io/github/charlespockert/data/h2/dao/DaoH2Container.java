package io.github.charlespockert.data.h2.dao;

import org.slf4j.Logger;
import com.google.inject.Inject;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.dao.CompanyDao;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dao.EmployeeDao;
import io.github.charlespockert.data.dao.PeriodDao;
import io.github.charlespockert.data.dao.ShareDao;
import io.github.charlespockert.data.dao.TransactionDao;
import io.github.charlespockert.data.h2.DatabaseMapper;

public class DaoH2Container implements DaoContainer {

	private CompanyDao companies;

	private PeriodDao periods;

	private ShareDao shares;

	private TransactionDao transactions;

	private EmployeeDao employees;

	@Override
	public CompanyDao companies() {
		return this.companies;
	}

	@Override
	public PeriodDao periods() {
		return this.periods;
	}

	@Override
	public ShareDao shares() {
		return this.shares;
	}

	@Override
	public TransactionDao transactions() {
		return this.transactions;
	}

	@Override
	public EmployeeDao employees() {
		return this.employees;
	}

	@Inject
	public DaoH2Container(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		this.companies = new CompanyH2Dao(connectionManager, logger, mapper);
		this.periods = new PeriodH2Dao(connectionManager, logger, mapper);
		this.shares = new ShareH2Dao(connectionManager, logger, mapper);
		this.transactions = new TransactionH2Dao(connectionManager, logger, mapper);
		this.employees = new EmployeeH2Dao(connectionManager, logger, mapper);
	}
}