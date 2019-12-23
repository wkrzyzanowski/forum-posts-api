package pl.wiktor.forumpostsapi.management.posts.model;

import lombok.*;
import pl.wiktor.forumpostsapi.management.posts.model.validation.PostValidation;
import pl.wiktor.forumpostsapi.management.topics.model.validation.FirstTopicValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostDTO {

    private String uuid;

    private LocalDateTime creationDate;

    @NotNull(groups = {FirstTopicValidation.class, PostValidation.class})
    @NotEmpty(groups = {FirstTopicValidation.class, PostValidation.class})
    private String authorUuid;

    @NotEmpty(groups = {FirstTopicValidation.class, PostValidation.class})
    @NotNull(groups = {FirstTopicValidation.class, PostValidation.class})
    @Size(min = 1,
            message = "Min. post content length is 1.",
            groups = {FirstTopicValidation.class, PostValidation.class})
    private String content;

    private int likes;

    private int dislikes;

}
