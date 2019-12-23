package pl.wiktor.forumpostsapi.management.topics.mapper;

import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;

import java.util.List;

public class TopicMapper {

    public static TopicEntity fromDtoToEntity(TopicDTO dto) {
        return TopicEntity.builder()
                .uuid(dto.getUuid())
                .authorUuid(dto.getAuthorUuid())
                .title(dto.getTitle())
                .creationDate(dto.getCreationDate())
                .lastPostDate(dto.getLastPostDate())
                .important(dto.isImportant())
                .active(dto.isActive())
                .build();
    }

    public static TopicDTO fromEntityToDto(TopicEntity entity) {
        return TopicDTO.builder()
                .uuid(entity.getUuid())
                .authorUuid(entity.getAuthorUuid())
                .title(entity.getTitle())
                .creationDate(entity.getCreationDate())
                .lastPostDate(entity.getLastPostDate())
                .important(entity.isImportant())
                .active(entity.isActive())
                .build();
    }


}
