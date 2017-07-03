package org.trninic.webforum.beans;

import java.util.ArrayList;

public class SectionProxy {
	private Section section;
	private ArrayList<Thread> threads = new ArrayList<>();
	
	public SectionProxy() {
		// TODO Auto-generated constructor stub
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public ArrayList<Thread> getThreads() {
		return threads;
	}

	public void setThreads(ArrayList<Thread> threads) {
		this.threads = threads;
	}
	
}
