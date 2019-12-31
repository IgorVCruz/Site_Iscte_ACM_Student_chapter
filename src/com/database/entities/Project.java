package com.database.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

/**
 * Entity implementation class for Entity: Project
 *
 */
@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "project_id")
	private int id;
	
	
	@Column(nullable = false)
	private int views = 0;
	
	
	@Column(nullable = false)
	private int maxMembers;
	
	
	@Column(length = 65, nullable = false, unique = true)	
	private String title;
	
	
	@Column(length = 665, nullable = false)	
	private String description;
	
	@Column(length = 665, nullable = false)	
	private String requirements;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadLine;
	
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date subscriptionDeadline;
	
	
	@Column(nullable = false)
	@Enumerated
	private State state =  State.ON_APPROVAL;  //State.values()[new Random().nextInt(State.values().length)]; to random generate

	
	@ManyToOne(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PROJECT_MANAGER_ID"), nullable= false)
 	private User manager;
	
	
	@Column
	@ElementCollection(targetClass=String.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> tags;
	
	
	@Exclude
	@Column
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "project_participants",
			   joinColumns = @JoinColumn(name = "project_id"),
			   inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> participants;
	
	
	@Column
	@ElementCollection(targetClass=String.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> imagePath;


	
	
	/**
	 * @param maxMembers number of maximum members of a team
	 * @param title	project title
	 * @param description project description
	 * @param deadLine	project deadline
	 * @param subscriptionDeadline project subscription deadline
	 * @param manager	project manager 
	 * @param tags	project tags
	 * @param imagePath	project images
	 */
	public Project(int maxMembers, String title, String description,String requirements, Date deadLine, Date subscriptionDeadline,
			User manager, List<String> tags, List<String> imagePath) {

		this.maxMembers = maxMembers;
		this.title = title;
		this.description = description;
		this.requirements = requirements;
		this.deadLine = deadLine;
		this.subscriptionDeadline = subscriptionDeadline;
		this.manager = manager;
		this.tags = tags;
		this.imagePath = imagePath.stream().map(path -> "projects/"+path).collect(Collectors.toList());
		
		List<User> participants = new ArrayList<>();
		participants.add(manager);
		this.participants = participants;
	}
}

