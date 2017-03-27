package io.github.charlespockert.data.h2;

import java.sql.SQLException;
import java.util.List;

import io.github.charlespockert.data.CompanyDao;
import io.github.charlespockert.data.CompanyDto;

public class CompanyH2Dao extends H2DataConnector implements CompanyDao {

	@Override
	public boolean exists(String name) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists(int id) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CompanyDto get(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompanyDto get(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CompanyDto> getAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(CompanyDto employee) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(CompanyDto employee) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(CompanyDto employee) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CompanyDto> getByEmployeeId(String uuid) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
