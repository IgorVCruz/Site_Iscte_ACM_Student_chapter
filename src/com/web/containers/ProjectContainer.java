package com.web.containers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.database.entities.Project;
import com.database.entities.State;
import com.database.managers.ProjectManager;

import lombok.Data;
import lombok.ToString.Exclude;

/**
 * This object represents the Data transfer Object of class Project (Project DTO)
 * @author RuiMenoita
 */

@Data
public class ProjectContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int maxMembers;
	private String title;
	@Exclude
	private String description;
	@Exclude
	private String requirements;
	private Date deadLine;
	private Date subscriptionDeadline;
	private State state ;
	@Exclude
	private UserContainer manager;
	private List<String> tags;
	@Exclude
	private List<UserContainer> participants;
	@Exclude
	private List<String> imagePath;



	/**
	 * Constructor
	 * @param p project p to be used to create an instance of ProjectContainer
	 */
	public ProjectContainer(Project p) {
		this.id = p.getId();
		this.maxMembers = p.getMaxMembers();
		this.title  = p.getTitle();
		this.description  = p.getDescription();
		this.requirements = p.getRequirements();
		this.deadLine  = p.getDeadLine();
		this.subscriptionDeadline  = p.getSubscriptionDeadline();
		this.state   = p.getState();
		this.tags  = p.getTags();
		this.imagePath  = p.getImagePath();
	}



	public ProjectContainer(int id) {
		Project p = ProjectManager.findById(id);
		this.id = p.getId();
		this.maxMembers = p.getMaxMembers();
		this.title  = p.getTitle();
		this.description  = p.getDescription();
		this.requirements = p.getRequirements();
		this.deadLine  = p.getDeadLine();
		this.subscriptionDeadline  = p.getSubscriptionDeadline();
		this.state   = p.getState();
		this.tags  = p.getTags();
		this.imagePath  = p.getImagePath();
	}
	
	
	public UserContainer getManager() {
		if(manager == null) 
			manager = new UserContainer(ProjectManager.findById(id).getManager());
		return manager;
	}
	
	
	public List<UserContainer> getParticipants() {
		if(participants == null) 
			participants = ProjectManager.findById(id).getParticipants().stream().map(UserContainer::new).collect(Collectors.toList());
		return participants;
	}
}
