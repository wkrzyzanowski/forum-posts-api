package pl.wiktor.forumpostsapi.management.combined.controller;

import lombok.extern.slf4j.Slf4j;
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
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.management.topics.model.validation.FirstTopicValidation;

import javax.websocket.server.PathParam;
import java.text.MessageFormat;

@Slf4j
@RestController
@RequestMapping("/mgmt/combined")
public class TopicPostController {

    @Autowired
    private TopicPostService topicPostService;


    @GetMapping("/topic/{uuid}")
    public ResponseEntity<TopicWithPostListDTO> getTopicByUuidWithPosts(@PathVariable("uuid") String topicUuid) {

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

    @PutMapping("/topic/{uuid}")
    public ResponseEntity<Object> changeTopicStatus(
            @PathVariable("uuid") String topicUuid,
            @PathParam("status") String status,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {

        TopicDTO dto = topicPostService.changeTopicStatus(topicUuid, status, token);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/post/{uuid}")
    public ResponseEntity<Object> deletePost(
            @PathVariable("uuid") String postUuid,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {

        PostDTO dto = topicPostService.deletePost(postUuid, token);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/topic/{uuid}")
    public ResponseEntity<Object> deleteTopicWithAllPosts(
            @PathVariable("uuid") String topicUuid,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        log.debug("Delete topic with uuid: " + topicUuid);
        TopicDTO dto = topicPostService.deleteTopic(topicUuid, token);

        return ResponseEntity.ok(dto);
    }

}
