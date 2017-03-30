package io.github.charlespockert.formatting;

import java.util.HashMap;

import org.spongepowered.api.text.format.TextColors;

public class FormatterData {

	public Object data;
	
	public HashMap<String, TextColors> colors;
	
	public FormatterData(Object source) {
		data = source;
		
		
	}
}
