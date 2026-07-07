package org.example.webmvc.step4;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.ZonedDateTime;

@Component
//@RequestScope
@SessionScope
@Getter
public class SessionScopeBean {
    private final String uuid;
    private final ZonedDateTime creationTime;

    public SessionScopeBean() {
        this.uuid = java.util.UUID.randomUUID().toString();
        this.creationTime = ZonedDateTime.now();
        System.out.println("SessionScopeBean.SessionScopeBean : %s".formatted(uuid));
    }

    private int count = 0;

    public void increment() {
        count++;
    }
}
