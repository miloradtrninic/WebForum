package org.trninic.webforum.beans;

import java.util.Date;

import org.trninic.webforum.json.DateDeserializer;
import org.trninic.webforum.json.DateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Ticket {
	public static enum TicketEntity {SECTION,THREAD,COMMENT};
	public static enum TicketResponse {DELETE,DENY,WARN};
	
	
	private String content;
	private long id;
	private String entity;
	private TicketEntity type;
	private String authorUsername;
	private TicketResponse response;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date date;
	
	public Ticket() {
		super();
	}
	
	public Ticket(String content, long id, TicketEntity type, String authorUsername) {
		super();
		this.content = content;
		this.id = id;
		this.type = type;
		this.authorUsername = authorUsername;
	}


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public TicketEntity getType() {
		return type;
	}
	public void setType(TicketEntity type) {
		this.type = type;
	}
	public String getAuthorUsername() {
		return authorUsername;
	}
	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public TicketResponse getResponse() {
		return response;
	}

	public void setResponse(TicketResponse response) {
		this.response = response;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}

