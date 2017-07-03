package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SectionService {

	
	
	private ObjectMapper om;
	private File sectionFile;
	public SectionService() {
		om = new ObjectMapper();
		sectionFile = new File(this.getClass().getClassLoader().getResource("../../resources/sections.json").getPath());
		
	}

	public ArrayList<Section> getSections() throws JsonParseException, JsonMappingException, IOException{
		
		return om.readValue(sectionFile, om.getTypeFactory()
				.constructCollectionType(ArrayList.class, Section.class));
	}
	public List<Section> getSections(int begining, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getSections().subList(begining, begining + maxResults);
	}

	public Section getSection(String naturalID) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<Section> allSections = getSections();
		return allSections.stream()
				.filter((section) -> section.getNaturalID().equals(naturalID))
				.findFirst()
				.orElse(null);
	}

	public ArrayList<User> getMods(String naturalID) throws JsonParseException, JsonMappingException, IOException{
		Section thisSection = getSection(naturalID);
		UserServices userResources = new UserServices();
		ArrayList<User> allUsers = userResources.getUsers();
		return allUsers.stream()
				.filter((user) -> thisSection.getModerators().contains(user.getUsername()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public User getHeadMod(String naturalID) throws JsonParseException, JsonMappingException, IOException {
		Section thisSection = getSection(naturalID);
		UserServices userResources = new UserServices();
		ArrayList<User> allUsers = userResources.getUsers();
		return allUsers.stream()
				.filter((user) -> thisSection.getHeadModerator().equals(user.getUsername()))
				.findFirst()
				.orElse(null);
	}
	public boolean addSection(Section newSection) throws JsonParseException, JsonMappingException, IOException {
		if(getSection(newSection.getNaturalID()) != null){
			return false;
		}
		ArrayList<Section> allSections = getSections();
		allSections.add(newSection);
		om.writeValue(sectionFile, allSections);
		return true;
	}
	public boolean updateService(Section section){
		ArrayList<Section> allSections;

		try {
			allSections = getSections();
			int i;
			boolean found = false;
			for(i = 0; i < allSections.size() ; i++){
				if(allSections.get(i).getNaturalID().equals(section.getNaturalID())){
					allSections.set(i, section);
					found = true;
					break;
				}
			}
			if(found)
				om.writeValue(sectionFile, allSections);

			return found;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean removeSection(String naturalID){
		ArrayList<Section> allSections;
		try {
			allSections = getSections();
			boolean success = allSections.removeIf((section) -> section.getNaturalID().equals(naturalID));
			if(success){
				om.writeValue(sectionFile, allSections);
			}
			return success;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*	public Section getSection(long id){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Section section = session.get(Section.class, id);

		session.getTransaction().commit();
		session.close();
		return section;
	}
	public Section getSection(String naturalID){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from Section where naturalID = :id");
		query.setParameter("id", naturalID);
		query.setFirstResult(0);
		query.setMaxResults(1);
		Section section = (Section) query.list().get(0);
		session.getTransaction().commit();
		session.close();
		return section;
	}
	public Collection<Section> getAllSections(){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Query<Section> query = session.createQuery("from Section");
		return query.list();
	}
	public Collection<Section> getSections(int begining, int maxResults){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Query<Section> query = session.createQuery("from Section");
		query.setFirstResult(begining);
		query.setMaxResults(maxResults);
		return query.list();
	}

	public User getSectionHeadMod(long id){
		Section section = getSection(id);
		return section.getHeadModerator();
	}
	 */

}
