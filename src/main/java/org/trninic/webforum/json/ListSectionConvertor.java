package org.trninic.webforum.json;

import java.util.Collection;
import java.util.stream.Collectors;

import org.trninic.webforum.beans.Section;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ListSectionConvertor extends StdConverter<Collection<Section>, Collection<String>>{

	@Override
	public Collection<String> convert(Collection<Section> arg0) {
		// TODO Auto-generated method stub
		return arg0.stream().map(Section::getNaturalID).collect(Collectors.toList());
	}
}
