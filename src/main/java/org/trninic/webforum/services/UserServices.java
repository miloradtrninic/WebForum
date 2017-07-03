package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.Thread;
import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserServices {
	private ObjectMapper om;
	private File userFile;
	private ThreadService threadService = new ThreadService();
	private SectionService sectionService = new SectionService();
	public UserServices() {
		// TODO Auto-generated constructor stub
		om = new ObjectMapper();
		userFile = new File(this.getClass().getClassLoader().getResource("../../resources/users.json").getPath());
	}

	public ArrayList<User> getUsers() throws JsonParseException, JsonMappingException, IOException{
		return om.readValue(userFile, om.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
	}
	public List<User> getUsers(int begining, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getUsers().subList(begining, begining + maxResults);
	}
	public ArrayList<Thread> getModThreads(String username) throws JsonParseException, JsonMappingException, IOException{
		User user = getUser(username);
		if(user.getRole().equals(User.RoleEnum.ADMIN)){
			return threadService.getThreads();
		} else {
			ArrayList<org.trninic.webforum.beans.Thread> myThreads = threadService.getThreads()
					.stream()
					.filter((thread) -> user.getMyThreads().contains(thread.getNaturalID()))
					.collect(Collectors.toCollection(ArrayList::new));
			if(user.getRole().equals(User.RoleEnum.MODERATOR)){
				List<String> sections = sectionService
						.getSections()
						.stream()
						.filter((section) -> section.getModerators().contains(user.getUsername()) || section.getHeadModerator().equals(user.getUsername()))
						.map(Section::getNaturalID)
						.collect(Collectors.toList());
				ArrayList<org.trninic.webforum.beans.Thread> modThreads = threadService.getThreads()
						.stream()
						.filter((thread) -> sections.contains(thread.getParentSection()))
						.collect(Collectors.toCollection(ArrayList::new));
				modThreads.addAll(myThreads);
				return modThreads;
			}
			return myThreads;
		} 
	}
	public ArrayList<Section> getModSections(String username) throws JsonParseException, JsonMappingException, IOException {
		User user = getUser(username);
		if(user.getRole().equals(User.RoleEnum.ADMIN)){
			return sectionService.getSections();
		} else if(user.getRole().equals(User.RoleEnum.MODERATOR)){
			ArrayList<Section> mySections = sectionService.getSections()
					.stream()
					.filter((section) -> section.getHeadModerator().equals(username) || section.getModerators().contains(username))
					.collect(Collectors.toCollection(ArrayList::new));
			return mySections;
		}
		return null;
	}
	
	public User getUser(String username) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<User> allUsers = getUsers();
		return allUsers.stream().filter((user) -> user.getUsername().equals(username)).findFirst().orElse(null);
	}
	
	public ArrayList<Section> getSubscribedSections(String username) throws JsonParseException, JsonMappingException, IOException{
		User user = getUser(username);
		SectionService sectionService = new SectionService();
		ArrayList<Section> allSections = sectionService.getSections();
		return allSections
				.stream()
				.filter((section) -> user.getSubscribedSections().contains(section.getNaturalID()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	
	public boolean addUser(User user) throws JsonParseException, JsonMappingException, IOException{
		if(getUser(user.getUsername()) != null){
			return false;
		}
		ArrayList<User> allUsers = getUsers();
		allUsers.add(user);
		om.writeValue(userFile, allUsers);
		return true;

	}
	public boolean deleteUser(String username) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<User> allUsers = getUsers();
		return allUsers.removeIf((user)->user.getUsername().equals(username));
	}
	public boolean updateUser(User newUser) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<User> allUsers = getUsers();

		int index = IntStream.range(0, allUsers.size())
				 .filter(i -> allUsers.get(i).getUsername().equals(newUser.getUsername()))
				 .findFirst()
				 .orElse(-1);
		
		if(index == -1)
			return false;
		allUsers.set(index, newUser);
		om.writeValue(userFile, allUsers);
		return true;


	}
	/*public User getUser(long id){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = session.get(User.class, id);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public User getUser(String userName){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Query<User> query = session.createQuery("from User where username = :username");
		query.setParameter("username", userName);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<User> listUser = query.list();
		session.getTransaction().commit();
		session.close();
		return listUser.get(0);
	}
	public User addUser(User user){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public int removeUser(String userName){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("DELETE FROM User WHERE username =:username");
		query.setParameter(":username", userName);
		int result = query.executeUpdate();
		session.getTransaction().commit();
		session.close();
		return result;
	}
	public User removeUser(User user){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(user);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public User updateUser(User toUpdate){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = getUser(toUpdate.getUsername());
		toUpdate.setId(user.getId());
		session.update(toUpdate);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public void subscribeToSection(String userName, String sectionID){
		Section section = new SectionService().getSection(sectionID);
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = getUser(userName);
		user.subscribeToSection(section);
		session.update(user);
		session.getTransaction().commit();
		session.close();

	}

	/*
	public User updatePassword(String userName, String newPassword){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = getUser(userName);
		user.setPassword(newPassword);
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public User updateRole(String userName, RoleEnum role){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = getUser(userName);
		user.setRole(role);
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	public User updateUserEmail(String userName, String newEmail){
		Session session = Application.getInstance().getSessionFactory().openSession();
		session.beginTransaction();
		User user = getUser(userName);
		user.setEmail(newEmail);
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return user;
	}
	 */
}
