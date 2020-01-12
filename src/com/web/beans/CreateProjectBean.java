package com.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;

import com.database.entities.Material;
import com.database.entities.Project;
import com.database.entities.User;
import com.database.managers.JpaUtil;
import com.database.managers.UserManager;
import com.utils.FileManager;
import com.web.Session;
import com.web.containers.ProjectContainer;
import com.web.containers.UserContainer;

import lombok.Data;

@ManagedBean
@ViewScoped
@Data
public class CreateProjectBean implements Serializable{

	private static final long serialVersionUID = 1L; 

	private ProjectContainer container;
	private int phase = 1;

	private Part uploadedFile;
	private List<Part> uploadedFiles = new ArrayList<>();

	private String tag ="";

	private String usernameOrEmail = "";

	private Material material = new Material();





	public CreateProjectBean() {
		UserContainer user = Session.getInstance().getUser();
		if(user == null)
			Session.getInstance().redirectToLogin("createProject");
		else {
			container  = new ProjectContainer(user);
		}
	}


	/**
	 * Increment phase
	 */
	public void incrementPhase(ActionEvent event) {
		if(phase < 3 && validatePhase2())
			phase++;
	}


	/**
	 * Validates all required components on phase2
	 */
	private boolean validatePhase2() {
		if(phase == 2) {
			boolean valid = true;
			FacesContext context = FacesContext.getCurrentInstance();
			
			if(container.getDeadline() == null ) {
				FacesMessage msg = new FacesMessage("This field must not be empty");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage("dateForm:deadline", msg);
				valid = false;
			}
			if(container.getSubscriptionDeadline() == null) {
				FacesMessage msg = new FacesMessage("This field must not be empty");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage("dateForm:sdeadline", msg);
				valid = false;
			}
			if(container.getDeadline() == null || container.getSubscriptionDeadline() == null) {
				if(container.getDeadline().before(container.getSubscriptionDeadline())) {
					FacesMessage msg = new FacesMessage("Deadline must be after subscription deadline");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					context.addMessage("dateForm:sdeadline", msg);
					valid = false;
				}
			}
			return valid;
		}
		return true;
	}


	/**
	 * Decrement phase
	 */
	public void decrementPhase(ActionEvent event) {
		if(phase > 0 )
			phase--;
	}


	/**
	 * 
	 * @param part
	 */
	public void addImage(ActionEvent event) {
		System.out.println("SOMETHING NICE TO BE CALLED");
		uploadedFiles.add(uploadedFile);
		container.getImagePath().add(uploadedFile.getName());
	}


	/**
	 * 
	 */
	public void addTag(ActionEvent event) {
		if(!tag.isBlank()) {
			if(!container.getTags().contains(tag)) {
				System.out.println("Saving Tag "+tag);
				container.getTags().add(tag);
				tag = "";	
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void removeTag(ActionEvent event ) {
		String tag = (String) event.getComponent().getAttributes().get("tag");
		container.getTags().remove(tag);
	}


	/**
	 * 
	 * @param event
	 */
	public void addUser(ActionEvent event) {
		if(!usernameOrEmail.isBlank()) {
			User u = UserManager.getUserByUsername(usernameOrEmail); //check if there is an user with username 
			if(u == null) 
				u = UserManager.getUserByEmail(usernameOrEmail);	//check if there is an user with email ( if didn't found one with username )

			if(u != null) {

				UserContainer participant = new UserContainer(u);
				if(!container.getParticipants().contains(participant))
					container.getParticipants().add(participant);

				usernameOrEmail = "";
			}else {
				System.out.println("THERE NO USER IF INPUT GIVEN: "+usernameOrEmail);
				//THERE NO USER IF INOUT GIVEN
			}
		}
	}


	/**
	 * 
	 * @param event
	 */
	public void removeUser(ActionEvent event) {
		UserContainer user =  (UserContainer) event.getComponent().getAttributes().get("participant");
		if(!user.equals(container.getManager()))
			container.getParticipants().remove(user);
		else
			System.out.println("Can't delete manager");
	}


	/**
	 * 
	 * @param event
	 */
	public void addMaterial(ActionEvent event) {
		if(!container.getMaterial().contains(material)) {
			container.getMaterial().add(material);
			material = new Material();
		}
	}



	/**
	 * 
	 * @param event
	 */
	public void removeMaterial(ActionEvent event) {
		Material m = (Material) event.getComponent().getAttributes().get("material");
		container.getMaterial().remove(m);
	}


	/**
	 * 
	 * @return
	 */
	public String getTotal() {
		double total = 0.00;
		if(!container.getMaterial().isEmpty()) {
			total = 0;
			for (Material m : container.getMaterial()) 
				total += (m.getPrice()*m.getQuantity());
		}
		return total+"";
	}



	/**
	 * Saves project into database
	 * @param event
	 */
	public void submitProject(ActionEvent event) {
		List<String> paths = FileManager.saveProjectFiles(uploadedFiles);
		container.setImagePath(paths);
		Project p = new Project(container);
		JpaUtil.createEntity(p);
	}
}
