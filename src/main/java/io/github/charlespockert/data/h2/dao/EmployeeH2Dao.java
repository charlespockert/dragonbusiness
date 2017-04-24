package io.github.charlespockert.data.h2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dao.EmployeeDao;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class EmployeeH2Dao extends DaoBase implements EmployeeDao {

	public EmployeeH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public boolean existsByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select count(*) from employee where name = ?",
					name);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public boolean exists(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select count(*) from employee where uuid = ?",
					UuidUtil.asBytes(uuid));
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public EmployeeDto getByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from employee where name = ?", name);
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public EmployeeDto getById(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from employee where uuid = ?",
					UuidUtil.asBytes(uuid));
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> getByCompanyId(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from employee where company_id = ?",
					id);
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> getAll() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from employee");
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> getByRank(EmployeeRank rank) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from employee where rank = ?", rank);
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public EmployeeDto getCompanyOwner(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from employee where company_id = ? and rank = 3", companyId);
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public void create(UUID uuid, int companyId, String name, EmployeeRank rank) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"insert into employee (uuid, company_id, name, rank, employment_start) values (?, ?, ?, ?, getdate())",
					UuidUtil.asBytes(uuid), companyId, name, rank);
			statement.execute();
		}
	}

	@Override
	public void update(UUID uuid, String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "update employee set name = ?, rank = ?", name,
					UuidUtil.asBytes(uuid));
			statement.execute();
		}
	}

	@Override
	public void delete(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "delete from employee where uuid = ?",
					UuidUtil.asBytes(uuid));
			statement.execute();
		}
	}
}
