package pl.wiktor.forumpostsapi.management.topics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wiktor.forumpostsapi.management.topics.mapper.TopicMapper;
import pl.wiktor.forumpostsapi.management.topics.model.TopicDTO;
import pl.wiktor.forumpostsapi.persistance.model.TopicEntity;
import pl.wiktor.forumpostsapi.persistance.repository.TopicsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopicService {

    private TopicsRepository topicsRepository;

    public TopicService(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    public List<TopicDTO> getAllTopics() {
        List<TopicEntity> topicEntityList = topicsRepository.findAll();
        return topicEntityList.stream()
                .map(TopicMapper::fromEntityToDto)
                .collect(Collectors.toList());

    }


}
