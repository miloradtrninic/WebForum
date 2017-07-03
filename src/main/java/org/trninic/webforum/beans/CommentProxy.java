package org.trninic.webforum.beans;

import java.util.Collection;

public class CommentProxy {
	private Comment parent;
	private Collection<CommentProxy> subComments;
	
	
	public Comment getParent() {
		return parent;
	}
	public void setParent(Comment parent) {
		this.parent = parent;
	}
	public Collection<CommentProxy> getSubComments() {
		return subComments;
	}
	public void setSubComments(Collection<CommentProxy> subComments) {
		this.subComments = subComments;
	}
	
	
	
}
