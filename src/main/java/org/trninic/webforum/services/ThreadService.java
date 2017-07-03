package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.Thread;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadService {
	private ObjectMapper om;
	private File threadFile;
	public ThreadService() {
		// TODO Auto-generated constructor stub
		om = new ObjectMapper();
		threadFile = new File(this.getClass().getClassLoader().getResource("../../resources/threads.json").getPath());
	}

	public ArrayList<Thread> getThreads() throws JsonParseException, JsonMappingException, IOException {

		return om.readValue(threadFile, om.getTypeFactory()
				.constructCollectionType(ArrayList.class, Thread.class));
	}
	public Collection<Thread> getThreads(int begining, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getThreads().subList(begining, begining+maxResults);
	}
	
	public ArrayList<Thread> getThreads(String sectionID) throws JsonParseException, JsonMappingException, IOException{
		return getThreads().stream()
				.filter((thread)->thread.getParentSection().equals(sectionID))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<Thread> getThreads(String sectionID, int begin, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getThreads(sectionID).stream()
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Thread getThread(String naturalID) throws JsonParseException, JsonMappingException, IOException{
		return getThreads().stream()
				.filter((thread) -> thread.getNaturalID().equals(naturalID))
				.findFirst()
				.orElse(null);
	}
	public Section getParentSection(String naturalID) throws JsonParseException, JsonMappingException, IOException{
		Thread thisThread = getThread(naturalID);
		SectionService sectionService = new SectionService();
		ArrayList<Section> allSections = sectionService.getSections();
		return allSections.stream()
				.filter((section) -> section.getNaturalID().equals(thisThread.getParentSection()))
				.findFirst()
				.orElse(null);
	}
	
	
	public boolean addThread(Thread newThread) {
		ArrayList<Thread> allThread;
		try {
			allThread = getThreads();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		Thread foundThread = allThread.stream()
				.filter((thread) -> thread.getNaturalID().equals(newThread.getNaturalID()))
				.findFirst()
				.orElse(null);
		if(foundThread != null){
			return false;
		}
		allThread.add(newThread);
		try {
			om.writeValue(threadFile, allThread);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean updateThread(Thread newThread){
		ArrayList<Thread> allThread;

		try {
			allThread = getThreads();
			int i;
			boolean found = false;
			for(i = 0; i < allThread.size() ; i++){
				if(allThread.get(i).getNaturalID().equals(newThread.getNaturalID())){
					allThread.set(i, newThread);
					found = true;
					break;
				}
			}
			if(found)
				om.writeValue(threadFile, allThread);
			return found;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteThread(String naturalID){
		ArrayList<Thread> allThread;
		try {
			allThread = getThreads();
			boolean success = allThread.removeIf((thread) -> thread.getNaturalID().equals(naturalID));
			if(success){
				om.writeValue(threadFile, allThread);
			}
			return success;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


}
