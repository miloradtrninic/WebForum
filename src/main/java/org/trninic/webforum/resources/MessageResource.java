package org.trninic.webforum.resources;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.trninic.webforum.beans.Message;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.services.MessageService;
import org.trninic.webforum.services.UserServices;

@Path("/messages")
@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
public class MessageResource {

	@Context
	HttpServletRequest request;
	private MessageService messageService = new MessageService();
	@Path("/inbox")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInbox(){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		ArrayList<Message> inbox = null;
		try {
			inbox = messageService.getInbox(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		if(inbox.isEmpty()){
			return Response.noContent().build();
		}
		
		return Response.ok(inbox).build();
	}
	
	
	@Path("/outbox")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOutbox(){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		
		ArrayList<Message> outbox = null;
		try {
			outbox = messageService.getOutbox(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		if(outbox.isEmpty()){
			return Response.noContent().build();
		}
		return Response.ok(outbox).build();
	}
	
	@Path("/send")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(Message message){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		message.setSender(username);
		message.setSeen(false);
		
		UserServices userService = new UserServices();
		try {
			if(userService.getUser(message.getRecepient()) == null){
				return Response.status(Status.NOT_FOUND).build();
			}
			messageService.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.ok(message).build();
	}
	
	@Path("/readMessage/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(@PathParam("id") long id){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		Message read = null;
		
		try {
			read = messageService.readMessage(id);
			if(read == null){
				throw new Exception();
			}
			if(!read.getRecepient().equals(username)){
				return Response.status(Status.UNAUTHORIZED).build();
			}
			
			return Response.ok(read).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
	
	}
	
	
}
