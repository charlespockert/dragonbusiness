package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {

	public boolean existsByName(String name) throws SQLException;
	public boolean exists(String uuid) throws SQLException;
	
	public EmployeeDto getByName(String name) throws SQLException;
	public EmployeeDto get(String uuid) throws SQLException;
	
	public List<EmployeeDto> getByCompanyId(int id) throws SQLException;

	public List<EmployeeDto> getAll() throws SQLException;
	
	public void create(EmployeeDto employee) throws SQLException;
	public void update(EmployeeDto employee) throws SQLException;
	public void delete(EmployeeDto employee) throws SQLException;
}
