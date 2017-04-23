package io.github.charlespockert.data.h2;

import javax.inject.Singleton;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@Singleton
@ConfigSerializable
public class H2QueriesConfig {

	@Setting
	public Company company = new Company();

	@Setting
	public Employee employee = new Employee();

	@Setting
	public Transaction transaction = new Transaction();

	@Setting
	public Share share = new Share();

	@ConfigSerializable
	public static class Company {
		@Setting
		public String count_by_name = "select count(*) from company where name = ?";
		@Setting
		public String count_by_id = "select count(*) from company where id = ?";
		@Setting
		public String get_owner = "select employee.* from company inner join employee on company.id = employee.company_id where employee.rank = ?";
		@Setting
		public String get_by_name = "select * from company where name = ?";
		@Setting
		public String get_by_id = "select * from company where id = ?";
		@Setting
		public String get_by_employee_id = "select company.* from company INNER JOIN employee ON employee.company_id = company.id where employee.uuid = ?";
		@Setting
		public String get_all = "select * from company";
		@Setting
		public String get_summary = "select company.name, employee.name as owner, (select count(*) from employee where employee.company_id = company.id) as employeecount, company.value from company inner join employee on employee.company_id = company.id and employee.rank = 3";
		@Setting
		public String create = "insert into company (name, bankrupt, shares_issued, value) values (?, 0, 0, 0)";
		@Setting
		public String update = "update company set name = ?, bankrupt = ?, shares_issued = ?, value = ?";
		@Setting
		public String delete = "delete from company where id = ?";
	}

	@ConfigSerializable
	public static class Employee {
		@Setting
		public String count_by_id = "select count(*) from employee where uuid = ?";
		@Setting
		public String count_by_name = "select count(*) from employee where name = ?";
		@Setting
		public String get_by_name = "select * from employee where name = ?";
		@Setting
		public String get_by_id = "select * from employee where uuid = ?";
		@Setting
		public String get_by_company_id = "select * from employee where company_id = ?";
		@Setting
		public String get_all = "select * from employee";
		@Setting
		public String get_by_rank = "select * from employee where rank = ?";
		@Setting
		public String create = "insert into employee (uuid, company_id, name, rank, employment_start) values (?, ?, ?, 3, getdate())";
		@Setting
		public String update = "update employee set name = ?, rank = ?";
		@Setting
		public String delete = "delete from employee where uuid = ?";
	}

	@ConfigSerializable
	public static class Transaction {
		@Setting
		public String get_by_id;
		@Setting
		public String get_by_employee_id;
		@Setting
		public String get_by_employee_id_and_period_id;
		@Setting
		public String get_by_company_id_and_period_id;
		@Setting
		public String get_by_company_id;
		@Setting
		public String get_by_period_id;
		@Setting
		public String create;
	}

	@ConfigSerializable
	public static class Share {
		@Setting
		public String get_by_id;
		@Setting
		public String get_by_employee_id;
		@Setting
		public String get_by_company_id;
		@Setting
		public String create;
	}
}
