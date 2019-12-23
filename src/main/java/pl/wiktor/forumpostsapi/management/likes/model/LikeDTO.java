package pl.wiktor.forumpostsapi.management.likes.model;


import lombok.*;
import pl.wiktor.forumpostsapi.management.likes.model.validation.LikeValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LikeDTO {


    private String uuid;

    @NotNull(groups = {LikeValidation.class})
    @NotEmpty(groups = {LikeValidation.class})
    private String post;

    @NotNull(groups = {LikeValidation.class})
    @NotEmpty(groups = {LikeValidation.class})
    private String topic;

    @NotNull(groups = {LikeValidation.class})
    @NotEmpty(groups = {LikeValidation.class})
    private String loggedUser;

    @NotNull(groups = {LikeValidation.class})
    private int likeStatus;
}
