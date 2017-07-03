package org.trninic.webforum.json;

import org.trninic.webforum.beans.Section;

import com.fasterxml.jackson.databind.util.StdConverter;

public class SectionConverter extends StdConverter<Section, String>{

	@Override
	public String convert(Section value) {
		// TODO Auto-generated method stub
		return value.getNaturalID();
	}
	

}
