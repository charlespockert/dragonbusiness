package io.github.charlespockert.data.dao;

public interface DaoContainer {
	public CompanyDao companies();

	public PeriodDao periods();

	public ShareDao shares();

	public TransactionDao transactions();

	public EmployeeDao employees();

	public CompanyStatisticsDao companyStats();
}
