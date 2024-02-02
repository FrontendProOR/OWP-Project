package com.ftn.owpproject.model;

import com.ftn.owpproject.model.enums.TravelCategoryEnum;

public class TravelCategory {
	public TravelCategory(Long id, TravelCategoryEnum categoryName, String description) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.description = description;
	}

	private Long id;
	private TravelCategoryEnum categoryName;
	private String description;

	public TravelCategory(TravelCategoryEnum categoryName, String description) {
		super();
		this.categoryName = categoryName;
		this.description = description;
	}
	
	public TravelCategory() {
		super();
	}
	
	public TravelCategoryEnum getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(TravelCategoryEnum categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
