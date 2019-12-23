package pl.wiktor.forumpostsapi.management.combined.model;

import lombok.*;
import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TopicWithPostListDTO {

    private TopicDTO topic;

    private List<PostDTO> posts;

}
