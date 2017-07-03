package org.trninic.webforum.json;

import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.databind.util.StdConverter;

public class UserConverter extends StdConverter<User, String> {

	@Override
	public String convert(User value) {
		// TODO Auto-generated method stub
		return value.getUsername();
	}
	

}
