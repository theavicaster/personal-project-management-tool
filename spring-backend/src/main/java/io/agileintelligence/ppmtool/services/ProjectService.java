package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            Backlog backlog = new Backlog();
            project.setBacklog(backlog);
            backlog.setProject(project);
            backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException(String.format("Project ID: %s already exists!", project.getProjectIdentifier().toUpperCase()));
        }

    }

    public Project updateProject(Project updatedProject) {

        updatedProject.setProjectIdentifier(updatedProject.getProjectIdentifier().toUpperCase());

        Project oldProject = projectRepository.findByProjectIdentifier(updatedProject.getProjectIdentifier());
        if (oldProject == null) {
            throw new ProjectIdException(String.format("Cannot update project as Project ID: %s does not exist", updatedProject.getProjectIdentifier()));
        }

        updatedProject.setId(oldProject.getId());
        updatedProject.setBacklog(oldProject.getBacklog());
        return projectRepository.save(updatedProject);

    }

    public Project findProjectByIdentifier(String projectId) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException(String.format("Project ID: %s does not exist", projectId));
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null) {
            throw new ProjectIdException(String.format("Cannot delete Project ID: %s as project does not exist", projectId));
        }

        projectRepository.delete(project);
    }
}
