package org.fi.muni.diploma.thesis.humantask;

public class HumanTaskOutput {
	
	private HumanTaskOutputType dataType;
	private String label;
	private String outputIdentifier;
	
	public HumanTaskOutputType getDataType() {
		return dataType;
	}
	public void setDataType(HumanTaskOutputType dataType) {
		this.dataType = dataType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getOutputIdentifier() {
		return outputIdentifier;
	}
	public void setOutputIdentifier(String outputIdentifier) {
		this.outputIdentifier = outputIdentifier;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((outputIdentifier == null) ? 0 : outputIdentifier.hashCode());
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
		HumanTaskOutput other = (HumanTaskOutput) obj;
		if (dataType != other.dataType)
			return false;
//we are not comparing labels
		if (outputIdentifier == null) {
			if (other.outputIdentifier != null)
				return false;
		} else if (!outputIdentifier.equals(other.outputIdentifier))
			return false;
		return true;
	}

	
}
