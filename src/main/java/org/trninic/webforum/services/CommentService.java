package org.trninic.webforum.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.trninic.webforum.beans.Comment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommentService {
	private ObjectMapper om;
	private File commentsFile;
	public CommentService() {
		// TODO Auto-generated constructor stub
		om = new ObjectMapper();
		commentsFile = new File(this.getClass().getClassLoader().getResource("../../resources/comments.json").getPath());
	}
	public ArrayList<Comment> getComments() throws JsonParseException, JsonMappingException, IOException{
		return om.readValue(commentsFile, om.getTypeFactory()
				.constructCollectionType(ArrayList.class, Comment.class));
	}

	public List<Comment> getComments(int begining, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getComments().subList(begining, begining + maxResults);
	}
	public ArrayList<Comment> getComments(String threadID) throws JsonParseException, JsonMappingException, IOException{
		return getComments().stream()
				.filter((comment) -> comment.getThread().equals(threadID))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<Comment> getComments(String threadID, int begin, int maxResults) throws JsonParseException, JsonMappingException, IOException{
		return getComments(threadID)
				.subList(begin, maxResults)
				.stream()
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Comment getComment(Long id) throws JsonParseException, JsonMappingException, IOException{
		return getComments()
				.stream()
				.filter((comment) -> comment.getId() == id.longValue())
				.findFirst()
				.orElse(null);
	}
	public ArrayList<Comment> getSubComments(Long id) throws JsonParseException, JsonMappingException, IOException{
		ArrayList<Comment> allComents = getComments();
		return allComents
				.stream()
				.filter((comment) -> comment.getParent() != null)
				.filter((comment) -> comment.getParent().equals(id))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Comment getParent(Long id) throws JsonParseException, JsonMappingException, IOException{
		Comment thisComment = getComment(id);
		return getComments().stream()
				.filter((comment) -> comment.getId() == thisComment.getParent())
				.findFirst()
				.orElse(null);
	}

	public ArrayList<Comment> addComment(long parentID, Comment comment) throws JsonGenerationException, JsonMappingException, IOException{
		ArrayList<Comment> allComments = getComments();
		comment.setParent(parentID);
		
		int index = IntStream.range(0, allComments.size())
	    .filter(commentInd-> allComments.get(commentInd).getId() == parentID)
	    .findFirst()
	    .orElse(-1);
		if(index==-1){
			return null;
		}
		comment.setId(allComments.size());
		//dodat subcomment na parenta
		allComments.get(index).getSubComments().add(comment.getId());
		//nasledjen thread od parent comentara
		comment.setThread(allComments.get(index).getThread());
		//dodat novi komentar
		allComments.add(comment);
		
		om.writeValue(commentsFile, allComments);
		
		return getComments(comment.getThread());

	}

	public ArrayList<Comment> addComment(Comment comment) throws JsonGenerationException, JsonMappingException, IOException{
		//pretpostavka da je na client side setovan thread
		ArrayList<Comment> allComments = getComments();
		comment.setId(allComments.size());
		
		allComments.add(comment);
		om.writeValue(commentsFile, allComments);
		return getComments(comment.getThread());
	}

	public Comment like(Comment comment) throws JsonParseException, JsonMappingException, IOException{
		if(comment!=null){
			comment.like();
			if(updateComment(comment)){
				return comment;
			}
		}
		return null;
	}
	public Comment dislike(Comment comment) throws JsonParseException, JsonMappingException, IOException{
		if(comment!=null){
			comment.dislike();
			if(updateComment(comment)){
				return comment;
			}
		}
		return null;
	}
	public boolean removeCommet(long id){
		try {
			Comment comment = getComment(id);
			comment.setAuthor("");
			comment.setText("");
			return updateComment(comment);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateComment(Comment toUpdate){
		ArrayList<Comment> allComments;
		try {
			allComments = getComments();
			int i;
			boolean found = false;

			for(i = 0; i < allComments.size() ; i++){
				if(allComments.get(i).getId() == toUpdate.getId()){
					allComments.set(i, toUpdate);
					found = true;
					break;
				}
			}
			if(found)
				om.writeValue(commentsFile, allComments);
			return found;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}


	}
}
