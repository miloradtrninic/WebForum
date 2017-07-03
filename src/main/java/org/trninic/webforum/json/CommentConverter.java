package org.trninic.webforum.json;

import org.trninic.webforum.beans.Comment;

import com.fasterxml.jackson.databind.util.StdConverter;

public class CommentConverter extends StdConverter<Comment, Long> {

	@Override
	public Long convert(Comment value) {
		// TODO Auto-generated method stub
		return value.getId();
	}

}
