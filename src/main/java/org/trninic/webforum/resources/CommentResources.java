package org.trninic.webforum.resources;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.trninic.webforum.beans.Comment;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.services.CommentService;


@Path("/comments")
public class CommentResources {

	private CommentService commentService = new CommentService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(){

		try {
			return Response
					.status(Status.OK)
					.entity(commentService.getComments())
					.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Path("/subcomments/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubComments(@PathParam("id") long id){
		ArrayList<Comment> subs = null;
		try {
			subs = commentService.getSubComments(id);
		} catch (IOException e) {
			return Response
					.serverError()
					.build();
		}
		if(subs != null){
			return Response
					.ok(subs, MediaType.APPLICATION_JSON)
					.build();
		} else{
			return Response.noContent().build();
		}
	}
	
	@POST
	@Path("/{parentID}/newComment")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response replyComment(Comment newComment, @PathParam("parentID")long parentID){
		ArrayList<Comment> allComments = null;
		try {
			allComments = commentService.addComment(parentID, newComment);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		if(allComments == null)
			return Response.serverError().build();
		
		if(allComments.isEmpty()){
			return Response
					.noContent()
					.build();
		}
		return Response
				.ok(allComments)
				.build();
	}
	@POST
	@Path("/replyThread")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR, User.RoleEnum.SUBSCRIBER})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response replyThread(Comment newComment){
		ArrayList<Comment> allComments = null;
		try {
			allComments = commentService.addComment(newComment);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		if(allComments == null)
			return Response.serverError().build();
		
		if(allComments.isEmpty()){
			return Response
					.noContent()
					.build();
		}
		return Response
				.ok(allComments)
				.build();
		
	}
		
	@Path("/edit")
	@PUT
	@Secured({User.RoleEnum.ADMIN,User.RoleEnum.MODERATOR,User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editComment(Comment toUpdate) {
		if(commentService.updateComment(toUpdate)){
			return Response.ok(toUpdate).build();
		}
		else{
			return Response.status(Status.NO_CONTENT).build();
		}
	}
	
	@Path("/delete/{id}")
	@DELETE
	@Secured({User.RoleEnum.ADMIN,User.RoleEnum.MODERATOR,User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteComment(@PathParam("id") long id) {
		if(commentService.removeCommet(id)){
			return Response.ok(true).build();
		}
		else{
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@Path("/like")
	@POST
	@Secured({User.RoleEnum.ADMIN,User.RoleEnum.MODERATOR,User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response like(Comment toUpdate){
		Comment updated = null;
		try {
			updated = commentService.like(toUpdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return Response
					.serverError()
					.build();
		}
		if(updated !=null){
			return Response
					.ok(updated)
					.build();
		}
		return Response
				.noContent()
				.build();
	}
	
	
	@Path("/dislike")
	@POST
	@Secured({User.RoleEnum.ADMIN,User.RoleEnum.MODERATOR,User.RoleEnum.SUBSCRIBER})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dislike(Comment toUpdate){
		System.out.println("dislike");
		Comment updated = null;
		try {
			updated = commentService.dislike(toUpdate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return Response
					.serverError()
					.build();
		}
		if(updated !=null){
			return Response
					.status(Status.OK)
					.entity(updated)
					.build();
		}
		return Response
				.noContent()
				.build();
	}

}
