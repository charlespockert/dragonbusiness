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
	
	public List<EmployeeDto> employeeGetByCompanyId(int id) throws SQLException;

	public List<EmployeeDto> employeeGetAll() throws SQLException;
	
	public List<EmployeeDto> employeeGetAllByRank(EmployeeRank rank) throws SQLException;
	
	public void employeeCreate(EmployeeDto employee) throws SQLException;
	public void employeeUpdate(EmployeeDto employee) throws SQLException;
	public void employeeDelete(EmployeeDto employee) throws SQLException;
	
	
	// Companies
	public boolean companyExists(String name) throws SQLException;
	public boolean companyExists(int id) throws SQLException;
	
	public EmployeeDto companyGetCompanyOwner(int id) throws SQLException;
	
	public CompanyDto companyGet(String name) throws SQLException;
	public CompanyDto companyGet(int id) throws SQLException;
	
	public List<CompanyDto> companyGetByEmployeeId(UUID uuid) throws SQLException;
	
	public List<CompanyDto> companyGetAll() throws SQLException;
	
	public List<CompanySummaryDto> companyGetSummary() throws SQLException;
	
	public int companyCreate(String name, UUID employeeId, String employeeName) throws SQLException;
	public void companyUpdate(CompanyDto company) throws SQLException;
	public void companyDelete(CompanyDto company) throws SQLException;
}
