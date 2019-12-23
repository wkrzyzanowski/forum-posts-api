package pl.wiktor.forumpostsapi.management.combined.model;

import lombok.*;
import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.management.topics.model.validation.FirstTopicValidation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class FirstTopicDTO {

    @Valid
    @NotNull(groups = {FirstTopicValidation.class})
    private TopicDTO topic;

    @Valid
    @NotNull(groups = {FirstTopicValidation.class})
    private PostDTO post;

}
