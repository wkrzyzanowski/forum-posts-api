package pl.wiktor.forumpostsapi.management.topics.model;

import lombok.*;
import pl.wiktor.forumpostsapi.management.topics.model.validation.FirstTopicValidation;
import pl.wiktor.forumpostsapi.management.topics.model.validation.TopicValidation;

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
public class TopicDTO {

    private String uuid;

    @NotNull(groups = {FirstTopicValidation.class, TopicValidation.class})
    @NotEmpty(groups = {FirstTopicValidation.class, TopicValidation.class})
    private String authorUuid;

    @NotNull(groups = {FirstTopicValidation.class, TopicValidation.class})
    @NotEmpty(groups = {FirstTopicValidation.class, TopicValidation.class})
    @Size(min = 5,
            message = "Min. topic length is 5.",
            groups = {FirstTopicValidation.class, TopicValidation.class})
    private String title;

    private LocalDateTime creationDate;

    private LocalDateTime lastPostDate;

    private boolean important;

    private boolean active;

}
