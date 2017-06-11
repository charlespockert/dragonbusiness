package io.github.charlespockert.config;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@Singleton
@ConfigSerializable
public class MainConfig {

	@Setting
	public Database database;

	@Setting
	public Business business;

	public MainConfig() {
		database = new Database();
		business = new Business();
	}

	@ConfigSerializable
	public static class Database {
		@Setting
		public String database_name = "dragonbusiness";
		@Setting
		public String database_user = "sa";
		@Setting
		public String database_pass = "";
	}

	@ConfigSerializable
	public static class Business {
		@Setting
		public int salary_interval_minutes = 3;
		@Setting
		public int fiscal_period_length_minutes = 30;
		@Setting
		public String company_account_prefix = "dragonbusiness2";
		@Setting
		public int salary_amount = 10;
		@Setting
		public int company_setup_fee = 1000;
		@Setting
		public List<String> fiscal_periods;

		public Business() {
			fiscal_periods = new ArrayList<String>();

			fiscal_periods.add("Monday");
			fiscal_periods.add("Tuesday");
			fiscal_periods.add("Wednesday");
			fiscal_periods.add("Thursday");
			fiscal_periods.add("Friday");
			fiscal_periods.add("Saturday");
			fiscal_periods.add("Sunday");
		}
	}
}
