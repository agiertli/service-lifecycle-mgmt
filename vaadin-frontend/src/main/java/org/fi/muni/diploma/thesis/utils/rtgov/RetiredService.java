package org.fi.muni.diploma.thesis.utils.rtgov;

/**
 * Retired service entity, identified by it's name and date of the retirement
 *
 */
public class RetiredService {
	
	public String name;
	public long retirementTimestamp;
	
	public RetiredService(){}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (retirementTimestamp ^ (retirementTimestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetiredService other = (RetiredService) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (retirementTimestamp != other.retirementTimestamp)
			return false;
		return true;
	}

}
