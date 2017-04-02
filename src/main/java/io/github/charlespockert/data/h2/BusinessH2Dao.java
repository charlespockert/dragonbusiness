package io.github.charlespockert.data.h2;

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
import io.github.charlespockert.data.dto.*;
import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.data.BusinessDao;
import io.github.charlespockert.entities.EmployeeRank;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class BusinessH2Dao implements BusinessDao {

	@Inject
	private ConnectionManager connectionManager;

	@Inject 
	private ConfigurationManager configurationManager;
	
	@Inject
	private Logger logger;
	
	private CommentedConfigurationNode queries;
	
	public BusinessH2Dao() throws Exception {
	}
	
	private String getQuery(Object... path) {
		if(queries == null) {
			try {
				configurationManager.loadConfiguration("query.conf", "h2/query.conf");
			} catch (Exception e) {
				logger.error("Failed to load queries, errors are likely: " + e.getMessage());
			}
			
			queries = configurationManager.getConfiguration("query.conf").getNode("sql");
		}
		
		return queries.getNode(path).getString();
	}
	// Companies

	@Override
	public boolean companyExists(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "count-by-name"), name);
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
	public boolean companyExists(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "count-by-id"), id);
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
	public EmployeeDto companyGetCompanyOwner(int id) throws SQLException { 
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-owner"), EmployeeRank.CEO.getValue());
			return DtoUtil.employeePopulateSingle(statement);
		}
		finally {
			conn.close();
		}		
	}
	
	@Override
	public CompanyDto companyGet(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-by-name"), name);
			return DtoUtil.companyPopulateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public CompanyDto companyGet(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-by-id"), id);
			return DtoUtil.companyPopulateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<CompanyDto> companyGetAll() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-all"));
			return DtoUtil.companyPopulateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<CompanySummaryDto> companyGetSummary() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-summary"));
			List<CompanySummaryDto> summaries = DtoUtil.companySummaryPopulateMany(statement);
			
			return summaries;
		}
		finally {
			conn.close();
		}
	}

	
	@Override
	public int companyCreate(String name, UUID plauyerUuid, String employeeName) throws SQLException {
		Connection conn = connectionManager.getConnection();
		conn.setAutoCommit(false);
		
		try {
			PreparedStatement compStatement = connectionManager.prepareStatement(conn, getQuery("company", "create"), name);
			compStatement.executeUpdate();
			ResultSet updateResult = compStatement.getGeneratedKeys();
			updateResult.next();
			int companyId = updateResult.getInt(1);

			PreparedStatement empStatement = connectionManager.prepareStatement(conn, getQuery("employee", "create"), UuidUtil.asBytes(plauyerUuid), companyId, employeeName);
			empStatement.execute();

			conn.commit();
			return companyId;
		}
		catch(SQLException e) {
			conn.rollback();
			throw e;
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void companyUpdate(CompanyDto company) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "update"), company.name, company.id);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void companyDelete(CompanyDto company) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "delete"), company.id);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<CompanyDto> companyGetByEmployeeId(UUID uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("company", "get-by-employee-id"), UuidUtil.asBytes(uuid));
			return DtoUtil.companyPopulateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	// Employees
	
	@Override
	public boolean employeeExistsByName(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "count-by-name"), name);
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
	public boolean employeeExists(UUID uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "count-by-id"), UuidUtil.asBytes(uuid));
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
	public EmployeeDto employeeGetByName(String name) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "get-by-name"), name);
			return DtoUtil.employeePopulateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public EmployeeDto employeeGet(UUID uuid) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "get-by-id"), UuidUtil.asBytes(uuid));
			return DtoUtil.employeePopulateSingle(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<EmployeeDto> employeeGetByCompanyId(int id) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "get-by-company-ud"), id);
			return DtoUtil.employeePopulateMany(statement);
		}
		finally {
			conn.close();
		}
	}

	@Override
	public List<EmployeeDto> employeeGetAll() throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "get-all"));
			return DtoUtil.employeePopulateMany(statement);
		}
		finally {
			conn.close();
		}
	}
	
	@Override
	public List<EmployeeDto> employeeGetAllByRank(EmployeeRank rank) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "get-by-rank"), rank.getValue());
			return DtoUtil.employeePopulateMany(statement);
		}
		finally {
			conn.close();
		}
	}


	@Override
	public void employeeCreate(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "create"), UuidUtil.asBytes(employee.uuid), employee.company_id, employee.name);
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void employeeUpdate(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "update"), employee.name, UuidUtil.asBytes(employee.uuid));
			statement.execute();
		}
		finally {
			conn.close();
		}
	}

	@Override
	public void employeeDelete(EmployeeDto employee) throws SQLException {
		Connection conn = connectionManager.getConnection();

		try {
			PreparedStatement statement = connectionManager.prepareStatement(conn, getQuery("employee", "delete"), UuidUtil.asBytes(employee.uuid));
			statement.execute();
		}
		finally {
			conn.close();
		}
	}
}
