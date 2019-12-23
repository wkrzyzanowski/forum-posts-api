package pl.wiktor.forumpostsapi.persistance.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LIKES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    private String post;

    private String topic;

    private String loggedUser;

    private int likeStatus;

}
