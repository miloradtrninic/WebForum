package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.trninic.webforum.beans.Message;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageService {
	private ObjectMapper om;
	private File messagesFile;
	
	public MessageService() {
		om = new ObjectMapper();
		messagesFile = new File(this.getClass().getClassLoader().getResource("../../resources/messages.json").getPath());
		// TODO Auto-generated constructor stub
	}
	public ArrayList<Message> getMessages() throws JsonParseException, JsonMappingException, IOException{
		return om.readValue(messagesFile, om.getTypeFactory()
				.constructCollectionType(ArrayList.class, Message.class));
	}
	public Collection<Message> getMessages(int begin, int maxResult) throws JsonParseException, JsonMappingException, IOException {
		return getMessages().subList(begin, begin + maxResult);
	}

	public ArrayList<Message> getInbox(String username) throws JsonParseException, JsonMappingException, IOException{
		return getMessages().stream()
				.filter((message) -> message.getRecepient().equals(username))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Collection<Message> getInbox(String username, int begin, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return ((ArrayList<Message>)getInbox(username)).subList(begin, begin + maxResults);
	}
	public ArrayList<Message> getOutbox(String username) throws JsonParseException, JsonMappingException, IOException{
		return getMessages().stream()
				.filter((message)->message.getSender().equals(username))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Collection<Message> getOutbox(String username, int begin, int maxResult) throws JsonParseException, JsonMappingException, IOException{
		return ((ArrayList<Message>)getOutbox(username)).subList(begin, begin+maxResult);
	}
	
	public void sendMessage(Message message) throws JsonGenerationException, JsonMappingException, IOException{
		
		ArrayList<Message> allMsg = getMessages();
		message.setId(allMsg.size() + 1);
		allMsg.add(message);
		om.writeValue(messagesFile, allMsg);
	}
	
	public Message readMessage(long id) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<Message> allMsg = getMessages();
		int index = IntStream
					.range(0, allMsg.size())
					.filter( i -> allMsg.get(i).getId() == id)
					.findFirst()
					.orElse(-1);
		if(index == -1)
			return null;
		
		allMsg.get(index).setSeen(true);
		
		om.writeValue(messagesFile, allMsg);
		return allMsg.get(index);
	}
	
	
}
