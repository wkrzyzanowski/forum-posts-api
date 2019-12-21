package pl.wiktor.forumpostsapi.jwt.model;

import lombok.Data;

import java.util.List;

@Data
public class UserSecurity {
    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private boolean active;

    private String department;

    private List<String> roles;
}
