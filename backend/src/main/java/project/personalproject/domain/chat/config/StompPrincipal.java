package project.personalproject.domain.chat.config;

import java.security.Principal;

public class StompPrincipal implements Principal {
    private final Long name;

    public StompPrincipal(Long name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name.toString();
    }
}
