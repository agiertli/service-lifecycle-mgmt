package org.fi.muni.diploma.thesis.utils.jbpm;

import java.util.List;
import java.util.Map;

import org.kie.api.command.Command;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Attachment;
import org.kie.api.task.model.Content;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;

/**
 * TaskService wrapper - uses RemoteRuntimeEngine internally and delegates actual implementation to TaskService
 * 
 * @author osiris
 * 
 */
public class TaskServiceWrapper implements TaskService {

	@Override
	public void claim(long taskId, String userId) {

		RuntimeEngineWrapper.getEngine().getTaskService().claim(taskId, userId);
	}

	@Override
	public void claimNextAvailable(String userId, String language) {
		RuntimeEngineWrapper.getEngine().getTaskService().claimNextAvailable(userId, language);
	}

	@Override
	public void complete(long taskId, String userId, Map<String, Object> data) {

		RuntimeEngineWrapper.getEngine().getTaskService().complete(taskId, userId, data);
	}

	@Override
	public void delegate(long taskId, String userId, String targetUserId) {

		RuntimeEngineWrapper.getEngine().getTaskService().delegate(taskId, userId, targetUserId);
	}

	@Override
	public void exit(long taskId, String userId) {

		RuntimeEngineWrapper.getEngine().getTaskService().exit(taskId, userId);
	}

	@Override
	public void fail(long taskId, String userId, Map<String, Object> faultData) {
		RuntimeEngineWrapper.getEngine().getTaskService().fail(taskId, userId, faultData);
	}

	@Override
	public void forward(long taskId, String userId, String targetEntityId) {

		RuntimeEngineWrapper.getEngine().getTaskService().forward(taskId, userId, targetEntityId);
	}

	@Override
	public Task getTaskByWorkItemId(long workItemId) {

		return RuntimeEngineWrapper.getEngine().getTaskService().getTaskByWorkItemId(workItemId);
	}

	@Override
	public Task getTaskById(long taskId) {

		return RuntimeEngineWrapper.getEngine().getTaskService().getTaskById(taskId);
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsBusinessAdministrator(String userId, String language) {

		return RuntimeEngineWrapper.getEngine().getTaskService()
				.getTasksAssignedAsBusinessAdministrator(userId, language);
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId, String language) {

		return RuntimeEngineWrapper.getEngine().getTaskService().getTasksAssignedAsPotentialOwner(userId, language);
	}

	@Override
	public List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatus(String userId, List<Status> status,
			String language) {

		return RuntimeEngineWrapper.getEngine().getTaskService()
				.getTasksAssignedAsPotentialOwnerByStatus(userId, status, language);
	}

	@Override
	public List<TaskSummary> getTasksOwned(String userId, String language) {
		return RuntimeEngineWrapper.getEngine().getTaskService().getTasksOwned(userId, language);
	}

	@Override
	public List<TaskSummary> getTasksOwnedByStatus(String userId, List<Status> status, String language) {
		return RuntimeEngineWrapper.getEngine().getTaskService().getTasksOwnedByStatus(userId, status, language);
	}

	@Override
	public List<TaskSummary> getTasksByStatusByProcessInstanceId(long processInstanceId, List<Status> status,
			String language) {

		return RuntimeEngineWrapper.getEngine().getTaskService()
				.getTasksByStatusByProcessInstanceId(processInstanceId, status, language);
	}

	@Override
	public List<Long> getTasksByProcessInstanceId(long processInstanceId) {

		return RuntimeEngineWrapper.getEngine().getTaskService().getTasksByProcessInstanceId(processInstanceId);
	}

	@Override
	public long addTask(Task task, Map<String, Object> params) {
		return RuntimeEngineWrapper.getEngine().getTaskService().addTask(task, params);
	}

	@Override
	public void release(long taskId, String userId) {

		RuntimeEngineWrapper.getEngine().getTaskService().release(taskId, userId);
	}

	@Override
	public void resume(long taskId, String userId) {
		RuntimeEngineWrapper.getEngine().getTaskService().resume(taskId, userId);
	}

	@Override
	public void skip(long taskId, String userId) {
		RuntimeEngineWrapper.getEngine().getTaskService().skip(taskId, userId);
	}

	@Override
	public void start(long taskId, String userId) {
		RuntimeEngineWrapper.getEngine().getTaskService().start(taskId, userId);
	}

	@Override
	public void stop(long taskId, String userId) {
		RuntimeEngineWrapper.getEngine().getTaskService().stop(taskId, userId);
	}

	@Override
	public void suspend(long taskId, String userId) {
		RuntimeEngineWrapper.getEngine().getTaskService().suspend(taskId, userId);
	}

	@Override
	public void nominate(long taskId, String userId, List<OrganizationalEntity> potentialOwners) {
		RuntimeEngineWrapper.getEngine().getTaskService().nominate(taskId, userId, potentialOwners);
	}

	@Override
	public Content getContentById(long contentId) {

		return RuntimeEngineWrapper.getEngine().getTaskService().getContentById(contentId);
	}

	@Override
	public Attachment getAttachmentById(long attachId) {
		return RuntimeEngineWrapper.getEngine().getTaskService().getAttachmentById(attachId);
	}

	@Override
	public Map<String, Object> getTaskContent(long taskId) {
		return RuntimeEngineWrapper.getEngine().getTaskService().getTaskContent(taskId);
	}

	@Override
	public <T> T execute(Command<T> arg0) {

		return RuntimeEngineWrapper.getEngine().getTaskService().execute(arg0);
	}

	@Override
	public void activate(long taskId, String userId) {

		RuntimeEngineWrapper.getEngine().getTaskService().activate(taskId, userId);
	}

}
