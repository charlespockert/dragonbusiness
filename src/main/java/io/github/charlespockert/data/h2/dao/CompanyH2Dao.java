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
import io.github.charlespockert.data.dao.CompanyDao;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.CompanySummaryDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class CompanyH2Dao extends DaoBase implements CompanyDao {

	public CompanyH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public boolean exists(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select count(*) from company where name = ?",
					name);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public boolean exists(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select count(*) from company where id = ?",
					id);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public EmployeeDto getOwner(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select employee.* from company inner join employee on company.id = employee.company_id where employee.rank = ?",
					EmployeeRank.CEO);
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public CompanyDto getByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from company where name = ?", name);
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}

	@Override
	public CompanyDto getById(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from company where id = ?",
					companyId);
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}

	@Override
	public List<CompanyDto> getAll(String filter) throws SQLException {

		if (filter == null)
			filter = "";

		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from company where name like ?",
					"%" + filter + "%");
			return mapper.populateMany(statement, CompanyDto.class);
		}
	}

	@Override
	public List<CompanySummaryDto> getSummary(String filter) throws SQLException {

		if (filter == null)
			filter = "";

		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select company.name, employee.name as owner, (select count(*) from employee where employee.company_id = company.id) as employeecount, company.value from company inner join employee on employee.company_id = company.id and employee.rank = 3 and company.name like ?",
					"%" + filter + "%");
			List<CompanySummaryDto> summaries = mapper.populateMany(statement, CompanySummaryDto.class);

			return summaries;
		}
	}

	@Override
	public int create(String name, UUID uuid, String employeeName) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			conn.setAutoCommit(false);

			PreparedStatement compStatement = DbUtil.prepareStatement(conn,
					"insert into company (name, bankrupt, shares_issued, value) values (?, 0, 0, 0)", name);
			compStatement.executeUpdate();
			ResultSet updateResult = compStatement.getGeneratedKeys();
			updateResult.next();
			int companyId = updateResult.getInt(1);

			PreparedStatement empStatement = DbUtil.prepareStatement(conn,
					"insert into employee (uuid, company_id, name, rank, employment_start) values (?, ?, ?, ?, getdate())",
					UuidUtil.asBytes(uuid), companyId, employeeName, EmployeeRank.CEO);
			empStatement.execute();

			conn.commit();
			return companyId;
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void update(CompanyDto company) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"update company set name = ?, bankrupt = ?, shares_issued = ?, value = ?", company.name,
					company.id);
			statement.execute();
		}
	}

	@Override
	public void delete(CompanyDto company) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "delete from company where id = ?", company.id);
			statement.execute();
		}
	}

	@Override
	public CompanyDto getByEmployeeId(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select company.* from company INNER JOIN employee ON employee.company_id = company.id where employee.uuid = ?",
					UuidUtil.asBytes(uuid));
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}
}
