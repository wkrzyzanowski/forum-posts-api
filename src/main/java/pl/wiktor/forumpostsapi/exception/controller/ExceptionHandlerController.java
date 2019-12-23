package pl.wiktor.forumpostsapi.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.wiktor.forumpostsapi.exception.*;
import pl.wiktor.forumpostsapi.exception.model.ErrorResponse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TopicException.class})
    public ResponseEntity<Object> handleTopicExceptions(TopicException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<Object> handleUserExceptions(UserException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({ExternalServiceException.class})
    public ResponseEntity<Object> handleExternalServiceExceptions(ExternalServiceException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({InternalServiceException.class})
    public ResponseEntity<Object> handleInternalServiceExceptions(InternalServiceException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({LikeException.class})
    public ResponseEntity<Object> handleLikeExceptions(LikeException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @ExceptionHandler({PostException.class})
    public ResponseEntity<Object> handlePostExceptions(PostException e) {
        String message = MessageFormat.format("Unexpected error occurs: {0}", e.getMessage());
        log.error(message);
        return ResponseEntity
                .badRequest()
                .body(Collections.singletonList(new ErrorResponse(message, e.getDetails())));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse(error.getField() + " " + error.getDefaultMessage(), error.toString()));
        }
        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
