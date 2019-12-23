package pl.wiktor.forumpostsapi.persistance.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "POSTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String authorUuid;

    @Column
    private int likes;

    @Column
    private int dislikes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", creationDate=" + creationDate +
                ", content='" + content + '\'' +
                ", authorUuid='" + authorUuid + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
