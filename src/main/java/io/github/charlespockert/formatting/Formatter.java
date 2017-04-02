package io.github.charlespockert.formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.stringtemplate.v4.ST;
import com.google.inject.Inject;

import io.github.charlespockert.config.ConfigurationManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Formatter {

	private static char delimiter = 28; 	  // file separator
	private static char colorIdentifier = 29; // group separator
	private static char lineFeed = 30;	  // line feed :P

	@Inject 
	private Logger logger;

	@Inject
	protected ConfigurationManager configurationManager;

	protected Text getPadding() {
		CommentedConfigurationNode config = configurationManager.getConfiguration(ConfigurationManager.MESSAGES);

		return Text.of(config.getNode("messages", "padding").getString());
	}

	private String delimitColor(TextColor color) {
		return String.valueOf(delimiter) + String.valueOf(colorIdentifier) + color.toString() + String.valueOf(delimiter);		
	}

	private String delimitLineFeed() {
		return String.valueOf(delimiter) + String.valueOf(lineFeed) + String.valueOf(delimiter);		
	}

	protected Text getText(String text) {		
		return getText(text, null);
	}

	protected Text getText(String text, Object data) {		
		ST st = new ST(text);
		//		st.groupThatCreatedThisInstance.registerRenderer(PaddedStringRenderer.class, new PaddedStringRenderer());

		if(data != null)
			st.add("data", data);

		st.addAggr("control.{NEWLINE}", delimitLineFeed());

		st.addAggr("colors.{AQUA,BLACK,BLUE,"
				+ "DARK_AQUA,DARK_BLUE,DARK_GRAY,"
				+ "DARK_GREEN,DARK_PURPLE,DARK_RED,"
				+ "GOLD,GRAY,GREEN,LIGHT_PURPLE,"
				+ "NONE,RED,RESET,WHITE,YELLOW}", 
				delimitColor(TextColors.AQUA), 
				delimitColor(TextColors.BLACK), 
				delimitColor(TextColors.BLUE), 
				delimitColor(TextColors.DARK_AQUA), 
				delimitColor(TextColors.DARK_BLUE), 
				delimitColor(TextColors.DARK_GRAY),
				delimitColor(TextColors.DARK_GREEN), 
				delimitColor(TextColors.DARK_PURPLE), 
				delimitColor(TextColors.DARK_RED),
				delimitColor(TextColors.GOLD), 
				delimitColor(TextColors.GRAY), 
				delimitColor(TextColors.GREEN), 
				delimitColor(TextColors.LIGHT_PURPLE),
				delimitColor(TextColors.NONE), 
				delimitColor(TextColors.RED), 
				delimitColor(TextColors.RESET), 
				delimitColor(TextColors.WHITE), 
				delimitColor(TextColors.YELLOW));

		// Render the text out, tokenise and recombine into a Text object
		String[] parts = st.render().split(String.valueOf(delimiter));

		ArrayList<Object> texts = new ArrayList<Object>();

		for(String str : parts) {
			if(str.length() > 0){
				if(str.charAt(0) == colorIdentifier) {
					switch(str.substring(1).toUpperCase()) {
					case "AQUA":
						texts.add(TextColors.AQUA);
						break;
					case "BLACK":
						texts.add(TextColors.BLACK);
						break;
					case "BLUE":
						texts.add(TextColors.BLUE);
						break;
					case "DARK_AQUA":
						texts.add(TextColors.DARK_AQUA);
						break;
					case "DARK_BLUE":
						texts.add(TextColors.DARK_BLUE);
						break;
					case "DARK_GRAY":
						texts.add(TextColors.DARK_GRAY);
						break;
					case "DARK_GREEN":
						texts.add(TextColors.DARK_GREEN);
						break;
					case "DARK_PURPLE":
						texts.add(TextColors.DARK_PURPLE);
						break;
					case "DARK_RED":
						texts.add(TextColors.DARK_RED);
						break;
					case "GOLD":
						texts.add(TextColors.GOLD);
						break;
					case "GRAY":
						texts.add(TextColors.GRAY);
						break;
					case "GREEN":
						texts.add(TextColors.GREEN);
						break;
					case "LIGHT_PURPLE":
						texts.add(TextColors.LIGHT_PURPLE);
						break;
					case "NONE":
						texts.add(TextColors.NONE);
						break;
					case "RED":
						texts.add(TextColors.RED);
						break;
					case "RESET":
						texts.add(TextColors.RESET);
						break;
					case "WHITE":
						texts.add(TextColors.WHITE);
						break;
					case "YELLOW":
						texts.add(TextColors.YELLOW);
						break;
					}
				} else if(str.charAt(0) == lineFeed) {
					texts.add(Text.NEW_LINE);
				} else {
					texts.add(Text.of(str));
				}
			}
		}

		return Text.of(texts.toArray());
	}

	public void sendList(MessageReceiver messageReceiver, List<?> data, String heading, String itemText) {
		formatList(data, heading, itemText).sendTo(messageReceiver);
	}

	public PaginationList.Builder formatList(List<?> data, String heading, String itemText) {
		PaginationList.Builder builder = PaginationList.builder();

		builder.title(getText(heading)).padding(getPadding());

		builder.contents(data.stream().map(listItem -> getText(itemText, listItem)).collect(Collectors.toList()));

		return builder;
	}

	public void sendText(MessageReceiver messageReceiver, Object data, String text) {		
		messageReceiver.sendMessage(getText(text, data));
	}

	public Text formatText(Object data, String text) {		
		return getText(text, data);
	}

}
