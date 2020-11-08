package io.agileintelligence.ppmtool.payload;

public class ProjectAlreadyExistsExceptionResponse {

    private String projectAlreadyExists;

    public ProjectAlreadyExistsExceptionResponse(String projectAlreadyExists) {
        this.projectAlreadyExists = projectAlreadyExists;
    }

    public String getProjectAlreadyExists() {
        return projectAlreadyExists;
    }

    public void setProjectAlreadyExists(String projectAlreadyExists) {
        this.projectAlreadyExists = projectAlreadyExists;
    }
}
