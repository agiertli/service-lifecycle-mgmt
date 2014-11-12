package org.fi.muni.diploma.thesis.utils.rtgov;

public class RetiredService {
	
	public String name;
	public long retirementTimestamp;
	
	public RetiredService(String name, long retirementTimestamp) {
		
		this.name = name;
		this.retirementTimestamp = retirementTimestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRetirementTimestamp() {
		return retirementTimestamp;
	}

	public void setRetirementTimestamp(long retirementTimestamp) {
		this.retirementTimestamp = retirementTimestamp;
	}

	@Override
	public String toString() {
		return "RetiredService [name=" + name + ", retirementTimestamp=" + retirementTimestamp + "]";
	}

}
