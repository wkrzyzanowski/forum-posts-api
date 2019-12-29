package pl.wiktor.forumpostsapi.management.likes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.forumpostsapi.exception.LikeException;
import pl.wiktor.forumpostsapi.management.likes.model.LikeDTO;
import pl.wiktor.forumpostsapi.management.likes.model.validation.LikeValidation;
import pl.wiktor.forumpostsapi.management.likes.service.LikesService;

import javax.websocket.server.PathParam;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mgmt")
public class LikeController {

    @Autowired
    LikesService likesService;

    @GetMapping("/likes/{topicUuid}")
    public ResponseEntity<Object> getLikesByTopicAndUserUuid(@PathVariable("topicUuid") String topicUuid,
                                                             @PathParam("user") String user) {

        if (topicUuid == null || topicUuid.isEmpty() || user == null || user.isEmpty()) {
            throw new LikeException(MessageFormat.format(LikeException.UUID_NOT_FOUND, topicUuid));
        }

        List<LikeDTO> likeDTOList = likesService.getLikesByTopicAndUser(topicUuid, user);

        return ResponseEntity.ok(likeDTOList);
    }

    @PostMapping("/likes")
    public ResponseEntity<Object> modifyLike(@RequestBody @Validated(LikeValidation.class) LikeDTO likeDTO) {

        log.debug(likeDTO.toString());

        LikeDTO dto = likesService.setLikeStatus(likeDTO);

        return ResponseEntity.ok(dto);
    }


}
