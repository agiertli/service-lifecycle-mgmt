package org.fi.muni.diploma.thesis.humantask;

/**
 * List of all human tasks in all processes, with their String representation
 * @author osiris
 *
 */
public enum HumanTaskName {

	DEPRECATE_SERVICE("Deprecate Service"),
	RETIRE_SERVICE("Retire Service"),
	INITIALIZE("Initialize"),
	REGISTER("Register"),
	TEST("Test"),
	IDENTIFY_SERVICE("Identify Service"),
	EVALUATE_TEST_RESULTS("Evaluate tests result"),
	SELECT_SERVICE_FROM_SRAMP("Select service from  S-RAMP"),
	REGISTER_EXISTING_SERVICE("Register Existing Service");

	private HumanTaskName(String name) {

		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}
}
