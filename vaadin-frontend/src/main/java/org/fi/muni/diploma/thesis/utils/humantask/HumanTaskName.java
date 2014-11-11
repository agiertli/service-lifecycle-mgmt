package org.fi.muni.diploma.thesis.utils.humantask;

/**
 * List of all human tasks in all processes, with their String representation
 * 
 * @author osiris
 * 
 */
public enum HumanTaskName {

	DEPRECATE_SERVICE("Deprecate service"), RETIRE_SERVICE("Retire Service"), INITIALIZE("Initialize"), REGISTER("Register"), TEST("Test"), IDENTIFY_SERVICE(
			"Identify Service"), EVALUATE_TEST_RESULTS("Evaluate tests result"), SELECT_SERVICE_FROM_SRAMP("Select service from S-RAMP"), REGISTER_EXISTING_SERVICE(
			"Register Existing Service");

	private HumanTaskName(String name) {

		this.name = name;
	}

	private final String name;

	public String toString() {
		return name;
	}

	public static HumanTaskName convertStringToEnum(String name) {

		switch (name) {

		case "Deprecate service":
			return DEPRECATE_SERVICE;

		case "Retire Service":
			return RETIRE_SERVICE;

		case "Initialize":
			return INITIALIZE;

		case "Register":
			return REGISTER;

		case "Test":
			return TEST;

		case "Identify Service":
			return IDENTIFY_SERVICE;

		case "Evaluate tests result":

			return EVALUATE_TEST_RESULTS;

		case "Select service from S-RAMP":
			return SELECT_SERVICE_FROM_SRAMP;

		case "Register Existing Service":
			return REGISTER_EXISTING_SERVICE;

		default:
			System.out.println("unkown value:" + name);
			break;

		}
		return null;
	}
}
