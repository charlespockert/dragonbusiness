package io.github.charlespockert.formatting;

import java.util.HashMap;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.stringtemplate.v4.ST;

import com.google.inject.Inject;

import io.github.charlespockert.config.ConfigurationManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public abstract class Formatter {
	
	@Inject
	protected ConfigurationManager configurationManager;

	protected Text getPadding() {
		CommentedConfigurationNode config = configurationManager.getConfiguration(ConfigurationManager.MESSAGES);
		
		return Text.of(config.getNode("messages", "padding").getPath());
	}
	
	protected Text getText(String text) {		
		return getText(text, null);
	}
	
	protected Text getText(String text, Object data) {		
		ST st = new ST(text);
		
		if(data != null)
			st.add("data", data);
		
		st.addAggr("colors.{AQUA,BLACK,BLUE,"
				+ "DARK_AQUA,DARK_BLUE,DARK_GRAY,"
				+ "DARK_GREEN,DARK_PURPLE,DARK_RED,"
				+ "GOLD,GRAY,GREEN,LIGHT_PURPLE,"
				+ "NONE,RED,RESET,WHITE,YELLOW}", 
				TextColors.AQUA, TextColors.BLACK, TextColors.BLUE, 
				TextColors.DARK_AQUA, TextColors.DARK_BLUE, TextColors.DARK_GRAY,
				TextColors.DARK_GREEN, TextColors.DARK_PURPLE, TextColors.DARK_RED,
				TextColors.GOLD, TextColors.GRAY, TextColors.GREEN, TextColors.LIGHT_PURPLE,
				TextColors.NONE, TextColors.RED, TextColors.RESET, TextColors.WHITE, TextColors.YELLOW);
		
		return Text.of(st.render());
	}
}
