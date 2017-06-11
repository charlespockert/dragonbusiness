package io.github.charlespockert.data.h2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;

import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.UuidUtil;
import io.github.charlespockert.data.dao.ApplicationDao;
import io.github.charlespockert.data.dto.ApplicationDto;
import io.github.charlespockert.data.dto.ApplicationStatus;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.DbUtil;

public class ApplicationH2Dao extends DaoBase implements ApplicationDao {

	@Inject
	public ApplicationH2Dao(ConnectionManager connectionManager, Logger logger, DatabaseMapper mapper) {
		super(connectionManager, logger, mapper);
	}

	@Override
	public ApplicationDto getById(int applicationId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn, "select * from application where id = ?",
					applicationId);
			statement.execute();
			return mapper.populateSingle(statement, ApplicationDto.class);
		}
	}

	@Override
	public List<ApplicationDto> getByEmployeeId(UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from application where employee_id = ?", UuidUtil.asBytes(employeeId));
			statement.execute();
			return mapper.populateMany(statement, ApplicationDto.class);
		}
	}

	@Override
	public List<ApplicationDto> getByCompanyId(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"select * from application where company_id = ?", companyId);
			statement.execute();
			return mapper.populateMany(statement, ApplicationDto.class);
		}
	}

	@Override
	public int create(int companyId, UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"insert into application () values (?, ?, ?, ?)", companyId);
			statement.executeUpdate();
			ResultSet updateResult = statement.getGeneratedKeys();
			updateResult.next();
			int applicationId = updateResult.getInt(1);

			return applicationId;
		}
	}

	@Override
	public void update(int applicationId, ApplicationStatus status, String rejectionReason) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"update application set status = ?, rejection_reason = ? where application_id = ?", status,
					rejectionReason, applicationId);
			statement.execute();
		}
	}

	@Override
	public void delete(int applicationId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = DbUtil.prepareStatement(conn,
					"delete from application where application_id = ?", applicationId);
			statement.execute();
		}
	}

}
