package org.trninic.webforum.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.trninic.webforum.beans.Thread;
import org.trninic.webforum.beans.Thread.ThreadType;
import org.trninic.webforum.beans.ThreadProxy;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.services.CommentService;
import org.trninic.webforum.services.ThreadService;
import org.trninic.webforum.services.UserServices;


@Path("/threads")
public class ThreadResource {
	private ThreadService threadService = new ThreadService();
	private CommentService commentService = new CommentService();

	@Context
	private  ServletContext context;
	
	/*
	 * 
	@GET
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public ArrayList<org.trninic.webforum.beans.Thread> getThreads(){
		try {
			return threadService.getThreads();
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	 */
	@GET
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getThreads(@QueryParam("begin") int begin,@QueryParam("results") int maxResults){
		try {
			if(begin == 0 && maxResults == 0){
				return Response
						.status(Status.OK)
						.entity(threadService.getThreads())
						.build();

			}
			return Response
					.status(Status.OK)
					.entity(threadService.getThreads(begin, maxResults))
					.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.NOT_ACCEPTABLE)
					.build();
		}
	}
	
	
	@Path("/search")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSections(@FormParam("title") String title, @FormParam("contentType")String contentType, @FormParam("author") String author,@FormParam("sectionID") String sectionID ){
		try {
			List<Thread> returnThreads = threadService.getThreads();
			System.out.println(returnThreads.size());
			if(!title.isEmpty()){
				returnThreads = returnThreads.stream()
												.filter(thread -> thread.getTitle().contains(title.trim()))
												.collect(Collectors.toCollection(ArrayList::new));
			} 
			if(!contentType.isEmpty()){
				returnThreads = returnThreads.stream()
												.filter(thread -> thread.getType().toString().equals(contentType))
												.collect(Collectors.toCollection(ArrayList::new));
			}
			if(!author.isEmpty()){
				returnThreads = returnThreads.stream()
												.filter(thread -> thread.getAuthor().equals(author))
												.collect(Collectors.toCollection(ArrayList::new));
			}
			if(!sectionID.isEmpty()){
				returnThreads = returnThreads.stream()
												.filter(thread -> thread.getParentSection().equals(sectionID))
												.collect(Collectors.toCollection(ArrayList::new));
			}
			
			if(returnThreads.isEmpty()){
				return Response.noContent().build();
			}
			return Response.ok(returnThreads).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	
	}
	
	@GET
	@Path("/suggest")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getSuggestThreads(){
		try {
			ArrayList<Thread> allThreads = threadService.getThreads();
			allThreads.sort((t1, t2) -> t1.getLikes().compareTo(t2.getLikes()));
			if(allThreads.size() < 10 ){
				return Response.ok(allThreads).build();
			}
			return Response.ok(allThreads.subList(0, 10)).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.NOT_ACCEPTABLE)
					.build();
		}
	
	}
	
	@GET
	@Path("/{naturalID}")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getThread(@PathParam("naturalID") String naturalID, @QueryParam("beginComment") int begin, @QueryParam("maxComment") int maxResults ){
		try {

			ThreadProxy proxyThread = new ThreadProxy();
			proxyThread.setThread(threadService.getThread(naturalID));
			if(begin == 0 && maxResults == 0){
				proxyThread.setComments(commentService.getComments(naturalID));
			}else{
				proxyThread.setComments(commentService.getComments(naturalID,begin,maxResults));
			}
			return Response
					.status(Status.OK)
					.entity(proxyThread)
					.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.NOT_ACCEPTABLE)
					.build();
		}
	}

	@POST
	@Path("/newthread")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newThread(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails,
			@FormDataParam("title") String title, @FormDataParam("naturalID") String naturalID,@FormDataParam("type") String type,
			@FormDataParam("author") String author, @FormDataParam("likes") int likes, @FormDataParam("dislikes") int dislikes,
			@FormDataParam("parentSection") String parentSection, @FormDataParam("content") String content, @FormDataParam("created") String created) {
		ThreadType typeEnum = ThreadType.TEXT;



		if(type.equals("IMAGE")){
			typeEnum = ThreadType.IMAGE;
			String uploadedFileLocation = context.getRealPath("/resources/images/") + "/" + fileDetails.getFileName();
			writeToFile(uploadedInputStream, uploadedFileLocation);
			content = "/resources/images/" + fileDetails.getFileName();
		} else if(type.equals("LINK")){
			typeEnum = ThreadType.LINK;
		}

		Thread newThread = new Thread(naturalID, parentSection, title, typeEnum, author, new ArrayList<Long>(), new String(""), new Date(), likes, dislikes);
		newThread.setContent(content);
		try {
			User authorUser;
			UserServices userService = new UserServices();
			authorUser = userService.getUser(author);
			if(!threadService.addThread(newThread)){
				return Response
						.status(Status.NOT_ACCEPTABLE)
						.build();
			}
			authorUser.getMyThreads().add(naturalID);
			userService.updateUser(authorUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response
				.status(Status.CREATED)
				.entity(newThread)
				.build();
	}

	@POST
	@Path("/update")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateThread(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetails,
			@FormDataParam("title") String title, @FormDataParam("naturalID") String naturalID,@FormDataParam("type") String type,
			@FormDataParam("content") String content){

		ThreadType typeEnum = ThreadType.TEXT;

		if(type.equals("IMAGE")){
			typeEnum = ThreadType.IMAGE;
			String uploadedFileLocation = context.getRealPath("/resources/images/") + "/" + fileDetails.getFileName();
			writeToFile(uploadedInputStream, uploadedFileLocation);
			content = "/resources/images/" + fileDetails.getFileName();
		} else if(type.equals("LINK")){
			typeEnum = ThreadType.LINK;
		}
		Thread toEdit;
		try {
			toEdit = threadService.getThread(naturalID);
			toEdit.setContent(content);
			toEdit.setTitle(title);
			toEdit.setType(typeEnum);

			if(!threadService.updateThread(toEdit)){
				return Response
						.status(Status.NOT_ACCEPTABLE)
						.build();
			}
			return Response
					.status(Status.OK)
					.entity(toEdit)
					.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	@PUT
	@Path("/updateLikes")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateLikes(Thread toUpdate){
		if(threadService.updateThread(toUpdate)){
			return Response.ok(toUpdate).build();
		} else{
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@DELETE
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Path("/delete/{naturalID}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteThread(@PathParam("naturalID") String naturalID){
		if(threadService.deleteThread(naturalID)){
			return Response
					.status(Status.OK)
					.entity("true")
					.build();
		}
		return Response
				.status(Status.NOT_ACCEPTABLE)
				.build();
	}

	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
