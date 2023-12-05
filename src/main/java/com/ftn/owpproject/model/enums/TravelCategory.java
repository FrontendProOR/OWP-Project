package com.ftn.owpproject.model.enums;

public enum TravelCategory {
    SKIING("Skiing"),
    SUMMER_VACATION("Summer vacation"),
    LAST_MINUTE("Last minute"),
    NEW_YEAR("New Year");

	private final String categoryName;
	TravelCategory(String categoryName) {
		this.categoryName = categoryName;
	}
	
    public String getCategoryName() {
    	return categoryName;
    }
}

