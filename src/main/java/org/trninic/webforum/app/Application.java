package org.trninic.webforum.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.trninic.webforum.beans.Comment;
import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.TestJacson;
import org.trninic.webforum.beans.Thread;
import org.trninic.webforum.beans.Thread.ThreadType;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.beans.User.RoleEnum;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Application {
	
	

	
	
	
	private static Application instance = null;
	/*private SessionFactory sessionFactory;
	private Application(){
		Configuration config = new Configuration().configure();
		sessionFactory = config.buildSessionFactory();
	}*/
	public static Application getInstance(){
		if(instance ==null){
			instance = new Application();
			return instance;
		}
		return instance;
	}
	
	/*public SessionFactory getSessionFactory() {
		return sessionFactory;
	}*/
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Section moviesSection = new Section();
		moviesSection.setDescription("Description");
		moviesSection.setImagePath("image");
		moviesSection.setId(1);
		moviesSection.setNaturalID("movieSection");
		moviesSection.setTitle("Movies section");
		moviesSection.setRules(new ArrayList<String>(){{add("rule 1"); add("rule 2"); add("rule 3"); add("rule 4"); }});

		User headMod = new User();
		headMod.setEmail("email");
		headMod.setName("name");
		headMod.setSurname("surname");
		headMod.setUsername("headmod");
		headMod.setPassword("pwd");
		headMod.setPhoneNum("555-333");
		headMod.setRole(RoleEnum.MODERATOR);
		headMod.setRegisterDate(new Date());
		
		moviesSection.setHeadModerator(headMod.getUsername());
		moviesSection.getModerators().add(headMod.getUsername());
		// section ispunjen
		
		Thread thread = new Thread();
		thread.setAuthor(headMod.getUsername());
		thread.setContent("sadrzaj");
		thread.setNaturalID("supramen");
		thread.setCreated(new Date());
		thread.setLikes(5);
		thread.setDislikes(1);
		thread.setParentSection(moviesSection.getNaturalID());
		thread.setTitle("Superman");
		thread.setType(ThreadType.TEXT);
		
		
		Comment comment1 = new Comment();
		comment1.setAuthor(headMod.getUsername());
		comment1.setDate(new Date());
		comment1.setDislikes(2);
		comment1.setLikes(1);
		comment1.setEdited(true);
		comment1.setId(1);
		comment1.setText("my text");
		comment1.setThread(thread.getNaturalID());
		
		Comment comment2 = new Comment();
		comment2.setAuthor(headMod.getUsername());
		comment2.setDate(new Date());
		comment2.setDislikes(2);
		comment2.setId(2);
		comment2.setLikes(1);
		comment2.setEdited(true);
		comment2.setText("my text 2");
		comment2.setThread(thread.getNaturalID());
		
		comment2.setParent(comment1.getId());
		comment1.getSubComments().add(comment1.getId());
		
		thread.getComments().add(comment1.getId());
		thread.getComments().add(comment2.getId());
		
		headMod.getMyComments().add(comment1.getId());
		headMod.getMyComments().add(comment2.getId());
		
		headMod.getMyThreads().add(thread.getNaturalID());
		headMod.getSubscribedSections().add(moviesSection.getNaturalID());
		
		TestJacson test = new TestJacson();
		test.setName("Ime");
		test.setUsername("Username");
		ObjectMapper om = new ObjectMapper();
		File threadFile = new File(Application.class.getClassLoader().getResource("").getPath());
		ArrayList<Thread> threads = new ArrayList<>();
		threads.add(thread);
		threads.add(thread);
		threads.add(thread);
		//om.enable(SerializationFeature.INDENT_OUTPUT).writeValue(threadFile, threads);
		
		File sectionFile = new File(Application.class.getClassLoader().getResource("").getPath());
		ArrayList<Section> sections = new ArrayList<>();
		sections.add(moviesSection);
		sections.add(moviesSection);
		sections.add(moviesSection);
		//om.enable(SerializationFeature.INDENT_OUTPUT).writeValue(sectionFile, sections);
		
		File commentFile = new File(Application.class.getClassLoader().getResource("").getPath());
		ArrayList<Comment> comments = new ArrayList<>();
		comments.add(comment1);
		comments.add(comment2);
//	om.enable(SerializationFeature.INDENT_OUTPUT).writeValue(commentFile, comments);
		
		ArrayList<User> users = new ArrayList<>();
		users.add(headMod);
		users.add(headMod);
		//File userFile  = new File(Application.class.getClassLoader().getResource("").getPath());
		//System.out.println(om.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(threads));
		
		System.out.println(om.writeValueAsString(headMod));
		
	/*	SectionService sectionService = new SectionService();
		List<Section> listaSekcija = sectionService.getSections();
		listaSekcija.forEach((sekcija) -> System.out.println(sekcija));
		*/
		
		/*Configuration config = new Configuration().configure();
		SessionFactory sessionFactory = config.buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(thread);
		session.save(moviesSection);
		session.save(headMod);
		session.save(comment1);
		session.save(comment2);
		
		session.getTransaction().commit();
		session.close();*/
		
		
		
	}

}
