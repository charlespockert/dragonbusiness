// COMPANY
// ============================

CREATE TABLE IF NOT EXISTS company
(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	name NVARCHAR(60) NOT NULL,
	bankrupt BIT NOT NULL,
	shares_issued INT NOT NULL,
	value DECIMAL(20, 5) NOT NULL
);

// Make sure name is unique as we will use it for virtual account names
ALTER TABLE company ADD CONSTRAINT name UNIQUE(name);

// EMPLOYEE
// ============================

CREATE TABLE IF NOT EXISTS employee
(
	uuid BINARY(16) PRIMARY KEY NOT NULL, 
	company_id INT NOT NULL, 
	name NVARCHAR(16) NOT NULL, 
	rank INT NOT NULL, 
	employment_start DATE NOT NULL,
	
	// Make sure there is only 1 owner per company
	is_owner BIT AS (CASE rank WHEN 3 THEN 1 ELSE null END)
);

// Make sure there is only 1 owner per company
ALTER TABLE employee ADD CONSTRAINT owner UNIQUE(company_id, is_owner);

// TRANSACTION
// ============================
// Transaction is always from the
// perspective of the company - salary payments are negative
// money coming in is positive

CREATE TABLE IF NOT EXISTS transaction
(	
	// Use big int just in case... hopefully.. never? Come on guys :D
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	uuid BINARY(16) NOT NULL,
	company_id INT NOT NULL,
	date TIMESTAMP NOT NULL,
	amount DECIMAL(20, 5) NOT NULL,

	// Transaction type:
	// 0 = salary payment
	// 1 = bonus payment
	// 2 = dividend payment
	// 3 = job income
	type INT NOT NULL
);

// SHARES
// ============================
// Shares are issued on company creation and usually tied to an employee
// They can also be issued, meaning they aren't tied to an employee and can be freely purchased

CREATE TABLE share
(
	company_id INT NOT NULL,
	
	// Make nullable for issued shares with no owner
	uuid BINARY(16) NULL,
	
	count INT NOT NULL
);

// APPLICATION
// ============================
// Applications track employees applying for job positions at a company
CREATE TABLE application 
(
	employee_id varbinary(16) NOT NULL,
	company_id int NOT NULL
)

// PERIODS
// ============================
// Periods track all financial periods created in the game. Users can query this table through commands
// in order to look back on previous financial data in the game
CREATE TABLE PERIOD 
(
	start_date TIMESTAMP, 
	end_date TIMESTAMP, 
	id int auto_increment
)