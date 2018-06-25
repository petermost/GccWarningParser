package com.pera_software.html;

import static org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Option {
	
	private List<String> _names;
	private String _description;
	
	public Option(List<String> names, String description) {
		_names = new ArrayList<>(names);
		_description = description;
	}
	
	public List<String> names() {
		return _names;
	}

	public String description() {
		return _description;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, NO_CLASS_NAME_STYLE)
			.append("name", _names.toArray())
			.append("description", _description)
			.toString();
	}
}
