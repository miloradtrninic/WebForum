package org.trninic.webforum.json;

import java.util.Collection;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.trninic.webforum.beans.Thread;

public class ListThreadConvertor extends StdConverter<Collection<Thread>, Collection<String>> {

	@Override
	public Collection<String> convert(Collection<Thread> arg0) {
		// TODO Auto-generated method stub
		return arg0.stream().map(Thread::getNaturalID).collect(Collectors.toList());
	}

}
