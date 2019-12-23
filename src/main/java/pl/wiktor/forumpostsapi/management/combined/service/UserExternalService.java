package pl.wiktor.forumpostsapi.management.combined.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wiktor.forumpostsapi.management.combined.model.UserInfoDTO;
import pl.wiktor.forumpostsapi.management.combined.service.errorhandler.UserRestTemplateErrorHandler;


@Service
public class UserExternalService {

    private final String GET_USER_BY_UUID_BASE_URL = "http://localhost:9091/info/users/";

    private RestTemplate restTemplate;

    @Autowired
    public UserExternalService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(new UserRestTemplateErrorHandler())
                .build();
    }

    public UserInfoDTO requestForUserByUuid(String userUuid, String token) {
        token = removeBearerPrefix(token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<UserInfoDTO> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<UserInfoDTO> responseUser = restTemplate.exchange(GET_USER_BY_UUID_BASE_URL + userUuid, HttpMethod.GET, httpEntity, UserInfoDTO.class);
        return responseUser.getBody();
    }

    private String removeBearerPrefix(String token) {
        return token.substring(7);
    }


}
