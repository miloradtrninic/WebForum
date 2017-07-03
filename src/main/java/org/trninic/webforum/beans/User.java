package org.trninic.webforum.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.trninic.webforum.json.DateDeserializer;
import org.trninic.webforum.json.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//@Entity (name="USERS")
//@JsonDeserialize(using = UserDeserializer.class)
public class User {
	public static enum RoleEnum {ADMIN,MODERATOR,SUBSCRIBER};
	//@Id
	//@GeneratedValue
	private String username;
	private String password;
	
	//@Enumerated(EnumType.STRING)
	private RoleEnum role;
	
	private String name;
	private String surname;
	private String phoneNum;
	private String email;
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date registerDate;
	
	
	
	
	public User(String username, String password, RoleEnum role, String name, String surname, String phoneNum,
			String email, Date registerDate, Collection<String> subscribedSections, Collection<String> myThreads,
			Collection<Long> myComments) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.phoneNum = phoneNum;
		this.email = email;
		this.registerDate = registerDate;
		this.subscribedSections = subscribedSections;
		this.myThreads = myThreads;
		this.myComments = myComments;
	}
	//@OneToMany
	//@JoinTable (name="USERS_SECTION_SUBSCRIBED", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="SECTION_ID"))
	//@JsonSerialize(converter=ListSectionConvertor.class)
	private Collection<String> subscribedSections = new ArrayList<>();
	
	
	//@OneToMany
	//@JoinTable(name="USER_THREADS", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="THREAD_ID"))
	//@JsonSerialize(converter=ListThreadConvertor.class)
	private Collection<String> myThreads = new ArrayList<>();
	
	//@OneToMany
	//@JoinTable(name="USER_COMMENTS", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="COMMENT_ID"))
	//@JsonSerialize(converter=ListCommentConverter.class)
	private Collection<Long> myComments = new ArrayList<>();
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public RoleEnum getRole() {
		return role;
	}
	public void setRole(RoleEnum role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Collection<String> getSubscribedSections() {
		return subscribedSections;
	}
	public void setSubscribedSections(Collection<String> subscribedSections) {
		this.subscribedSections = subscribedSections;
	}
	public void subscribeToSection(String sectionID){
		this.subscribedSections.add(sectionID);
	}
	public Collection<String> getMyThreads() {
		return myThreads;
	}
	public void setMyThreads(Collection<String> myThreads) {
		this.myThreads = myThreads;
	}
	public void addThread(String threadID){
		this.myThreads.add(threadID);
	}
	public Collection<Long> getMyComments() {
		return myComments;
	}
	public void setMyComments(Collection<Long> myComments) {
		this.myComments = myComments;
	}
	public void addComment(Long commentID){
		this.myComments.add(commentID);
	}
	
	
}
