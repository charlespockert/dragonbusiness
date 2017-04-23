package io.github.charlespockert.business;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.api.command.CommandSource;

import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.BusinessRepository;
import io.github.charlespockert.entities.Employee;
import io.github.charlespockert.formatting.Formatter;

public class Enquiry {

	private BusinessRepository businessRepository;

	private Formatter formatter;

	private MessagesConfig messagesConfig;

	public Enquiry(BusinessRepository businessRepository, Formatter formatter, MessagesConfig messagesConfig) {
		this.businessRepository = businessRepository;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
	}

	public void companyDetailedInformation(int companyId) throws Exception {

		businessRepository.companyGet(companyId);

		// PerformanceData perfData =
		// businessRepository.companyGetPerformanceData(company.getId());
		List<Employee> employees = businessRepository.employeeGetByCompanyId(companyId);

		// General company info
		// formatter.formatText(company, messagesConfig.info.company);

		// Performance data
		// formatter.formatText(company, messagesConfig.info.company);

		// Employee list
		for (Employee emp : employees) {

		}

		// formatter.formatPaged(company, messages.getNode("info",
		// "company").getString());

	}

	public void companyListing(CommandSource src, String filter) throws Exception {
		formatter.sendList(src, businessRepository.companyGetSummary(), messagesConfig.list.heading,
				messagesConfig.list.item);
	}

	public void companyTopPerformers(int maxCount) {

	}

	public void companyDetailedPerformance(int companyId) {

	}

	public void companyEmployeeListing(CommandSource src, String companyName) {

	}
}
