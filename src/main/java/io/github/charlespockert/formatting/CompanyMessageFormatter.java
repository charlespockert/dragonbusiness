package io.github.charlespockert.formatting;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import io.github.charlespockert.config.ConfigurationManager;
import io.github.charlespockert.entities.Company;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class CompanyMessageFormatter extends Formatter {

	public void formatCompanyList(MessageReceiver messageReceiver, List<Company> companies) {
		CommentedConfigurationNode config = configurationManager.getConfiguration(ConfigurationManager.MESSAGES);

		PaginationList.Builder builder = PaginationList.builder();
		
		builder.title(getText(config.getNode("messages", "list", "list_heading").getString()))
		.padding(getPadding());
		
		builder.contents(companies.stream().map(c -> getText(config.getNode("messages", "list", "list_item").getString(), c)).collect(Collectors.toList()));

		builder.sendTo(messageReceiver);
	}

}
