package pl.wiktor.forumpostsapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicException extends RuntimeException {

    public static final String UUID_NOT_FOUND = "Cannot find topic with UUID: {0}";

    private String message;

    private String details;

    public TopicException(String message) {
        this.message = message;
    }

    public TopicException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
