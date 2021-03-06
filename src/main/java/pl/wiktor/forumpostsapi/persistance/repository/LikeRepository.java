package pl.wiktor.forumpostsapi.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.forumpostsapi.persistance.model.LikeEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    List<LikeEntity> getAllByTopicAndLoggedUser(String topic, String loggedUser);

    Optional<LikeEntity> getByUuid(String uuid);

    @Transactional
    void deleteAllByTopic(String topic);

    @Transactional
    void deleteAllByPost(String post);

}
