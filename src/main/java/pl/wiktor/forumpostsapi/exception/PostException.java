package pl.wiktor.forumpostsapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostException extends RuntimeException {
    public static final String UUID_NOT_FOUND = "Cannot find post with UUID: {0}";

    private String message;

    private String details;

    public PostException(String message) {
        this.message = message;
    }

    public PostException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
