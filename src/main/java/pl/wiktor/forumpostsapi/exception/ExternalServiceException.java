package pl.wiktor.forumpostsapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExternalServiceException extends RuntimeException {

    public static final String SERVER_SIDE_ERROR = "External service has error or it is not available.";

    private String message;

    private String details;

    public ExternalServiceException(String message) {
        this.message = message;
    }

    public ExternalServiceException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
