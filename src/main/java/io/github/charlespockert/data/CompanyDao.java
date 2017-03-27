package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDao {
	public boolean exists(String name) throws SQLException;
	public boolean exists(int id) throws SQLException;
	
	public CompanyDto get(String name) throws SQLException;
	public CompanyDto get(int id) throws SQLException;
	
	public List<CompanyDto> getByEmployeeId(String uuid) throws SQLException;
	
	public List<CompanyDto> getAll() throws SQLException;
	
	public void create(CompanyDto company) throws SQLException;
	public void update(CompanyDto company) throws SQLException;
	public void delete(CompanyDto company) throws SQLException;
}
