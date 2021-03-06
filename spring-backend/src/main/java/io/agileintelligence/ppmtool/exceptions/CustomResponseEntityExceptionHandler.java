package io.agileintelligence.ppmtool.exceptions;

import io.agileintelligence.ppmtool.payload.ProjectAlreadyExistsExceptionResponse;
import io.agileintelligence.ppmtool.payload.ProjectNotFoundExceptionResponse;
import io.agileintelligence.ppmtool.payload.UsernameAlreadyExistsExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<ProjectAlreadyExistsExceptionResponse> handleProjectAlreadyExistsException(ProjectAlreadyExistsException ex, WebRequest req) {
        ProjectAlreadyExistsExceptionResponse exceptionResponse = new ProjectAlreadyExistsExceptionResponse(ex.getMessage());
        return new ResponseEntity<ProjectAlreadyExistsExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<ProjectNotFoundExceptionResponse> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest req) {
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
        return new ResponseEntity<ProjectNotFoundExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public final ResponseEntity<UsernameAlreadyExistsExceptionResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, WebRequest req) {
        UsernameAlreadyExistsExceptionResponse exceptionResponse = new UsernameAlreadyExistsExceptionResponse(ex.getMessage());
        return new ResponseEntity<UsernameAlreadyExistsExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
