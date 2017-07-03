package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.trninic.webforum.beans.Comment;
import org.trninic.webforum.beans.Message;
import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.Thread;
import org.trninic.webforum.beans.Ticket;
import org.trninic.webforum.beans.Ticket.TicketEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TicketService {
	private ObjectMapper om;
	private File ticketsFile;
	private SectionService sectionService = new SectionService();
	private CommentService commentService = new CommentService();
	private ThreadService threadService = new ThreadService();
	private MessageService messageService = new MessageService();
	
	public TicketService() {
		// TODO Auto-generated constructor stub
		om = new ObjectMapper();
		ticketsFile = new File(this.getClass().getClassLoader().getResource("../../resources/tickets.json").getPath());
	}
	
	
	
	public ArrayList<Ticket> getTickets() throws JsonParseException, JsonMappingException, IOException {
		return om.readValue(ticketsFile, om.getTypeFactory()
				.constructCollectionType(ArrayList.class, Ticket.class));
	}
	public Ticket submitTicket(Ticket ticket) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Ticket> allTickets = getTickets();
		ticket.setId(allTickets.size() + 1);
		allTickets.add(ticket);
		om.writeValue(ticketsFile, allTickets);
		return ticket;
		
	}
	public ArrayList<Ticket> getUserTicket(String username) throws JsonParseException, JsonMappingException, IOException {
		return getTickets()
				.stream()
				.filter(ticket -> ticket.getAuthorUsername().equals(username))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	
	
	public ArrayList<Ticket> getAdminTickets() throws JsonParseException, JsonMappingException, IOException {
		return getTickets();
	}
	public ArrayList<Ticket> getModTickets(String username) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Ticket> modTickets = new ArrayList<>();
		ArrayList<Ticket> allTickets = getTickets();
		for(Ticket ticket:allTickets){
			if(ticket.getType().equals(TicketEntity.THREAD)){
				if(checkThread(ticket.getEntity(), username)){
					modTickets.add(ticket);
				}
			} else if(ticket.getType().equals(TicketEntity.SECTION)){
				if(checkSection(ticket.getEntity(), username))
					modTickets.add(ticket);
			} else if(ticket.getType().equals(TicketEntity.COMMENT)){
				checkComment(ticket.getEntity(), username);
			}
		}
		return modTickets;
	}
	
	public boolean resolveTicket(Ticket ticket, String modUsername) throws JsonParseException, JsonMappingException, NumberFormatException, IOException{
		if(ticket.getResponse().equals(Ticket.TicketResponse.DELETE)){
			if(ticket.getType().equals(TicketEntity.COMMENT)){
				commentService.removeCommet(Long.parseLong(ticket.getEntity()));
			} else if(ticket.getType().equals(TicketEntity.THREAD)){
				threadService.deleteThread(ticket.getEntity());
			} else if(ticket.getType().equals(TicketEntity.SECTION)){
				sectionService.removeSection(ticket.getEntity());
			}
		} else if(ticket.getResponse().equals(Ticket.TicketResponse.WARN)){
			Message message = new Message();

			message.setSeen(false);
			message.setSender(modUsername);
			
			if(ticket.getType().equals(TicketEntity.COMMENT)){
				Comment comment = commentService.getComment(Long.parseLong(ticket.getEntity()));
				message.setRecepient(comment.getAuthor());
				message.setContent("You have been warned because of your comment. Comment is on thread http://localhost:8080/#/thread/" + comment.getThread());
				
			} else if(ticket.getType().equals(TicketEntity.THREAD)){
				Thread thread = threadService.getThread(ticket.getEntity());
				message.setRecepient(thread.getAuthor());
				message.setContent("You have been warned because of your thread. Thread is http://localhost:8080/#/thread/" + ticket.getEntity());
				System.out.println("posalji poruku za thread");
			} else if(ticket.getType().equals(TicketEntity.SECTION)){
				Section section = sectionService.getSection(ticket.getEntity());
				message.setRecepient(section.getHeadModerator());
				message.setContent("You have been warned because of your section. Section is on thread http://localhost:8080/#/section/" + ticket.getEntity());
			}
			messageService.sendMessage(message);
		}
		return updateTicket(ticket);
	}
	
	public boolean updateTicket(Ticket toUpdate) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Ticket> allTickets = getTickets();
		int index = IntStream.range(0, allTickets.size())
				.filter(i -> allTickets.get(i).getId() == toUpdate.getId())
				.findFirst()
				.orElse(-1);
		if(index == -1) {
			return false;
		}
		allTickets.set(index, toUpdate);
		om.writeValue(ticketsFile, allTickets);
		return true;
	}
	
	protected boolean checkThread (String threadID, String username) throws JsonParseException, JsonMappingException, IOException{
		Thread entity = threadService.getThread(threadID);
		if(entity==null)
			return false;
		
		Section section = sectionService.getSection(entity.getParentSection());
		if(section==null)
			return false;
		
		if(section.getHeadModerator().equals(username) || section.getModerators().contains(username))
			return true;
		return false;
	}
	protected boolean checkSection (String sectionID, String username) throws JsonParseException, JsonMappingException, IOException{
		Section section = sectionService.getSection(sectionID);
		if(section==null)
			return false;
		
		if(section.getHeadModerator().equals(username) || section.getModerators().contains(username))
			return true;
		return false;
	}
	protected boolean checkComment (String commentID, String username) throws JsonParseException, JsonMappingException, IOException{
		Comment comment = commentService.getComment(Long.parseLong(commentID));
		if(comment==null)
			return false;
		return checkThread(comment.getThread(), username);
		
	}
	
	
	
	
	
}
