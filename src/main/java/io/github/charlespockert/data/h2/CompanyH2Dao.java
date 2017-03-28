package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import io.github.charlespockert.data.CompanyDao;
import io.github.charlespockert.data.CompanyDto;
import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.EmployeeDto;

public class CompanyH2Dao implements CompanyDao {

	@Inject
	private Logger logger;
	
	@Inject
	private ConnectionManager connectionManager;
	
	@Override
	public boolean exists(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select count(*) from company where name = ?", name);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;		
		}
		finally {
			conn.close();
		}
	}

	@Override
	public boolean exists(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select count(*) from company where id = ?", id);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;		
		}
		finally {
			conn.close();
		}
	}

	@Override
	public CompanyDto get(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from company where name = ?", name);
			return populateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public CompanyDto get(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from company where id = ?", id);
			return populateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<CompanyDto> getAll() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from company");
			return populateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void create(CompanyDto company) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "insert into company (id, name) values (?, ?)", company.id, company.name);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void update(CompanyDto company) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "update company set name = ? where id = ?", company.name, company.id);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void delete(CompanyDto company) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "delete from company where id = ?", company.id);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<CompanyDto> getByEmployeeId(String uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select company.* from company INNER JOIN employee ON employee.company_id = company.id where employee.uuid = ?", uuid);
			return populateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	public static CompanyDto populateDto(ResultSet resultSet) throws SQLException {
		CompanyDto dto = new CompanyDto();
		
		dto.id = resultSet.getInt("id");
		dto.name = resultSet.getString("name");
		
		return dto;	
	}
	
	public static CompanyDto populateSingle(PreparedStatement statement) throws SQLException {		
		statement.execute();

		ResultSet resultSet = statement.getResultSet();		

		if(resultSet == null) return null;
		
		return populateDto(resultSet);
	}

	public static List<CompanyDto> populateMany(PreparedStatement statement) throws SQLException {
		ArrayList<CompanyDto> companyDtos = new ArrayList<CompanyDto>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if(resultSet != null) {
			while(resultSet.next()) {
				companyDtos.add(populateDto(resultSet));
			}
		}

		return companyDtos;
	}
}
