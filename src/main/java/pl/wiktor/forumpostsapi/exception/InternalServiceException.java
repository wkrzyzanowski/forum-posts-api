package pl.wiktor.forumpostsapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InternalServiceException extends RuntimeException {
    public static final String INTERNAL_SIDE_ERROR = "Internal service has error or request is not correct.";

    private String message;

    private String details;

    public InternalServiceException(String message) {
        this.message = message;
    }

    public InternalServiceException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
