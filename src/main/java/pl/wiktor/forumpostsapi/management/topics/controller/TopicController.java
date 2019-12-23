package pl.wiktor.forumpostsapi.management.topics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumpostsapi.management.topics.service.TopicService;

@Slf4j
@RestController
@RequestMapping("/mgmt")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    public ResponseEntity<Object> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }




}
