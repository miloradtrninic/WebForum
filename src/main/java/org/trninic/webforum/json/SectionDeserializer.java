package org.trninic.webforum.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.User;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class SectionDeserializer  {

	/*
	 * 
	
	private static final long serialVersionUID = 1L;
	
	public SectionDeserializer(){
		this(null);
	}
	public SectionDeserializer(Class<?> vc) {
		super(vc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Section deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		JsonNode node = p.getCodec().readTree(p);
		Section section = new Section();
		section.setId(node.get("id").longValue());
		section.setNaturalID(node.get("naturalID").textValue());
		section.setImagePath(node.get("imagePath").textValue());
		section.setTitle(node.get("title").textValue());
		section.setDescription(node.get("description").textValue());
		if(node.get("rules").isArray()){
			for(JsonNode nodeRule : node.get("rules")){
				section.getRules().add(nodeRule.asText());
			}
		}
		File source = new File("src/main/resources/users.json");
		ObjectMapper mapper = new ObjectMapper();
		String headModUsername = node.get("headModerator").textValue();
		ArrayList<User> allUsers = mapper.readValue(source, mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
		for(User user:allUsers ){
			if(user.getUsername().equals(headModUsername)){
				section.setHeadModerator(user);
				break;
			}
		}
		ArrayList<String> modUsernames = mapper.readValue(node.get("moderators").asText(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
		for(User user:allUsers ){
			if(modUsernames.contains(user.getUsername())){
				section.getModerators().add(user);
				break;
			}
		}
		
		return null;
	}
*/
}
