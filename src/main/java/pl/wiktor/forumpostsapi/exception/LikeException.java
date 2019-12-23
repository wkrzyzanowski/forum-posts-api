package pl.wiktor.forumpostsapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeException extends RuntimeException {
    public static final String UUID_NOT_FOUND = "Cannot find like with UUID: {0}";
    public static final String WRONG_STATUS = "Wrong Like status.";

    private String message;

    private String details;

    public LikeException(String message) {
        this.message = message;
    }

    public LikeException(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
