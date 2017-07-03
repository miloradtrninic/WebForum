package org.trninic.webforum.beans;

import java.util.ArrayList;

public class ThreadProxy {
	private Thread thread;
	private ArrayList<Comment> comments;
	
	public ThreadProxy() {
		// TODO Auto-generated constructor stub
		comments = new ArrayList<>();
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
}
