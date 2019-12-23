package pl.wiktor.forumpostsapi.management.likes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.exception.LikeException;
import pl.wiktor.forumpostsapi.exception.PostException;
import pl.wiktor.forumpostsapi.management.likes.mapper.LikeMapper;
import pl.wiktor.forumpostsapi.management.likes.model.LikeDTO;
import pl.wiktor.forumpostsapi.management.likes.model.LikeStatus;
import pl.wiktor.forumpostsapi.persistance.model.LikeEntity;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.repository.LikeRepository;
import pl.wiktor.forumpostsapi.persistance.repository.PostsRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LikesService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostsRepository postsRepository;

    public List<LikeDTO> getLikesByTopicAndUser(String topicUuid, String userUuid) {
        List<LikeEntity> likeEntityList = likeRepository.getAllByTopicAndLoggedUser(topicUuid, userUuid);
        return likeEntityList.stream().map(LikeMapper::fromEntityToDto).collect(Collectors.toList());
    }

    public LikeDTO setLikeStatus(LikeDTO likeDTO) {
        PostEntity postEntity = postsRepository.getByUuid(likeDTO.getPost()).orElseThrow(() -> {
            throw new PostException(MessageFormat.format(PostException.UUID_NOT_FOUND, likeDTO.getPost()));
        });

        LikeEntity likeEntity;

        int likes = postEntity.getLikes();
        int dislikes = postEntity.getDislikes();

        if (likeDTO.getUuid() == null || likeDTO.getUuid().isEmpty()) {
            likeEntity = LikeMapper.fromDtoToEntity(likeDTO);
            likeEntity.setUuid(UUID.randomUUID().toString());

            if (likeDTO.getLikeStatus() == LikeStatus.UP.getStatus()) {
                postEntity.setLikes(++likes);
            } else if (likeDTO.getLikeStatus() == LikeStatus.DOWN.getStatus()) {
                postEntity.setDislikes(++dislikes);
            }

        } else {
            likeEntity = likeRepository.getByUuid(likeDTO.getUuid()).orElseThrow(() -> {
                throw new LikeException(LikeException.UUID_NOT_FOUND);
            });

            int likeStatusBefore = likeEntity.getLikeStatus();

            likeEntity.setLikeStatus(LikeMapper.resolveStatus(likeDTO.getLikeStatus()));

            if (likeStatusBefore != likeDTO.getLikeStatus()) {
                if (likeDTO.getLikeStatus() == LikeStatus.UP.getStatus()) {
                    postEntity.setLikes(++likes);
                    if (dislikes != 0) {
                        postEntity.setDislikes(--dislikes);
                    }
                } else if (likeDTO.getLikeStatus() == LikeStatus.DOWN.getStatus()) {
                    postEntity.setDislikes(++dislikes);
                    if (likes != 0) {
                        postEntity.setLikes(--likes);
                    }
                }
            }
        }


        final LikeDTO dto = LikeMapper.fromEntityToDto(likeEntity);

        postsRepository.save(postEntity);
        likeRepository.save(likeEntity);

        return dto;
    }


}
