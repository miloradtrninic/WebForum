package org.trninic.webforum.json;

import java.util.Collection;
import java.util.stream.Collectors;

import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ListUserConvertor extends StdConverter<Collection<User>, Collection<String>>{

	@Override
	public Collection<String> convert(Collection<User> arg0) {
		// TODO Auto-generated method stub
		return arg0.stream().map(User::getUsername).collect(Collectors.toList());
	}
	
}
