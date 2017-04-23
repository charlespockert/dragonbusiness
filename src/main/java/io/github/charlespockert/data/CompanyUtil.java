package io.github.charlespockert.data;

public class CompanyUtil {
	public static String getAccountId(String companyName, String accountPrefix) {
		return accountPrefix + "_" + companyName;

	}
}
