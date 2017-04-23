package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;

import io.github.charlespockert.data.dto.*;
import io.github.charlespockert.entities.*;

public class BusinessRepository {

	@Inject
	private BusinessDao dao;

	public boolean companyExists(int id) throws SQLException {
		return dao.companyExists(id);
	}

	public boolean companyExists(String name) throws SQLException {
		return dao.companyExists(name);
	}

	public Company companyGet(int id) throws SQLException {
		CompanyDto dto = dao.companyGetById(id);
		return Company.readFromDto(dto);
	}

	public Company companyGet(String name) throws SQLException {
		CompanyDto dto = dao.companyGetByName(name);
		return Company.readFromDto(dto);
	}

	public Company companyGetByEmployeeId(UUID uuid) throws SQLException {
		CompanyDto dto = dao.companyGetByEmployeeId(uuid);
		return Company.readFromDto(dto);
	}

	public List<Company> companyGetAll() throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();

		for (CompanyDto dto : dao.companyGetAll()) {
			companies.add(Company.readFromDto(dto));
		}

		return companies;
	}

	public List<CompanySummary> companyGetSummary() throws SQLException {
		ArrayList<CompanySummary> companies = new ArrayList<CompanySummary>();

		for (CompanySummaryDto dto : dao.companyGetSummary()) {
			companies.add(CompanySummary.readFromDto(dto));
		}

		return companies;
	}

	public int companyCreate(String name, UUID playerId, String playerName) throws SQLException {
		return dao.companyCreate(name, playerId, playerName);
	}

	public void companyUpdate(Company company) throws SQLException {
		CompanyDto dto = company.writeToDto();
		dao.companyUpdate(dto);
	}

	public void companyDelete(Company company) throws SQLException {
		CompanyDto dto = company.writeToDto();
		dao.companyDelete(dto);
	}

	public boolean employeeExistsByName(String uuid) throws SQLException {
		return dao.employeeExistsByName(uuid);
	}

	public boolean employeeExists(UUID uuid) throws SQLException {
		return dao.employeeExists(uuid);
	}

	public Employee employeeGet(UUID uuid) throws SQLException {
		EmployeeDto dto = dao.employeeGet(uuid);
		return Employee.readFromDto(dto);
	}

	public Employee employeeGetByName(String name) throws SQLException {
		EmployeeDto dto = dao.employeeGetByName(name);
		return Employee.readFromDto(dto);
	}

	public List<Employee> employeeGetAll() throws SQLException {
		ArrayList<Employee> employees = new ArrayList<Employee>();

		for (EmployeeDto dto : dao.employeeGetAll()) {
			employees.add(Employee.readFromDto(dto));
		}

		return employees;
	}

	public List<Employee> employeeGetByCompanyId(int id) throws SQLException {
		List<Employee> employees = new ArrayList<Employee>();

		for (EmployeeDto dto : dao.employeeGetByCompanyId(id)) {
			employees.add(Employee.readFromDto(dto));
		}

		return employees;
	}

	public List<Employee> employeeGetAllByRank(EmployeeRank rank) throws SQLException {
		ArrayList<Employee> employees = new ArrayList<Employee>();

		for (EmployeeDto dto : dao.employeeGetAllByRank(rank)) {
			employees.add(Employee.readFromDto(dto));
		}

		return employees;
	}

	public void employeeCreate(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		dao.employeeCreate(dto);
	}

	public void employeeUpdate(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		dao.employeeUpdate(dto);
	}

	public void employeeDelete(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		dao.employeeDelete(dto);
	}
}
