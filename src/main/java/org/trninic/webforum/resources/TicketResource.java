package org.trninic.webforum.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.trninic.webforum.beans.Ticket;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.beans.User.RoleEnum;
import org.trninic.webforum.services.TicketService;
import org.trninic.webforum.services.UserServices;

@Path("/tickets")
public class TicketResource {
	private TicketService ticketService = new TicketService();
	private UserServices userServices = new UserServices();
	@Context
	HttpServletRequest request;
	
	@Path("/moderator")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	public Response getModTickets(){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		User user;
		try {
			ArrayList<Ticket> tickets = null;
			user = userServices.getUser(username);
			if(user.getRole().equals(RoleEnum.ADMIN)){
				tickets = ticketService.getAdminTickets();
			} else if(user.getRole().equals(RoleEnum.MODERATOR)) {
				tickets = ticketService.getModTickets(username);
			}
			return Response.ok(tickets).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/user")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	public Response getUserTickets(){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		try {
			ArrayList<Ticket> tickets = null;
			tickets = ticketService.getUserTicket(username);
			return Response.ok(tickets).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@Path("/resolve")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	public Response resolveTicket(Ticket ticket){
		try {
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String username = Authentication.validateToken(authorizationHeader);
			if(ticketService.resolveTicket(ticket, username))
				return Response.ok(ticket).build();
			else
				return Response.noContent().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/submit")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response submitTicket(Ticket ticket){
		String authorizationHeader = 
				request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = Authentication.validateToken(authorizationHeader);
		ticket.setDate(new Date());
		ticket.setAuthorUsername(username);
		try {
			ticketService.submitTicket(ticket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok(ticket).build();
		
	}
	
}
