package org.trninic.webforum.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.trninic.webforum.beans.Comment;
import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.Thread;
import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class UserDeserializer  {

	/*
	 * 
	 
	private static final long serialVersionUID = 1L;

	public UserDeserializer(){
		this(null);
	}
	public UserDeserializer(Class<?> vc) {
		super(vc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 


	@Override
	public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		JsonNode node = p.getCodec().readTree(p);
		User user = new User();
		user.setId(node.get("id").longValue());
		user.setUsername(node.get("username").textValue());
		user.setPassword(node.get("password").textValue());
		switch(node.get("role").textValue()){
		case "ADMIN":
			user.setRole(User.RoleEnum.ADMIN);
			break;
		case "MODERATOR":
			user.setRole(User.RoleEnum.MODERATOR);
			break;
		case "SUBSCRIBER":
			user.setRole(User.RoleEnum.SUBSCRIBER);
			break;
		default:
			user.setRole(User.RoleEnum.SUBSCRIBER);
			break;
		}
		user.setName(node.get("name").textValue());
		user.setSurname(node.get("surname").textValue());
		user.setPhoneNum(node.get("phoneNum").textValue());
		user.setEmail(node.get("email").textValue());
		//TODO DATE
		//user.setRegisterDate(node.get("registerDate"));




		File source = new File("src/main/resources/sections.json");
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<Section> allSections = mapper.readValue(source, mapper.getTypeFactory().constructCollectionType(ArrayList.class, Section.class));
		ArrayList<String> subSectionIDs = mapper.readValue(node.get("subscribedSections").asText(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class));
		for(Section section : allSections){
			if(subSectionIDs.contains(section.getNaturalID()))
				user.subscribeToSection(section);
		}

		source = new File("src/main/resources/threads.json");
		ArrayList<org.trninic.webforum.beans.Thread> allThreads = mapper.readValue(source, mapper.getTypeFactory().constructCollectionType(ArrayList.class, org.trninic.webforum.beans.Thread.class));
		ArrayList<String> myThreadID = mapper.readValue(node.get("myThreads").asText(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class));
		for(Thread thread : allThreads){
			if(myThreadID.contains(thread.getNaturalID()))
				user.addThread(thread);
		}




		source = new File("src/main/resources/comments.json");
		ArrayList<Comment> allComments = mapper.readValue(source, mapper.getTypeFactory().constructCollectionType(ArrayList.class, Comment.class));
		ArrayList<Long> myCommentsIDs = mapper.readValue(node.get("myComments").asText(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, Long.class));
		for(Comment comment : allComments){
			if(myCommentsIDs.contains(comment.getId()))
				user.addComment(comment);
		}

		return user;
	}*/

}
