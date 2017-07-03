package org.trninic.webforum.beans;

//@Entity (name="MESSAGES")
public class Message {
	//@Id
	//@GeneratedValue
	
	//@OneToOne
	//@JoinColumn(name="SENDER")
	//@JsonSerialize(converter=UserConverter.class)
	private long id;
	private String sender;
	//@OneToOne
	//@JoinColumn(name="RECEPIENT")
	//@JsonSerialize(converter=UserConverter.class)
	private String recepient;
	
	private String content;
	private boolean seen;
	
	
	public Message() {
		// TODO Auto-generated constructor stub
	}

	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getRecepient() {
		return recepient;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public boolean isSeen() {
		return seen;
	}


	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	
}
