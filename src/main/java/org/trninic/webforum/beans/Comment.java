package org.trninic.webforum.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.trninic.webforum.json.DateDeserializer;
import org.trninic.webforum.json.DateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;



//@Entity (name="COMMENTS")
public class Comment {
	//	@Id
	//@GeneratedValue
	private long id;
	//@OneToOne
	//@JoinColumn(name="AUTHOR")
	//@JsonSerialize(converter=UserConverter.class)
	private String author;

	//@OneToOne
	//@JoinColumn(name="THREAD")
	//@JsonSerialize(converter = ThreadConverter.class)
	private String thread;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date date;

	//@ManyToOne
	//	@JoinTable(name="COMMENTS_HIERARCHY", joinColumns=@JoinColumn(name="SUBCOMENT_ID"), inverseJoinColumns=@JoinColumn(name="PARENT_ID"))
	//	@NotFound(action=NotFoundAction.IGNORE)
	//@JsonSerialize(converter=CommentConverter.class)
	private Long parent;

	//	@OneToMany(mappedBy="parent")
	//@JoinTable(name="COMMENTS_HIERARCHY", joinColumns=@JoinColumn(name="PARENT_ID"), inverseJoinColumns=@JoinColumn(name="SUBCOMENT_ID"))
	//	@NotFound(action=NotFoundAction.IGNORE)
	//@JsonSerialize(converter=ListCommentConverter.class)
	private Collection<Long> subComments = new ArrayList<>();

	private String text;
	private Integer likes;
	private Integer dislikes;
	private boolean edited;

	public Comment() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public Collection<Long> getSubComments() {
		return subComments;
	}

	public void setSubComments(Collection<Long> subComments) {
		this.subComments = subComments;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public void like(){
		likes++;
	}
	public void dislike(){
		dislikes++;
	}


}
