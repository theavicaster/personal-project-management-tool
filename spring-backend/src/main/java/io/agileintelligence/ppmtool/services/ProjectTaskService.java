package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    private final BacklogRepository backlogRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectTaskRepository projectTaskRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        projectTask.setBacklog(backlog);

        if (backlog == null) {
            throw new ProjectNotFoundException(String.format("Project with id: %s does not exist", projectIdentifier));
        }

        Integer PTSequence = backlog.getPTSequence();
        PTSequence++;
        backlog.setPTSequence(PTSequence);

        projectTask.setProjectSequence(String.format("%s-%d",
                projectIdentifier, PTSequence));

        projectTask.setProjectIdentifier(projectIdentifier);

        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogByIdentifier(String projectIdentifier) {

        // service handles case where project is not found
        projectService.findProjectByIdentifier(projectIdentifier);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier.toUpperCase());
    }

    public ProjectTask findProjectTaskBySequence(String backlog_id, String pt_id) {

        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) {
            throw new ProjectNotFoundException(String.format("Project with id: %s does not exist", backlog_id));
        }

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException(String.format("Project Task: %s does not exist", pt_id));
        }

        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException(String.format("Project Task: %s does not exist in project: %s", pt_id, backlog_id));
        }

        return projectTask;

    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {

        // service is called to check if project task exists
        findProjectTaskBySequence(backlog_id, pt_id);

        return projectTaskRepository.save(updatedTask);

    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String pt_id) {

        ProjectTask projectTask = findProjectTaskBySequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);

    }
}
