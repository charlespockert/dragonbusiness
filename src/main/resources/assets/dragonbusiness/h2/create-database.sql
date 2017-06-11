// VERSION
// ============================
CREATE TABLE IF NOT EXISTS version 
(
	version_no VARCHAR(20) PRIMARY KEY NOT NULL
);

INSERT INTO version (version_no) VALUES ('0.1-SNAPSHOT');

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
// Issued shares have a UUID of 0
CREATE TABLE share
(
	company_id INT NOT NULL,
	
	// Make nullable for issued shares with no owner
	uuid BINARY(16) NOT NULL,
	
	count INT NOT NULL
);

ALTER TABLE share ADD PRIMARY KEY (company_id, uuid);

// APPLICATION
// ============================
// Applications track employees applying for job positions at a company
CREATE TABLE application 
(
	employee_id VARBINARY(16) NOT NULL,
	company_id INT NOT NULL
);

// PERIODS
// ============================
// Periods track all financial periods created in the game. Users can query this table through commands
// in order to look back on previous financial data in the game
CREATE TABLE period 
(
	id INT AUTO_INCREMENT,
	name varchar(50) NOT NULL,
	start_date TIMESTAMP NOT NULL, 
	end_date TIMESTAMP
);

INSERT INTO period (name, start_date) VALUES ('Opening', '1900-01-01');

// PERFORMANCE
// ============================
// Performance statistics are snapshots of company performance at the end of each period
CREATE TABLE performance
(
	company_id INT NOT NULL, 
	period_id INT NOT NULL,
	company_name NVARCHAR(60) NOT NULL,
	bonuses DECIMAL(20, 5) NOT NULL, 
	dividends DECIMAL(20, 5) NOT NULL, 
	growth DECIMAL(20, 5) NOT NULL, 
	overheads DECIMAL(20, 5) NOT NULL, 
	profit DECIMAL(20, 5) NOT NULL, 
	salary DECIMAL(20, 5) NOT NULL, 
	turnover DECIMAL(20, 5) NOT NULL, 
	value DECIMAL(20, 5) NOT NULL
);


ALTER TABLE performance ADD PRIMARY KEY (company_id, period_id);