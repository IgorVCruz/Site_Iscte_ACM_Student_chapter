package com.web.beans.admin;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.database.entities.State;
import com.database.managers.JpaUtil;
import com.database.managers.ResearchManager;
import com.web.containers.ResearchContainer;

import lombok.Data;

@ManagedBean
@ViewScoped
@Data
public class AdminResearchesBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final int lastMonths = 7;

	private List<ResearchContainer> researches;
	
	private long pendingResearches;
	private long researchesDeveloping;
	private double likesMonth;
	private double viewsMonth;
	
	@PostConstruct
	public void init() {
		researches = ResearchManager.findAllResearches().stream().map(ResearchContainer::new).collect(Collectors.toList());
		pendingResearches = JpaUtil.executeQuery("Select count(*) from Research p where p.state = "+ State.ON_APPROVAL.ordinal(), Long.class) .get(0);
		researchesDeveloping = JpaUtil.executeQuery("Select count(*) from Research p where p.state = "+ State.DEVELOPING.ordinal(), Long.class) .get(0);
		viewsMonth = getViewsPerMonth();
		likesMonth = getLikesPerMonth();
	}
	
	
	
	
	
	
	/**
	 * @return return the average views per month
	 */
	private double getLikesPerMonth() {
		LocalDateTime begin = LocalDateTime.of(2020, 1, 1, 0, 0);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = begin.minusYears(now.getYear());
		double allViews = JpaUtil.executeQuery("Select count(*) from Research p join p.likes l", Long.class).get(0);
		double total = date.getYear() * 12 + date.getMonthValue();
		return allViews/total;
	}
	
	
	
	
	
	
	
	/**
	 * @return return the average views per month
	 */
	private double getViewsPerMonth() {
		LocalDateTime begin = LocalDateTime.of(2020, 1, 1, 0, 0);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = begin.minusYears(now.getYear());
		double allViews = JpaUtil.executeQuery("Select count(*) from Research p join p.views v", Long.class).get(0);
		double total = date.getYear() * 12 + date.getMonthValue();
		return allViews/total;
	}
	
	
	
	
	
	
	/**
	 * Return the last lastMonths as a string
	 * e.g lastMonths = 6 and today is 26/01/2020
	 * returns
	 * January
	 * December
	 * November
	 * October
	 * September
	 * August
	 */
	public List<String> getLastMonths(){
		LocalDateTime later = LocalDateTime.now().minusMonths(lastMonths-1);
		List<String> months = new ArrayList<>();
		for (int i = 0; i < lastMonths ; i++) {
			months.add((later.getMonth()+"").substring(0, 1)+(later.getMonth()+"").substring(1).toLowerCase());
			later = later.plusMonths(1);
		}
		return months;
	}
	
	
	
	
	
	
	/**
	 * @return return a list of data that corresponds of how much researches have been created in a specific month
	 */
	public List<Long> getLastMonthsResearchesCreated(){
		LocalDateTime later = LocalDateTime.now().minusMonths(lastMonths-1);
		List<Long> months = new ArrayList<>();
		for (int i = 0; i < lastMonths ; i++) {
			months.add(JpaUtil.executeQuery("select count(p) from Research p WHERE YEAR(p.creationDate) = "+later.getYear()+" AND MONTH(p.creationDate) = "+later.getMonthValue(), Long.class).get(0));
			later = later.plusMonths(1);
		}
		return months;
	}
	
	
	
	
	
	
	
	/**
	 * @return return a list of data that corresponds of how much researches have been created in a specific month
	 */
	public List<Long> getLastMonthsViews(){
		LocalDateTime later = LocalDateTime.now().minusMonths(lastMonths-1);
		List<Long> months = new ArrayList<>();
		for (int i = 0; i < lastMonths ; i++) {
			months.add(JpaUtil.executeQuery("Select count(*) from Research p join p.views v where YEAR(v.date) = "+ later.getYear() +"and MONTH(v.date) = "+later.getMonthValue(), Long.class).get(0));
			later = later.plusMonths(1);
		}
		return months;
	}
	
	
	
	
	
	
	
	/**
	 * @return return a list of data that corresponds of how much researches have been created in a specific month
	 */
	public List<Long> getLastMonthsLikes(){
		LocalDateTime later = LocalDateTime.now().minusMonths(lastMonths-1);
		List<Long> months = new ArrayList<>();
		for (int i = 0; i < lastMonths ; i++) {
			months.add(JpaUtil.executeQuery("Select count(*) from Research p join p.likes v where YEAR(v.date) = "+ later.getYear() +"and MONTH(v.date) = "+later.getMonthValue(), Long.class).get(0));
			later = later.plusMonths(1);
		}
		return months;
	}
	
	
	
	
	
	
	/**
	 * @param e
	 * removes an research 
	 */
	public void removeProject(ActionEvent e ) {
		System.out.println(e.getComponent().getAttributes().get("researchId"));
	}
	
}
