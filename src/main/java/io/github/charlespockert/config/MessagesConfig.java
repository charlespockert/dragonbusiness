package io.github.charlespockert.config;

import com.google.inject.Singleton;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@Singleton
@ConfigSerializable
public class MessagesConfig {

	@Setting
	public String padding = "=";

	@Setting
	public General general = new General();

	@Setting
	public List list = new List();

	@Setting
	public Create create = new Create();

	@Setting
	public Salary salary = new Salary();

	@Setting
	public Company company = new Company();

	@Setting
	public Info info = new Info();

	@Setting
	public Period period = new Period();

	@ConfigSerializable
	public static class General {
		@Setting
		public String player_only_command = "This command is for players only";
		@Setting
		public String error = "An error occurred, please contact an admin for assistance";
	}

	@ConfigSerializable
	public static class List {
		@Setting
		public String heading = "Company List";
		@Setting
		public String item = "<data.name> - CEO: <data.owner><control.NEWLINE>Employees: <data.employeeCount> - New Worth: <data.value>";
	}

	@ConfigSerializable
	public static class Create {
		@Setting
		public String success = "Your company <data.name> has been created!";
		@Setting
		public String failed = "You do not have enough cash or there is an issue with your account";
		@Setting
		public String already_employed = "You cannot create a company as you are already employed";
	}

	@ConfigSerializable
	public static class Salary {
		@Setting
		public String company_insufficient_funds = "Your company has run out of cash and you cannot be paid!";

		@Setting
		public String salary_paid = "Your salary of <data> has been paid into your account";

		@Setting
		public String payment_issue = "There was a technical issue paying your salary";
	}

	@ConfigSerializable
	public static class Company {
		@Setting
		public String company_bankrupt_broadcast = "<data.name> can no longer meet its financial obligations and has filed for bankruptcy!";
	}

	@ConfigSerializable
	public static class Info {
		@Setting
		public String company_name = "Company: <data.name>";
		@Setting
		public String owner = "CEO: <data.name>";
		@Setting
		public String employee_info = "<data.name> can no longer meet its financial obligations and has filed for bankruptcy!";
		@Setting
		public String employee_list_title = "Employees";
		@Setting
		public String page_title = "Detailed Information";
	}

	@ConfigSerializable
	public static class Period {
		@Setting
		public String period_closed = "Financial period <data> has been closed";
		@Setting
		public String top_company = "<data.name> are the front runners in this period with <data.turnover>";
		@Setting
		public String no_top_company = "There were no front runners in this financial period";
	}

}
