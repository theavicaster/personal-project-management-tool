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

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
        projectTask.setBacklog(backlog);

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

    public Iterable<ProjectTask> findBacklogByIdentifier(String projectIdentifier, String username) {

        // service handles case where project is not found
        projectService.findProjectByIdentifier(projectIdentifier, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier.toUpperCase());
    }

    public ProjectTask findProjectTaskBySequence(String backlog_id, String pt_id, String username) {

        // service checks if backlog exists
        projectService.findProjectByIdentifier(backlog_id, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException(String.format("Project Task: %s does not exist", pt_id));
        }

        if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException(String.format("Project Task: %s does not exist in project: %s", pt_id, backlog_id));
        }

        return projectTask;

    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {

        // service is called to check if project task exists
        findProjectTaskBySequence(backlog_id, pt_id, username);

        return projectTaskRepository.save(updatedTask);

    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String pt_id, String username) {

        ProjectTask projectTask = findProjectTaskBySequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);

    }
}
