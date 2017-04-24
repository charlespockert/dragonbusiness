package io.github.charlespockert.business;

import java.util.List;

import org.spongepowered.api.command.CommandSource;

import com.google.inject.Inject;

import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.formatting.Formatter;

public class Enquiry {

	private DaoContainer daoContainer;

	private Formatter formatter;

	private MessagesConfig messagesConfig;

	@Inject
	public Enquiry(DaoContainer daoContainer, Formatter formatter, MessagesConfig messagesConfig) {
		this.daoContainer = daoContainer;
		this.formatter = formatter;
		this.messagesConfig = messagesConfig;
	}

	public void companyDetailedInformation(CommandSource src, String companyName) throws Exception {

		CompanyDto company = daoContainer.companies().getByName(companyName);
		EmployeeDto ceo = daoContainer.employees().getCompanyOwner(company.id);

		// General company info
		formatter.sendText(src, company, messagesConfig.info.company_name);
		formatter.sendText(src, ceo, messagesConfig.info.owner);

		// Performance data
		// formatter.formatText(company, messagesConfig.info.company);

		// Paginated employee list
		List<EmployeeDto> employees = daoContainer.employees().getByCompanyId(company.id);
		formatter.sendList(src, employees, "Employees", messagesConfig.info.employee_info);
	}

	public void companyListing(CommandSource src, String filter) throws Exception {
		formatter.sendList(src, daoContainer.companies().getSummary(filter), messagesConfig.list.heading,
				messagesConfig.list.item);
	}

	public void companyTopPerformers(int maxCount) {

	}

	public void companyDetailedPerformance(int companyId) {

	}

	public void companyEmployeeListing(CommandSource src, String companyName) {

	}
}
