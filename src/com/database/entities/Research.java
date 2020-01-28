package com.database.entities;

import com.database.entities.Investigator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 * Entity implementation class for Entity: Research
 *
 */
@Entity
@Data
@NoArgsConstructor
public class Research implements Serializable {

	private static final long serialVersionUID = 1L;   
	
	@Id
	@lombok.EqualsAndHashCode.Exclude
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Exclude
	@lombok.EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_INVESTIGATOR_ID"), nullable= false)
	private Investigator investigator;

	@Column(length = 65, nullable = false, unique = true)	
	private String title;
	
	@Exclude
	@Column(length = 665, nullable = false)	
	private String description;
	
	@Exclude
	@Column(length = 100, nullable = false)	
	private String shortDescription;
	
	@Exclude
	@Column(length = 300, nullable = false)	
	private String requirements;
	
	@Column(nullable = false)
	@Enumerated
	private State state = State.values()[new Random().nextInt(State.values().length-1)]; // State.ON_APPROVAL;  
	
	@Exclude
	@ElementCollection(targetClass=String.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> tags;
	
	@Exclude
	@ManyToMany
	@lombok.EqualsAndHashCode.Exclude
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "research_participants",
			   joinColumns = @JoinColumn(name = "research_id"),
			   inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> participants;
	
	@Exclude
	@ElementCollection(targetClass=String.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> imagePath;
	
	@Exclude
	@lombok.EqualsAndHashCode.Exclude
	@ManyToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "research_likes",
			   joinColumns = @JoinColumn(name = "research_id"),
			   inverseJoinColumns = @JoinColumn(name = "like_id"))
	private List<AcmLike> likes = new ArrayList<>();
	
	@Exclude
	@lombok.EqualsAndHashCode.Exclude
	@ManyToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "research_views",
			   joinColumns = @JoinColumn(name = "research_id"),
			   inverseJoinColumns = @JoinColumn(name = "view_id"))
	private List<View> views = new ArrayList<>();
	
	
	@Enumerated
	private ResearchType type;
	
	@Exclude
	@lombok.EqualsAndHashCode.Exclude
	@ManyToMany(cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "research_candidates",
	joinColumns = @JoinColumn(name = "research_id"),
	inverseJoinColumns = @JoinColumn(name = "candidate_id"))
	private List<Candidate> candidates = new ArrayList<>();


	
	
	
	/**
	 * @param investigator
	 * @param title
	 * @param description
	 * @param shortDescription
	 * @param requirements
	 * @param tags
	 * @param imagePath
	 * @param type
	 */
	public Research(Investigator investigator, String title, String description, String shortDescription,
			String requirements, List<String> tags, List<String> imagePath, ResearchType type) {
		this.investigator = investigator;
		this.title = title;
		this.description = description;
		this.shortDescription = shortDescription;
		this.requirements = requirements;
		this.tags = tags;
		this.imagePath = imagePath.stream().map(path -> "research/"+path).collect(Collectors.toList());
		this.type = type;
	}
}
