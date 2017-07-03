package org.trninic.webforum.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.trninic.webforum.json.DateDeserializer;
import org.trninic.webforum.json.DateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//@Entity (name="THREADS")
public class Thread {
	public static enum ThreadType {TEXT,IMAGE,LINK};
	//@Id
	//@GeneratedValue
	
	private String naturalID;
	
	//@OneToOne
	//@JoinColumn(name="SECTION_ID")
	//@JsonSerialize(converter=SectionConverter.class)
	private String parentSection;
	
	private String title;
	
	///@Enumerated(EnumType.STRING)
	private ThreadType type;
	
	//@OneToOne
	//@JoinColumn(name="USER_AUTHOR")
	//@JsonSerialize(converter=UserConverter.class)
	private String author;
	
	//@OneToMany
	//@JoinTable(name="THREAD_COMMENTS", joinColumns=@JoinColumn(name="THREAD_ID"), inverseJoinColumns=@JoinColumn(name="COMMENT_ID"))
	//@JsonSerialize(converter=ListCommentConverter.class)
	private Collection<Long> comments = new ArrayList<>();
	
	private String content;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date created;
	
	private Integer likes;
	private Integer dislikes;
	
	public Thread() {
		// TODO Auto-generated constructor stub
	}
	
	public Thread(String naturalID, String parentSection, String title, ThreadType type, String author,
			Collection<Long> comments, String content, Date created, Integer likes, Integer dislikes) {
		super();
		this.naturalID = naturalID;
		this.parentSection = parentSection;
		this.title = title;
		this.type = type;
		this.author = author;
		this.comments = comments;
		this.content = content;
		this.created = created;
		this.likes = likes;
		this.dislikes = dislikes;
	}

	public String getNaturalID() {
		return naturalID;
	}

	public void setNaturalID(String naturalID) {
		this.naturalID = naturalID;
	}

	public String getParentSection() {
		return parentSection;
	}

	public void setParentSection(String parentSection) {
		this.parentSection = parentSection;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ThreadType getType() {
		return type;
	}

	public void setType(ThreadType type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Collection<Long> getComments() {
		return comments;
	}

	public void setComments(Collection<Long> comments) {
		this.comments = comments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
	
	
	
}
