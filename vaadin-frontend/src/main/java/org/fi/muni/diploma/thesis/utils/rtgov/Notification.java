package org.fi.muni.diploma.thesis.utils.rtgov;

public class Notification {
	
	private RetiredService service;
	private long invocationTimestamp;
	private String interfaceName;
	private String operation;

	
	
	public Notification(){}
	
	public Notification(RetiredService service, long invocationTimestamp, String interfaceName, String operation) {
		super();
		this.service = service;
		this.invocationTimestamp = invocationTimestamp;
		this.interfaceName = interfaceName;
		this.operation = operation;
	}
	
	
	public RetiredService getService() {
		return service;
	}
	public void setService(RetiredService service) {
		this.service = service;
	}
	public long getInvocationTimestamp() {
		return invocationTimestamp;
	}
	public void setInvocationTimestamp(long invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Override
	public String toString() {
		return "Notification [service=" + service + ", invocationTimestamp=" + invocationTimestamp + ", interfaceName=" + interfaceName
				+ ", operation=" + operation + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (invocationTimestamp ^ (invocationTimestamp >>> 32));
		result = prime * result + ((service == null) ? 0 : service.hashCode());
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
		Notification other = (Notification) obj;
		if (invocationTimestamp != other.invocationTimestamp)
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}






}
