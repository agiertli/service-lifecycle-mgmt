package org.fi.muni.diploma.thesis.utils.rtgov;

public class Notification {
	
	private RetiredService service;
	private long invocationTimestamp;
	private String interfaceName;
	private String operation;
	
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

}
