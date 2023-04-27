package com.ucoruh.mkdocs;


public enum SemesterType {
	
	NONE("notset"), SPRING("Spring"), FALL("Fall");

	private String name;

	private SemesterType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
