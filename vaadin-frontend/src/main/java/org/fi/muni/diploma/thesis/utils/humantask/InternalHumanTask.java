package org.fi.muni.diploma.thesis.utils.humantask;

import java.util.List;

/**
 * Other representation of a human task, which include human task name and list of its outputs
 * 
 * @author osiris
 * 
 */
public class InternalHumanTask {

	private String name;
	private List<HumanTaskOutput> outputs; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HumanTaskOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<HumanTaskOutput> outputs) {
		this.outputs = outputs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((outputs == null) ? 0 : outputs.hashCode());
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
		InternalHumanTask other = (InternalHumanTask) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outputs == null) {
			if (other.outputs != null)
				return false;
		} else if (!outputs.equals(other.outputs))
			return false;
		return true;
	}



}
