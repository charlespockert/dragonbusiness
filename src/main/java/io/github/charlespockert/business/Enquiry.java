package io.github.charlespockert.business;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import io.github.charlespockert.config.MessagesConfig;
import io.github.charlespockert.data.dao.DaoContainer;
import io.github.charlespockert.data.dto.CompanyDto;
import io.github.charlespockert.data.dto.EmployeeDto;
import io.github.charlespockert.data.dto.EmployeeRank;
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

	public void companyDetailedInformation(CommandSource src, int companyId) throws Exception {

		CompanyDto company = daoContainer.companies().getById(companyId);
		EmployeeDto ceo = daoContainer.employees().getCompanyOwner(company.id);

		ArrayList<Text> texts = new ArrayList<Text>();

		// General company info
		texts.add(Text.of(formatter.formatText(company, messagesConfig.info.company_name, false)));
		texts.add(Text.of(formatter.formatText(ceo, messagesConfig.info.owner, false)));

		// Performance data
		// formatter.formatText(company, messagesConfig.info.company);

		// Employee list
		texts.add(Text.of(formatter.formatText(null, messagesConfig.info.employee_list_title, false)));

		List<EmployeeDto> employees = daoContainer.employees().getByCompanyId(company.id);
		for (EmployeeDto emp : employees) {
			if (emp.rank != EmployeeRank.CEO) {
				texts.add(Text.of(formatter.formatText(emp, messagesConfig.info.employee_info, false)));
			}
		}

		Text[] textArr = new Text[texts.size()];
		textArr = texts.toArray(textArr);

		formatter.sendPaged(src, messagesConfig.info.page_title, textArr);
	}

	public void companyListing(CommandSource src, String filter) throws Exception {
		formatter.sendList(src, daoContainer.companyStats().getSummary(filter), messagesConfig.list.heading,
				messagesConfig.list.item);
	}

	public void companyTopPerformers(int maxCount) {

	}

	public void companyDetailedPerformance(int companyId) {

	}

	public void companyEmployeeListing(CommandSource src, String companyName) {

	}
}
