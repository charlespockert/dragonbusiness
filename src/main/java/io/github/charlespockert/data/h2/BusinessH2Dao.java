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
import io.github.charlespockert.data.h2.mappers.CompanySummaryDtoMapper;
import io.github.charlespockert.data.BusinessDao;
import io.github.charlespockert.entities.EmployeeRank;

public class BusinessH2Dao implements BusinessDao {

	private ConnectionManager connectionManager;

	private Logger logger;

	private H2QueriesConfig queries;

	private DatabaseMapper mapper;

	@Inject
	public BusinessH2Dao(ConnectionManager connectionManager, Logger logger, H2QueriesConfig queries,
			DatabaseMapper databaseMapper) {
		this.connectionManager = connectionManager;
		this.logger = logger;
		this.queries = queries;
		this.mapper = databaseMapper;
	}

	// Companies

	@Override
	public boolean companyExists(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.count_by_name, name);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public boolean companyExists(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.count_by_id, id);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public EmployeeDto companyGetCompanyOwner(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_owner,
					EmployeeRank.CEO.getValue());
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public CompanyDto companyGetByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_by_name, name);
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}

	@Override
	public CompanyDto companyGetById(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_by_id,
					companyId);
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}

	@Override
	public List<CompanyDto> companyGetAll() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_all);
			return mapper.populateMany(statement, CompanyDto.class);
		}
	}

	@Override
	public List<CompanySummaryDto> companyGetSummary() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_summary);
			List<CompanySummaryDto> summaries = mapper.populateMany(statement, CompanySummaryDto.class);

			return summaries;
		}
	}

	@Override
	public int companyCreate(String name, UUID plauyerUuid, String employeeName) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			conn.setAutoCommit(false);

			PreparedStatement compStatement = connectionManager.prepareStatement(conn, queries.company.create, name);
			compStatement.executeUpdate();
			ResultSet updateResult = compStatement.getGeneratedKeys();
			updateResult.next();
			int companyId = updateResult.getInt(1);

			PreparedStatement empStatement = connectionManager.prepareStatement(conn, queries.employee.create,
					UuidUtil.asBytes(plauyerUuid), companyId, employeeName);
			empStatement.execute();

			conn.commit();
			return companyId;
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public void companyUpdate(CompanyDto company) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.update, company.name,
					company.id);
			statement.execute();
		}
	}

	@Override
	public void companyDelete(CompanyDto company) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.delete, company.id);
			statement.execute();
		}
	}

	@Override
	public CompanyDto companyGetByEmployeeId(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.company.get_by_employee_id,
					UuidUtil.asBytes(uuid));
			return mapper.populateSingle(statement, CompanyDto.class);
		}
	}

	// Employees

	@Override
	public boolean employeeExistsByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.count_by_name,
					name);
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public boolean employeeExists(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.count_by_id,
					UuidUtil.asBytes(uuid));
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			int count = resultSet.getInt(1);
			return count > 0;
		}
	}

	@Override
	public EmployeeDto employeeGetByName(String name) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.get_by_name, name);
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public EmployeeDto employeeGet(UUID uuid) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.get_by_id,
					UuidUtil.asBytes(uuid));
			return mapper.populateSingle(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> employeeGetByCompanyId(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.get_by_company_id,
					id);
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> employeeGetAll() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.get_all);
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public List<EmployeeDto> employeeGetAllByRank(EmployeeRank rank) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.get_by_rank,
					rank.getValue());
			return mapper.populateMany(statement, EmployeeDto.class);
		}
	}

	@Override
	public void employeeCreate(EmployeeDto employee) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.create,
					UuidUtil.asBytes(employee.uuid), employee.company_id, employee.name);
			statement.execute();
		}
	}

	@Override
	public void employeeUpdate(EmployeeDto employee) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.update,
					employee.name, UuidUtil.asBytes(employee.uuid));
			statement.execute();
		}
	}

	@Override
	public void employeeDelete(EmployeeDto employee) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.employee.delete,
					UuidUtil.asBytes(employee.uuid));
			statement.execute();
		}
	}

	@Override
	public TransactionDto transactionGet(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.transaction.get_by_id, id);
			return mapper.populateSingle(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> transactionGetByEmployeeId(UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn,
					queries.transaction.get_by_employee_id, employeeId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> transactionGetByEmployeeIdAndPeriod(UUID employeeId, int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn,
					queries.transaction.get_by_employee_id_and_period_id, employeeId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> transactionGetByCompanyIdAndPeriod(int companyId, int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn,
					queries.transaction.get_by_company_id_and_period_id, companyId, periodId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> transactionGetByCompanyId(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn,
					queries.transaction.get_by_company_id, companyId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public List<TransactionDto> transactionGetByPeriodId(int periodId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.transaction.get_by_period_id,
					periodId);
			return mapper.populateMany(statement, TransactionDto.class);
		}
	}

	@Override
	public int transactionCreate() throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn,
					queries.transaction.get_by_period_id);
			return 0;
		}
	}

	@Override
	public ShareDto shareGet(int id) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.share.get_by_id, id);
			return mapper.populateSingle(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> shareGetByEmployeeId(UUID employeeId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.share.get_by_employee_id,
					employeeId);
			return mapper.populateMany(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> shareGetByCompanyId(int companyId) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.share.get_by_company_id,
					companyId);
			return mapper.populateMany(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> shareCreate(UUID employeeId, int companyId, int amount) throws SQLException {
		try (Connection conn = connectionManager.getConnection()) {
			PreparedStatement statement = connectionManager.prepareStatement(conn, queries.share.create, employeeId,
					companyId, amount);
			return mapper.populateMany(statement, ShareDto.class);
		}
	}

	@Override
	public List<ShareDto> shareUpdate(UUID employeeId, int comapnyId, int amount) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int shareCreate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int shareUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
}
