package pl.wiktor.forumpostsapi.management.likes.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum LikeStatus {
    DOWN(-1),
    UP(1);

    private int status;

    LikeStatus(int status) {
        this.status = status;
    }

    @JsonCreator
    public static LikeStatus fromInteger(int status) {
        for (LikeStatus r : LikeStatus.values()) {
            if (r.getStatus() == status) {
                return r;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
