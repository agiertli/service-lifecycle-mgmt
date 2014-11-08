package org.fi.muni.diploma.thesis.humantask;

public enum HumanTaskOutputType {
	
	STRING("string"),
	TEXT_AREA("text-area"),
	PASSWORD("password"),
	INTEGER("integer"),
	BOOLEAN("boolean"),
	ENUM_STATE("enum_state");
	
	private HumanTaskOutputType(String outputType) {

		this.outputType = outputType;
	}

	private final String outputType;

	public String toString() {
		return outputType;
	}

}
