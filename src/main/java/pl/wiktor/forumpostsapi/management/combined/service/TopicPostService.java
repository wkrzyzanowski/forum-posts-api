package pl.wiktor.forumpostsapi.management.combined.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.exception.TopicException;
import pl.wiktor.forumpostsapi.management.combined.model.FirstTopicDTO;
import pl.wiktor.forumpostsapi.management.combined.model.TopicWithPostListDTO;
import pl.wiktor.forumpostsapi.management.combined.model.UserInfoDTO;
import pl.wiktor.forumpostsapi.management.posts.mapper.PostMapper;
import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.topics.mapper.TopicMapper;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;
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
    private UserExternalService userExternalService;


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


    public FirstTopicDTO createTopic(FirstTopicDTO firstTopicDTO, String token) {

        // TODO: NOT NECESSARY HERE BECAUSE ONLY LOGGED USER CAN CREATE TOPIC/POST
        // UserInfoDTO existingTopicAuthor = userExternalService.requestForUserByUuid(firstTopicDTO.getTopic().getAuthorUuid(), token);
        // UserInfoDTO existingPostAuthor = userExternalService.requestForUserByUuid(firstTopicDTO.getPost().getAuthorUuid(), token);

        LocalDateTime now = LocalDateTime.now();

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
         UserInfoDTO existingPostAuthor = userExternalService.requestForUserByUuid(postDTO.getAuthorUuid(), token);

        LocalDateTime now = LocalDateTime.now();

        TopicEntity topicEntity = topicsRepository.getByUuid(topicUuid).orElseThrow(() -> {
            throw new TopicException(MessageFormat.format(TopicException.UUID_NOT_FOUND, topicUuid));
        });

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


}
