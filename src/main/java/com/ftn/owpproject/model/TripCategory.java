package com.ftn.owpproject.model;

import com.ftn.owpproject.model.enums.TripCategoryEnum;

public class TripCategory {

	private TripCategoryEnum categoryName;
	private String description;

	public TripCategory(TripCategoryEnum categoryName, String description) {
		super();
		this.categoryName = categoryName;
		this.description = description;
	}
	
	public TripCategory() {
		super();
	}
	
	public TripCategoryEnum getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(TripCategoryEnum categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
