package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import io.github.charlespockert.entities.Employee;

public class EmployeeRepository {

	@Inject
	private Logger logger;

	@Inject
	private EmployeeDao employeeDao;

	public boolean existsEmployeeByName(String uuid) throws SQLException {
		return employeeDao.existsByName(uuid);
	}

	public boolean existsEmployee(String name) throws SQLException {
		return employeeDao.exists(name);
	}

	public Employee getEmployee(String uuid) throws SQLException {
		EmployeeDto dto = employeeDao.get(uuid);
		Employee employee = new Employee();
		employee.readFromDto(dto);		
		return employee;
	}

	public Employee getEmployeeByName(String name) throws SQLException {
		EmployeeDto dto = employeeDao.getByName(name);
		Employee employee = new Employee();
		employee.readFromDto(dto);		
		return employee;
	}

	public List<Employee> getAllEmployees() throws SQLException {
		ArrayList<Employee> employees = new ArrayList<Employee>();

		for(EmployeeDto dto : employeeDao.getAll()) {
			Employee employee = new Employee();
			employee.readFromDto(dto);					
			employees.add(employee);
		}

		return employees;
	}

	public void createEmployee(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		employeeDao.create(dto);
	}

	public void updateEmployee(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		employeeDao.update(dto);
	}

	public void deleteEmployee(Employee employee) throws SQLException {
		EmployeeDto dto = employee.writeToDto();
		employeeDao.delete(dto);
	}

}
