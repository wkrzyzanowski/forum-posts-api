package pl.wiktor.forumpostsapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;

import java.util.Optional;

@Repository
public interface TopicsRepository extends JpaRepository<TopicEntity, Long> {

    Optional<TopicEntity> getByUuid(String uuid);

}
