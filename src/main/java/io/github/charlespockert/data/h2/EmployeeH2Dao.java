package io.github.charlespockert.data.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.EmployeeDao;
import io.github.charlespockert.data.EmployeeDto;

public class EmployeeH2Dao implements EmployeeDao {

	@Inject
	private ConnectionManager connectionManager;

	@Override
	public boolean existsByName(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select count(*) from employee where name = ?", name);
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
	public boolean exists(String uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select count(*) from employee where uuid = ?", uuid);
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
	public EmployeeDto getByName(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from employee where name = ?", name);
			return populateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public EmployeeDto get(String uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from employee where uuid = ?", uuid);
			return populateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<EmployeeDto> getByCompanyId(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from employee where company_id = ?", id);
			return populateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<EmployeeDto> getAll() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "select * from employee");
			return populateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void create(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "insert into employee (uuid, company_id, name) values (?, ?, ?)", employee.uuid, employee.company_id, employee.name);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void update(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "update employee set name = ? where uuid = ?", employee.name, employee.uuid);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void delete(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, "delete employee where uuid = ?", employee.uuid);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	public static EmployeeDto populateDto(ResultSet resultSet) throws SQLException {
		EmployeeDto dto = new EmployeeDto();
		
		dto.uuid = resultSet.getString("uuid");
		dto.name = resultSet.getString("name");
		dto.rank = resultSet.getInt("rank");

		return dto;	
	}
	
	public static EmployeeDto populateSingle(PreparedStatement statement) throws SQLException {		
		statement.execute();

		ResultSet resultSet = statement.getResultSet();		

		if(resultSet == null) return null;
		
		return populateDto(resultSet);
	}

	public static List<EmployeeDto> populateMany(PreparedStatement statement) throws SQLException {
		ArrayList<EmployeeDto> employeeDtos = new ArrayList<EmployeeDto>();
		statement.execute();
		ResultSet resultSet = statement.getResultSet();

		if(resultSet != null) {
			while(resultSet.next()) {
				employeeDtos.add(populateDto(resultSet));
			}
		}

		return employeeDtos;
	}


}
