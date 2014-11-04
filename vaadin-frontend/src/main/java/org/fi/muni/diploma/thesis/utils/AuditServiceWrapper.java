package org.fi.muni.diploma.thesis.utils;

import java.util.List;

import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.kie.internal.query.data.QueryData;
import org.kie.internal.runtime.manager.audit.query.NodeInstanceLogQueryBuilder;
import org.kie.internal.runtime.manager.audit.query.ProcessInstanceLogQueryBuilder;
import org.kie.internal.runtime.manager.audit.query.VariableInstanceLogQueryBuilder;

/**
 * audit service wrapper - uses runtimeengine under the hood
 * @author osiris
 *
 */
public class AuditServiceWrapper implements AuditLogService {

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessInstanceLog> findProcessInstances(String processId) {

		return (List<ProcessInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findProcessInstances(processId);
	}

	@Override
	public ProcessInstanceLog findProcessInstance(long pid) {

		return (ProcessInstanceLog) RuntimeEngineWrapper.getEngine().getAuditLogService().findProcessInstance(pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceLog> findVariableInstances(long pid) {

		return (List<VariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findVariableInstances(pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessInstanceLog> findActiveProcessInstances(String processId) {

		return (List<ProcessInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findActiveProcessInstances(processId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeInstanceLog> findNodeInstances(long pid) {

		return (List<NodeInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findNodeInstances(pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeInstanceLog> findNodeInstances(long pid, String nodeId) {

		return (List<NodeInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findNodeInstances(pid, nodeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessInstanceLog> findProcessInstances() {

		return (List<ProcessInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findProcessInstances();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessInstanceLog> findSubProcessInstances(long pid) {

		return (List<ProcessInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findSubProcessInstances(pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceLog> findVariableInstances(long pid, String variableId) {

		return (List<VariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService().findVariableInstances(pid, variableId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceLog> findVariableInstancesByName(String variableName, boolean onlyActiveProcesses) {
		return (List<VariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstancesByName(variableName, onlyActiveProcesses);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VariableInstanceLog> findVariableInstancesByNameAndValue(String variableName, String value,
			boolean onlyActiveProcesses) {

		return (List<VariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstancesByNameAndValue(variableName, value, onlyActiveProcesses);
	}

	@Override
	public void clear() {

		RuntimeEngineWrapper.getEngine().getAuditLogService().clear();
	}

	@Override
	public void dispose() {

		RuntimeEngineWrapper.getEngine().getAuditLogService().dispose();

	}

	/**
	 * Implementation for the below method is not completed as these methods won't be used in the application
	 */
	@Override
	public NodeInstanceLogQueryBuilder nodeInstanceLogQuery() {
		return null;
	}

	@Override
	public VariableInstanceLogQueryBuilder variableInstanceLogQuery() {
		return null;
	}

	@Override
	public ProcessInstanceLogQueryBuilder processInstanceLogQuery() {
		return null;
	}

	@Override
	public List<org.kie.api.runtime.manager.audit.NodeInstanceLog> queryNodeInstanceLogs(QueryData queryData) {
		return null;
	}

	@Override
	public List<org.kie.api.runtime.manager.audit.VariableInstanceLog> queryVariableInstanceLogs(QueryData queryData) {
		return null;
	}

	@Override
	public List<org.kie.api.runtime.manager.audit.ProcessInstanceLog> queryProcessInstanceLogs(QueryData queryData) {
		return null;
	}

}
