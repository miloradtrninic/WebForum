package org.trninic.webforum.json;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ThreadConverter extends StdConverter<org.trninic.webforum.beans.Thread, String>{

	@Override
	public String convert(org.trninic.webforum.beans.Thread value) {
		// TODO Auto-generated method stub
		return value.getNaturalID();
	}

}
