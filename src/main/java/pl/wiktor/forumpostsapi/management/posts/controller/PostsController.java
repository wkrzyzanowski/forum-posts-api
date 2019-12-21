package pl.wiktor.forumpostsapi.management.posts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mgmt/")
public class PostsController {

    @GetMapping("/posts")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("OK!");
    }


}
