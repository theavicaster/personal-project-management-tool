package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.domain.User;
import io.agileintelligence.ppmtool.exceptions.ProjectAlreadyExistsException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import io.agileintelligence.ppmtool.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project saveProject(Project project, String username) {
        try {

            User user = userRepository.findByUsername(username);

            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            Backlog backlog = new Backlog();
            project.setBacklog(backlog);
            backlog.setProject(project);
            backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectAlreadyExistsException(String.format("Project ID: %s already exists!", project.getProjectIdentifier().toUpperCase()));
        }

    }

    public Project updateProject(Project updatedProject, String username) {

        updatedProject.setProjectIdentifier(updatedProject.getProjectIdentifier().toUpperCase());

        // case where project to update does not exist
        // is thrown an exception in call to find service
        Project oldProject = findProjectByIdentifier(updatedProject.getProjectIdentifier(), username);

        updatedProject.setId(oldProject.getId());
        updatedProject.setBacklog(oldProject.getBacklog());
        updatedProject.setProjectLeader(oldProject.getProjectLeader());
        return projectRepository.save(updatedProject);

    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectNotFoundException(String.format("Project ID: %s does not exist", projectId));
        }
        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException(String.format("Project ID: %s does not belong to user: %s", projectId, username));
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {

        Project project = findProjectByIdentifier(projectId, username);
        projectRepository.delete(project);
    }
}
