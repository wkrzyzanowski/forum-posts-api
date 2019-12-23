package pl.wiktor.forumpostsapi.management.combined.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumpostsapi.exception.TopicException;
import pl.wiktor.forumpostsapi.management.combined.model.FirstTopicDTO;
import pl.wiktor.forumpostsapi.management.combined.model.TopicWithPostListDTO;
import pl.wiktor.forumpostsapi.management.combined.service.TopicPostService;
import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.posts.model.validation.PostValidation;
import pl.wiktor.forumpostsapi.management.topics.model.validation.FirstTopicValidation;

import java.text.MessageFormat;

@RestController
@RequestMapping("/mgmt/combined")
public class TopicPostController {

    @Autowired
    private TopicPostService topicPostService;

    @GetMapping("/{topicUuid}")
    public ResponseEntity<TopicWithPostListDTO> getTopicByUuidWithPosts(@PathVariable("topicUuid") String topicUuid) {

        if (topicUuid == null || topicUuid.isEmpty()) {
            throw new TopicException(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
        }

        return ResponseEntity.ok(topicPostService.getTopicWithPosts(topicUuid));
    }

    @PostMapping("/topic")
    public ResponseEntity<Object> createTopic(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @Validated(FirstTopicValidation.class) FirstTopicDTO firstTopicDTO) {

        FirstTopicDTO dto = topicPostService.createTopic(firstTopicDTO, token);


        return ResponseEntity.ok(dto);
    }

    @PostMapping("/topic/{uuid}")
    public ResponseEntity<Object> createPostForTopic(
            @PathVariable("uuid") String topicUuid,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @Validated(PostValidation.class) PostDTO postDTO) {

        if (topicUuid == null || topicUuid.isEmpty()) {
            throw new TopicException(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
        }

        PostDTO dto = topicPostService.createPostForTopic(postDTO, topicUuid, token);


        return ResponseEntity.ok(dto);
    }

}
