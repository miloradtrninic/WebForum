package org.trninic.webforum.resources;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.services.ThreadService;
import org.trninic.webforum.services.UserServices;;

@Path("/users")
public class UserResources {
	@Context
	HttpServletRequest request;
	private UserServices userServices = new UserServices();
	private ThreadService threadService = new ThreadService();
	
	@GET
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers(){
		Collection<User> users = null;

		try {
			users = userServices.getUsers();
			System.out.println(users + "users");
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		if(users.isEmpty()){
			return Response
					.noContent()
					.build();
		}
		return Response
				.ok(users)
				.build();
	}

	@GET
	@Path("/{username}")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR,  User.RoleEnum.SUBSCRIBER})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("username") String username){
		User user = null;
		try {
			user = userServices.getUser(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
		if(user==null){
			return Response.noContent().build();
		}
		return Response.ok(user).build();

	}


	@DELETE
	@Path("/delete/{userName}")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("userName") String userName){
		boolean deleted = false; 
		try {
			deleted = userServices.deleteUser(userName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok(deleted).build();
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		try {
			if(!userServices.addUser(user)){
				return Response
						.status(Status.UNAUTHORIZED)
						.build();
			}
			return Response
					.ok(user)
					.build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response
					.serverError()
					.build();
		}
	}

	@Path("/update")
	@POST
	@Secured({User.RoleEnum.ADMIN})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response update(User user){
		try {
			if(userServices.updateUser(user)){
				return Response.ok(user).build();
			}
			return Response.noContent().build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@Path("/threads")
	@GET
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR,User.RoleEnum.SUBSCRIBER})
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getThreads(){
		try {
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String userName = Authentication.validateToken(authorizationHeader);
			
			Collection<org.trninic.webforum.beans.Thread> threads = userServices.getModThreads(userName);
			if(threads.isEmpty()){
				System.out.println("no content");
				return Response.noContent().build();
			}
			System.out.println("vratice treads");
			return Response.ok(threads).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@Path("/subscribed")
	@GET
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getSubThreads(){
		try {
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String userName = Authentication.validateToken(authorizationHeader);
			Collection<Section> sections = userServices.getSubscribedSections(userName);
			if(sections.isEmpty()){
				return Response.noContent().build();
			}
			return Response.ok(sections).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/unsubscribe")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	public Response unsubscribe(Section secion){
		try {
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String username = Authentication.validateToken(authorizationHeader);
			
			User user = userServices.getUser(username);
			if(user == null)
				return Response.status(Status.FORBIDDEN).build();
			user.getSubscribedSections().remove(secion.getNaturalID());
			System.out.println(secion.getNaturalID());
			if(!userServices.updateUser(user)){
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			return Response.ok(userServices.getSubscribedSections(username)).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	
	@Path("/subscribe")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	public Response subscribe(Section secion){
		try {
			
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String userName = Authentication.validateToken(authorizationHeader);
			
			User user = userServices.getUser(userName);
			if(user == null)
				return Response.status(Status.FORBIDDEN).build();
			
			if(!user.getSubscribedSections().contains(secion.getNaturalID()))
				user.getSubscribedSections().add(secion.getNaturalID());
			
			if(!userServices.updateUser(user)){
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
			return Response.ok(userServices.getSubscribedSections(userName)).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	

	@Path("/sectionmod")
	@GET
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getModSections(){
		try {
			
			String authorizationHeader = 
					request.getHeader(HttpHeaders.AUTHORIZATION);
			String userName = Authentication.validateToken(authorizationHeader);
			
			Collection<Section> sections = userServices.getModSections(userName);
			if(sections == null){
				return Response.status(Status.FORBIDDEN).build();
			}
			if(sections.isEmpty()){
				return Response.noContent().build();
			}
			return Response.ok(sections).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}


}
