package org.trninic.webforum.beans;

import java.util.ArrayList;

import java.util.Collection;

//@Entity (name="SECTIONS")
public class Section {
	
	//@Id
	//@GeneratedValue
	private long id;
	private String naturalID;
	private String title;
	private String description;
	private String imagePath;
	
	
	
	
	public Section(long id, String naturalID, String title, String description, String imagePath,
			Collection<String> rules, String headModerator, Collection<String> moderators) {
		super();
		this.id = id;
		this.naturalID = naturalID;
		this.title = title;
		this.description = description;
		this.imagePath = imagePath;
		this.rules = rules;
		this.headModerator = headModerator;
		this.moderators = moderators;
	}


	//@ElementCollection
	//@JoinTable(name="SECTION_RULES", joinColumns=@JoinColumn(name="SECTION_ID"))
	private Collection<String> rules  = new ArrayList<>();
	
	//@OneToOne
	//@JoinColumn(name="HEAD_MOD")
	//@JsonSerialize(converter=UserConverter.class)
	private String headModerator;
	
	//@OneToMany
	//@JoinTable(name="SECTION_MODERATORS", joinColumns=@JoinColumn(name="SECTION_ID"), inverseJoinColumns=@JoinColumn(name="MOD_ID"))
	//@JsonSerialize(converter=ListUserConvertor.class)
	private Collection<String> moderators = new ArrayList<>();
	
	
	public Section() {
		// TODO Auto-generated constructor stub
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	

	public String getNaturalID() {
		return naturalID;
	}


	public void setNaturalID(String naturalID) {
		this.naturalID = naturalID;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public Collection<String> getRules() {
		return rules;
	}


	public void setRules(Collection<String> rules) {
		this.rules = rules;
	}


	public String getHeadModerator() {
		return headModerator;
	}


	public void setHeadModerator(String headModerator) {
		this.headModerator = headModerator;
	}


	public Collection<String> getModerators() {
		return moderators;
	}


	public void setModerators(Collection<String> moderators) {
		this.moderators = moderators;
	}
	
	
	
}
