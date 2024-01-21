package com.ftn.owpproject.model.enums;

public enum TripCategoryEnum {
    SKIING("Skiing"),
    SUMMER_VACATION("Summer vacation"),
    LAST_MINUTE("Last minute"),
    NEW_YEAR("New Year");

	private final String categoryName;
	TripCategoryEnum(String categoryName) {
		this.categoryName = categoryName;
	}
	
    public String getCategoryName() {
    	return categoryName;
    }
}

