package io.github.charlespockert.test;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.spongepowered.api.service.sql.SqlService;

import io.github.charlespockert.assets.AssetGrabber;
import io.github.charlespockert.config.MainConfig;
import io.github.charlespockert.data.ConnectionManager;
import io.github.charlespockert.data.DatabaseManager;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.CompanyPerformanceDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;
import io.github.charlespockert.data.dto.PeriodDto;
import io.github.charlespockert.data.dto.ShareDto;
import io.github.charlespockert.data.dto.TransactionDto;
import io.github.charlespockert.data.dto.TransactionType;
import io.github.charlespockert.data.h2.ConnectionManagerH2;
import io.github.charlespockert.data.h2.DatabaseManagerH2;
import io.github.charlespockert.data.h2.DatabaseMapper;
import io.github.charlespockert.data.h2.dao.DaoH2Container;
import junit.framework.TestCase;

@RunWith(PowerMockRunner.class)
public class H2IntegrationTest extends TestCase {

	private int companyId;

	private MainConfig mainConfig;

	private Logger mockLogger;

	private ConnectionManager connectionManager;

	private SqlService mockSqlService;

	private DaoContainer dao;

	private DatabaseMapper databaseMapper;

	private DatabaseManager databaseManager;

	private AssetGrabber mockAssetGrabber;

	public void setUp() throws Exception {
		mainConfig = new MainConfig();
		mainConfig.database.database_name = "integrationtest";

		URL url = mainConfig.getClass().getClassLoader().getResource("assets/dragonbusiness/h2/create-database.sql");
		String createScript = IOUtils.toString(url.openStream(), "UTF-8");

		url = mainConfig.getClass().getClassLoader().getResource("assets/dragonbusiness/h2/delete-database.sql");
		String deleteScript = IOUtils.toString(url.openStream(), "UTF-8");

		mockAssetGrabber = mock(AssetGrabber.class);
		when(mockAssetGrabber.getTextFile("h2/create-database.sql")).thenReturn(createScript);
		when(mockAssetGrabber.getTextFile("h2/delete-database.sql")).thenReturn(deleteScript);

		mockLogger = mock(Logger.class);

		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL("jdbc:h2:~/integrationtest");

		mockSqlService = mock(SqlService.class);
		when(mockSqlService.getDataSource(anyString())).thenReturn(ds);

		connectionManager = new ConnectionManagerH2(mockLogger, mockSqlService, mainConfig);

		databaseMapper = new DatabaseMapper();

		dao = new DaoH2Container(connectionManager, mockLogger, databaseMapper);

		databaseManager = new DatabaseManagerH2(connectionManager, mockAssetGrabber, mockLogger);
		// Delete and recreate the DB each time so we have expected state
		databaseManager.deleteDatabase();
		databaseManager.createDatabase();

		// Seed with some test values
		seedDatabase();
	}

	public void seedDatabase() throws SQLException {
		companyId = dao.companies().create("charlie company", UUID.randomUUID(), "Charlie");
		dao.companies().create("fred company", UUID.randomUUID(), "Fred");
		dao.employees().create(UUID.randomUUID(), companyId, "Jack", EmployeeRank.Employee);
	}

	@Test
	public void testDatabaseExists() throws Exception {
		databaseManager.deleteDatabase();

		assertFalse(databaseManager.databaseExists());

		databaseManager.createDatabase();

		assertTrue(databaseManager.databaseExists());
	}

	@Test
	public void testCompanies() throws SQLException {
		List<CompanyDto> companies = dao.companies().getAll("");
		assertEquals(2, companies.size());

		companies = dao.companies().getAll("charlie");
		assertEquals(1, companies.size());

		CompanyDto company = dao.companies().getById(companyId);
		assertEquals(companyId, company.id);
		assertEquals(0, company.sharesIssued);
		assertEquals("charlie company", company.name);
		assertTrue(company.value.compareTo(BigDecimal.ZERO) == 0);
	}

