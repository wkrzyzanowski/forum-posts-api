package pl.wiktor.forumpostsapi.management.likes.mapper;

import pl.wiktor.forumpostsapi.exception.LikeException;
import pl.wiktor.forumpostsapi.management.likes.model.LikeDTO;
import pl.wiktor.forumpostsapi.management.likes.model.LikeStatus;
import pl.wiktor.forumpostsapi.persistance.model.LikeEntity;

public class LikeMapper {

    public static LikeEntity fromDtoToEntity(LikeDTO dto) {
        return LikeEntity.builder()
                .uuid(dto.getUuid())
                .topic(dto.getTopic())
                .post(dto.getPost())
                .loggedUser(dto.getLoggedUser())
                .likeStatus(dto.getLikeStatus())
                .build();
    }

    public static LikeDTO fromEntityToDto(LikeEntity dto) {
        return LikeDTO.builder()
                .uuid(dto.getUuid())
                .topic(dto.getTopic())
                .post(dto.getPost())
                .loggedUser(dto.getLoggedUser())
                .likeStatus(resolveStatus(dto.getLikeStatus()))
                .build();
    }

    public static int resolveStatus(int status) {

        int likeStatus = 0;

        for (LikeStatus st : LikeStatus.values()) {
            if (st.getStatus() == status) {
                likeStatus = st.getStatus();
            }
        }

        if (likeStatus == 0) {
            throw new LikeException(LikeException.WRONG_STATUS);
        }
        return likeStatus;
    }
}
