package org.trninic.webforum.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.trninic.webforum.beans.Section;
import org.trninic.webforum.beans.SectionProxy;
import org.trninic.webforum.beans.User;
import org.trninic.webforum.services.SectionService;
import org.trninic.webforum.services.ThreadService;

@Path("/section")
public class SectionResource {
	private SectionService sectionService = new SectionService();
	private ThreadService threadService = new ThreadService();
	@Context
	private  ServletContext context;
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSections(@QueryParam("begin") int begin,@QueryParam("results") int maxResults){
		try {
			if(begin == 0 && maxResults == 0){
				List<Section> listaSekcija = sectionService.getSections();
				listaSekcija.forEach((sekcija) -> System.out.println(sekcija));
				return Response
						.status(Status.OK)
						.entity(listaSekcija)
						.build();

			}
			return Response
					.status(Status.OK)
					.entity(sectionService.getSections(begin, maxResults))
					.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.FORBIDDEN)
					.build();
		}
	}
	@Path("/search")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSections(@FormParam("title") String title, @FormParam("description") String description, @FormParam("moderator") String moderator){
		try {
			List<Section> returnSections = sectionService.getSections();
			System.out.println(returnSections.size());
			if(!title.isEmpty()){
				System.out.println("pokupice title");
				returnSections = returnSections.stream()
												.filter(section -> section.getTitle().contains(title.trim()))
												.collect(Collectors.toCollection(ArrayList::new));
			} 
			if(!description.isEmpty()){
				System.out.println("pokupice desc");
				returnSections = returnSections.stream()
												.filter(section -> section.getDescription().contains(description.trim()))
												.collect(Collectors.toCollection(ArrayList::new));
			}
			if(!moderator.isEmpty()){
				System.out.println("pokupice mod");
				returnSections = returnSections.stream()
												.filter(section -> section.getHeadModerator().equals(moderator))
												.collect(Collectors.toCollection(ArrayList::new));
			}
			
			if(returnSections.isEmpty()){
				System.out.println("nema nista");
				return Response.noContent().build();
			}
			return Response.ok(returnSections).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
	
	}

	@POST
	@Path("/newSection")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response newThread(@FormDataParam("imagePath") InputStream uploadedInputStream,
			@FormDataParam("imagePath") FormDataContentDisposition fileDetails,
			@FormDataParam("title") String title, @FormDataParam("naturalID") String naturalID,@FormDataParam("description") String description,
			@FormDataParam("headModerator") String headModerator, @FormDataParam("rules") String rulesString){

		String uploadedFileLocation = context.getRealPath("/resources/images/") + "/" + fileDetails.getFileName();
		writeToFile(uploadedInputStream, uploadedFileLocation);
		String imagePath = "/resources/images/" + fileDetails.getFileName();


		Section newSection = new Section();
		newSection.setDescription(description);
		newSection.setHeadModerator(headModerator);
		newSection.setModerators(new ArrayList<String>());
		newSection.setNaturalID(naturalID);
		newSection.setTitle(title);
		newSection.setImagePath(imagePath);
		String rules[] = rulesString.split("\\[rule\\]");
		for(String rule: rules){
			System.out.println(rule);
			newSection.getRules().add(rule);
		}
		
		
		try {
			if(!sectionService.addSection(newSection)){
				return Response
						.status(Status.NOT_ACCEPTABLE)
						.build();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.build();
		}
		return Response
				.status(Status.CREATED)
				.entity(newSection)
				.build();
	}

	@GET
	@Path("/{naturalID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSection(@PathParam("naturalID") String naturalID, @QueryParam("begin") int begin,@QueryParam("results") int maxResults){
		try {
			SectionProxy sectionProxy = new SectionProxy();
			sectionProxy.setSection(sectionService.getSection(naturalID));
			if(begin == 0 && maxResults == 0){
				sectionProxy.setThreads(threadService.getThreads(naturalID));
			}else{
				sectionProxy.setThreads(threadService.getThreads(naturalID, begin, maxResults));
			}
			return Response
					.status(Status.CREATED)
					.entity(sectionProxy)
					.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Status.NOT_ACCEPTABLE)
					.build();
		}
	}
	@PUT
	@Path("/update")
	@Secured({User.RoleEnum.ADMIN, User.RoleEnum.MODERATOR})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSection(Section section){
		if(sectionService.updateService(section)){
			return Response
					.status(Status.CREATED)
					.entity(section)
					.build();
		}
		return Response
				.status(Status.NOT_ACCEPTABLE)
				.build();
	}

	@DELETE
	@Path("/delete/{naturalID}")
	@Secured({User.RoleEnum.ADMIN})
	@Produces(MediaType.TEXT_PLAIN)
	public Response removeSection(@PathParam("naturalID") String naturalID){
		if(sectionService.removeSection(naturalID)){
			return Response
					.status(Status.OK)
					.build();
		}else{
			return Response
					.status(Status.FORBIDDEN)
					.build();
		}
	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

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
