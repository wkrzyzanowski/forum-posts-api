package pl.wiktor.forumpostsapi.management.posts.mapper;

import pl.wiktor.forumpostsapi.management.posts.model.PostDTO;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;

public class PostMapper {

    public static PostEntity fromDtoToEntity(PostDTO dto) {
        return PostEntity.builder()
                .uuid(dto.getUuid())
                .authorUuid(dto.getAuthorUuid())
                .content(dto.getContent())
                .creationDate(dto.getCreationDate())
                .likes(dto.getLikes())
                .dislikes(dto.getDislikes())
                .build();
    }

    public static PostDTO fromEntityToDto(PostEntity entity) {
        return PostDTO.builder()
                .uuid(entity.getUuid())
                .authorUuid(entity.getAuthorUuid())
                .content(entity.getContent())
                .creationDate(entity.getCreationDate())
                .likes(entity.getLikes())
                .dislikes(entity.getDislikes())
                .build();
    }

}
