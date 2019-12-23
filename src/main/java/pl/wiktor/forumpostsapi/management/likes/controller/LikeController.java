package pl.wiktor.forumpostsapi.management.likes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.forumpostsapi.management.likes.model.LikeDTO;
import pl.wiktor.forumpostsapi.management.likes.model.validation.LikeValidation;
import pl.wiktor.forumpostsapi.management.likes.service.LikesService;

@Slf4j
@RestController
@RequestMapping("/mgmt")
public class LikeController {

    @Autowired
    LikesService likesService;

    // TODO: Implement second method from likes service !

    @PostMapping("/likes")
    public ResponseEntity<Object> modifyLike(@RequestBody @Validated(LikeValidation.class) LikeDTO likeDTO) {

        log.debug(likeDTO.toString());

        LikeDTO dto = likesService.setLikeStatus(likeDTO);

        return ResponseEntity.ok(dto);
    }


}
