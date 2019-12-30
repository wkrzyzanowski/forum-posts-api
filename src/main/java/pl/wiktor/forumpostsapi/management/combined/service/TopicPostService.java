package pl.wiktor.forumpostsapi.management.combined.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.config.security.jwt.JwtUtil;
import pl.wiktor.forumpostsapi.exception.PostException;
import pl.wiktor.forumpostsapi.exception.TopicException;
import pl.wiktor.forumpostsapi.management.combined.model.FirstTopicDTO;
import pl.wiktor.forumpostsapi.management.combined.model.TopicWithPostListDTO;
import pl.wiktor.forumpostsapi.management.posts.mapper.PostMapper;
import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.topics.mapper.TopicMapper;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;
import pl.wiktor.forumpostsapi.persistance.repository.LikeRepository;
import pl.wiktor.forumpostsapi.persistance.repository.PostsRepository;
import pl.wiktor.forumpostsapi.persistance.repository.TopicsRepository;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopicPostService {

    @Autowired
    private TopicsRepository topicsRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserExternalService userExternalService;
    @Autowired
    JwtUtil jwtUtil;


    public TopicWithPostListDTO getTopicWithPosts(String topicUuid) {

        TopicDTO topicDTO;

        TopicEntity topicEntity = topicsRepository.getByUuid(topicUuid).orElseThrow(() -> {
            log.error(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
            throw new TopicException(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
        });

        topicDTO = TopicMapper.fromEntityToDto(topicEntity);

        List<PostDTO> postDtoList = postsRepository.getAllByTopic(topicEntity)
                .stream()
                .map(PostMapper::fromEntityToDto)
                .collect(Collectors.toList());

        return new TopicWithPostListDTO(topicDTO, postDtoList);

    }

    public TopicDTO deleteTopic(String topicUuid, String token) {

        String tokenAuthorUuid = jwtUtil.extractUuid(token.substring(7));
        List<String> roles = jwtUtil.extractRoles(token.substring(7));

        TopicEntity topicEntity = getTopicEntity(topicUuid);

        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MOD")) {
            deleteSingleTopic(topicEntity);
        } else if (roles.contains("ROLE_USER") && topicEntity.getAuthorUuid().equals(tokenAuthorUuid)) {
            deleteSingleTopic(topicEntity);
        } else {
            throw new TopicException("Cannot delete topic. Check user privileges or author.");
        }

        return TopicMapper.fromEntityToDto(topicEntity);
    }

    private void deleteSingleTopic(TopicEntity topicEntity) {
        topicsRepository.delete(topicEntity);
        likeRepository.deleteAllByTopic(topicEntity.getUuid());
    }

    public PostDTO deletePost(String postUuid, String token) {
        String tokenAuthorUuid = jwtUtil.extractUuid(token.substring(7));
        List<String> roles = jwtUtil.extractRoles(token.substring(7));

        PostEntity postEntity = getPostEntity(postUuid);

        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MOD")) {
            deleteSinglePost(postEntity);
        } else if (roles.contains("ROLE_USER") && postEntity.getAuthorUuid().equals(tokenAuthorUuid)) {
            deleteSinglePost(postEntity);
        } else {
            throw new TopicException("Cannot delete topic. Check user privileges or author.");
        }

        return PostMapper.fromEntityToDto(postEntity);
    }

    void deleteSinglePost(PostEntity postEntity) {
        likeRepository.deleteAllByPost(postEntity.getUuid());
        postsRepository.delete(postEntity);
    }


    public TopicDTO changeTopicStatus(String topicUuid, String status, String token) {

        String tokenAuthorUuid = jwtUtil.extractUuid(token.substring(7));
        List<String> roles = jwtUtil.extractRoles(token.substring(7));
        TopicEntity topicEntity = getTopicEntity(topicUuid);

        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_MOD")) {
            topicEntity = changeActiveStatus(topicEntity, status);
        } else if (roles.contains("ROLE_USER") && topicEntity.getAuthorUuid().equals(tokenAuthorUuid)) {
            topicEntity = changeActiveStatus(topicEntity, status);
        } else {
            throw new TopicException("Cannot change topic active status. Check user privileges or author.");
        }

        return TopicMapper.fromEntityToDto(topicEntity);
    }

    private TopicEntity changeActiveStatus(TopicEntity topicEntity, String status) {
        if ("active".equals(status) || "inactive".equals(status)) {

            if ("active".equals(status)) {
                topicEntity.setActive(true);
            } else if ("inactive".equals(status)) {
                topicEntity.setActive(false);
            }

            topicsRepository.save(topicEntity);
        } else {
            throw new TopicException("Wrong status. Only two values are possible: 'active' or 'inactive'. Was: " + status);
        }
        return topicEntity;
    }


    public FirstTopicDTO createTopic(FirstTopicDTO firstTopicDTO, String token) {

        // TODO: NOT NECESSARY HERE BECAUSE ONLY LOGGED USER CAN CREATE TOPIC/POST
        // UserInfoDTO existingTopicAuthor = userExternalService.requestForUserByUuid(firstTopicDTO.getTopic().getAuthorUuid(), token);
        // UserInfoDTO existingPostAuthor = userExternalService.requestForUserByUuid(firstTopicDTO.getPost().getAuthorUuid(), token);

        LocalDateTime now = LocalDateTime.now().withNano(0);

        TopicEntity topicEntity = TopicMapper.fromDtoToEntity(firstTopicDTO.getTopic());
        topicEntity.setUuid(generateUuid());
        topicEntity.setActive(true);
        topicEntity.setCreationDate(now);
        topicEntity.setLastPostDate(now);

        topicsRepository.save(topicEntity);

        PostEntity postEntity = PostMapper.fromDtoToEntity(firstTopicDTO.getPost());
        postEntity.setUuid(generateUuid());
        postEntity.setCreationDate(now);
        postEntity.setTopic(topicEntity);

        postsRepository.save(postEntity);


        firstTopicDTO.setTopic(TopicMapper.fromEntityToDto(topicEntity));
        firstTopicDTO.setPost(PostMapper.fromEntityToDto(postEntity));

        return firstTopicDTO;
    }

    public PostDTO createPostForTopic(PostDTO postDTO, String topicUuid, String token) {

        // TODO: NOT NECESSARY HERE BECAUSE ONLY LOGGED USER CAN CREATE TOPIC/POST
        // UserInfoDTO existingPostAuthor = userExternalService.requestForUserByUuid(postDTO.getAuthorUuid(), token);

        LocalDateTime now = LocalDateTime.now().withNano(0);

        TopicEntity topicEntity = getTopicEntity(topicUuid);

        PostEntity postEntity = PostMapper.fromDtoToEntity(postDTO);
        postEntity.setUuid(generateUuid());
        postEntity.setCreationDate(now);
        postEntity.setTopic(topicEntity);

        postsRepository.save(postEntity);

        topicEntity.setLastPostDate(now);
        topicsRepository.save(topicEntity);

        return PostMapper.fromEntityToDto(postEntity);
    }

    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private TopicEntity getTopicEntity(String topicUuid) {
        return topicsRepository.getByUuid(topicUuid).orElseThrow(() -> {
            throw new TopicException(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
        });
    }

    private PostEntity getPostEntity(String postUuid) {
        return postsRepository.getByUuid(postUuid).orElseThrow(() -> {
            throw new PostException(MessageFormat.format(PostException.UUID_NOT_FOUND, postUuid));
        });
    }
}
