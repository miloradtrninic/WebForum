package org.trninic.webforum.json;

import java.util.Collection;
import java.util.stream.Collectors;

import org.trninic.webforum.beans.Comment;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ListCommentConverter extends StdConverter<Collection<Comment>, Collection<Long>>{

	@Override
	public Collection<Long> convert(Collection<Comment> arg0) {
		// TODO Auto-generated method stub
		return arg0.stream().map(Comment::getId).collect(Collectors.toList());
	}

}
