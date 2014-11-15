package org.fi.muni.diploma.thesis.utils.jbpm;

public class ProcessStateMap {

	public enum States {

		ACTIVE, PENDING, COMPLETED, ABORTED, SUSPENDED, UNKNOWN;
	}

	public static States getProcessStatusAsEnum(Integer state) {

		switch (state) {

		case 0:
			return States.PENDING;
		case 1:
			return States.ACTIVE;
		case 2:
			return States.COMPLETED;
		case 3:
			return States.ABORTED;
		case 4:
			return States.SUSPENDED;
		default:
			return States.UNKNOWN;

		}

	}

	public static String getProcessStatusAsString(States state) {

		switch (state) {

		case PENDING:
			return "PENDING";
		case ACTIVE:
			return "ACTIVE";
		case COMPLETED:
			return "COMPLETED";
		case ABORTED:
			return "ABORTED";
		case SUSPENDED:
			return "SUSPENDED";
		default:
			return "UNKNOWN";

		}

	}

}
