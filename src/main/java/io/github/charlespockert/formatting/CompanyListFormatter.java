package io.github.charlespockert.formatting;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

import io.github.charlespockert.entities.Company;

public class CompanyListFormatter implements Formatter {

	private List<Company> companies;
	
	public CompanyListFormatter(List<Company> companies) {
		this.companies = companies;
	}
	
	@Override
	public void format(MessageReceiver messageReceiver) {
		PaginationList.Builder builder = PaginationList.builder();

		builder.title(Text.of("Company Listing"))
		.padding(Text.of("="));
		
		builder.contents(companies.stream().map(c -> Text.of(c.getName())).collect(Collectors.toList()));
	
		builder.sendTo(messageReceiver);
	}


}
