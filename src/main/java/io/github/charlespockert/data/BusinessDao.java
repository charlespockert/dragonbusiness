package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import io.github.charlespockert.data.dto.*;
import io.github.charlespockert.entities.EmployeeRank;

public interface BusinessDao {

	// Employees
	public boolean employeeExistsByName(String name) throws SQLException;

	public boolean employeeExists(UUID uuid) throws SQLException;

	public EmployeeDto employeeGetByName(String name) throws SQLException;

	public EmployeeDto employeeGet(UUID uuid) throws SQLException;

	public List<EmployeeDto> employeeGetByCompanyId(int companyId) throws SQLException;

	public List<EmployeeDto> employeeGetAll() throws SQLException;

	public List<EmployeeDto> employeeGetAllByRank(EmployeeRank rank) throws SQLException;

	public void employeeCreate(EmployeeDto employee) throws SQLException;

	public void employeeUpdate(EmployeeDto employee) throws SQLException;

	public void employeeDelete(EmployeeDto employee) throws SQLException;

	// Companies
	public boolean companyExists(String name) throws SQLException;

	public boolean companyExists(int companyId) throws SQLException;

	public EmployeeDto companyGetCompanyOwner(int companyId) throws SQLException;

	public CompanyDto companyGetByName(String name) throws SQLException;

	public CompanyDto companyGetById(int companyId) throws SQLException;

	public CompanyDto companyGetByEmployeeId(UUID uuid) throws SQLException;

	public List<CompanyDto> companyGetAll() throws SQLException;

	public List<CompanySummaryDto> companyGetSummary() throws SQLException;

	public int companyCreate(String name, UUID employeeId, String employeeName) throws SQLException;

	public void companyUpdate(CompanyDto company) throws SQLException;

	public void companyDelete(CompanyDto company) throws SQLException;

	// Transactions
	public TransactionDto transactionGet(int id) throws SQLException;

	public List<TransactionDto> transactionGetByEmployeeId(UUID employeeId) throws SQLException;

	public List<TransactionDto> transactionGetByEmployeeIdAndPeriod(UUID employeeId, int periodId) throws SQLException;

	public List<TransactionDto> transactionGetByCompanyId(int periodId) throws SQLException;

	public List<TransactionDto> transactionGetByCompanyIdAndPeriod(int companyId, int periodId) throws SQLException;

	public List<TransactionDto> transactionGetByPeriodId(int periodId) throws SQLException;

	public int transactionCreate() throws SQLException;

	// Transactions
	public ShareDto shareGet(int id) throws SQLException;

	public List<ShareDto> shareGetByEmployeeId(UUID employeeId) throws SQLException;

	public List<ShareDto> shareGetByCompanyId(int companyId) throws SQLException;

	public List<ShareDto> shareCreate(UUID employeeId, int companyId, int amount) throws SQLException;

	public List<ShareDto> shareUpdate(UUID employeeId, int comapnyId, int amount) throws SQLException;

	public int shareCreate() throws SQLException;

	public int shareUpdate() throws SQLException;
}