	@Test
	public void testEmployees() throws SQLException {
		List<EmployeeDto> employees = dao.employees().getAll();
		assertEquals(3, employees.size());

		employees = dao.employees().getByCompanyId(companyId);
		assertEquals(2, employees.size());

		EmployeeDto employee = dao.employees().getByName("Charlie");
		assertEquals("Charlie", employee.name);
		assertEquals(companyId, employee.company_id);
		assertEquals(EmployeeRank.CEO, employee.rank);
	}

	@Test
	public void testTransactions() throws SQLException {
		Timestamp ts = new Timestamp(Date.from(Instant.now()).getTime());

		int id = dao.transactions().create(UUID.randomUUID(), 1, ts, new BigDecimal(1000), TransactionType.Salary);

		TransactionDto tran = dao.transactions().get(id);

		assertEquals(id, tran.id);
		assertTrue(new BigDecimal(1000).compareTo(tran.amount) == 0);
		assertEquals(ts, tran.date);
	}

	@Test
	public void testShares() throws SQLException {
		UUID uuid = UUID.randomUUID();

		dao.shares().create(companyId, uuid, 1000);

		ShareDto share = dao.shares().get(uuid, companyId);
		assertEquals(companyId, share.companyId);
		assertEquals(1000, share.count);
		assertEquals(uuid, share.uuid);

		List<ShareDto> shares = dao.shares().getByCompanyId(companyId);
		assertEquals(1, shares.size());
	}

	@Test
	public void testPeriods() throws SQLException {
		PeriodDto period = dao.periods().getCurrent();

		// There should be an open period in a new database, created by the DB
		// create script
		assertNotNull(period);

		int oldPeriodId = period.id;

		// Make sure the old period gets closed and the new one is created
		int newPeriodId = dao.periods().create();

		// Check the old and new period are there and correct
		PeriodDto oldPeriod = dao.periods().getById(oldPeriodId);
		PeriodDto newPeriod = dao.periods().getById(newPeriodId);

		assertEquals(newPeriodId, newPeriod.id);
		assertEquals(oldPeriodId, oldPeriod.id);
		assertTrue(newPeriod.id != oldPeriod.id);

		// Get the new period using "getCurrent" to check it's the right one
		PeriodDto currentPeriod = dao.periods().getCurrent();
		assertEquals(newPeriodId, currentPeriod.id);
	}

	@Test
	public void testPerformance() throws SQLException {
		UUID uuid = UUID.randomUUID();
		Timestamp ts = Timestamp.from(Instant.now());
		BigDecimal amount = new BigDecimal(1000);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Salary);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Salary);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Salary);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Salary);

		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Dividend);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Dividend);

		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.Bonus);

		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);
		dao.transactions().create(uuid, companyId, ts, amount, TransactionType.EmployeeIncome);

		PeriodDto period = dao.periods().getCurrent();
		CompanyPerformanceDto performance = dao.companyStats().getPerformance(companyId, period.id);

		// Shouldn't be a performance record yet as we haven't closed the period
		assertNull(performance);

		// Now close period
		dao.periods().create();

		// Get performance data
		performance = dao.companyStats().getPerformance(companyId, period.id);
		assertTrue(new BigDecimal(4000).compareTo(performance.salary) == 0);
		assertTrue(new BigDecimal(2000).compareTo(performance.dividends) == 0);
		assertTrue(new BigDecimal(1000).compareTo(performance.bonuses) == 0);
		assertTrue(new BigDecimal(10000).compareTo(performance.turnover) == 0);
		assertTrue(new BigDecimal(7000).compareTo(performance.overheads) == 0);
		assertTrue(new BigDecimal(3000).compareTo(performance.value) == 0);

		assertTrue(new BigDecimal(3000).compareTo(performance.profit) == 0);

		assertTrue(new BigDecimal(100).compareTo(performance.growth) == 0);
	}
}
