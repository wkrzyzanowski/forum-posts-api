package pl.wiktor.forumpostsapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.forumpostsapi.persistance.model.PostEntity;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> getAllByTopic(TopicEntity topicEntity);

    Optional<PostEntity> getByUuid(String uuid);


}
