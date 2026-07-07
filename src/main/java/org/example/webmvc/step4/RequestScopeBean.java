package org.example.webmvc.step4;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.ZonedDateTime;

@Component
@RequestScope
@Getter
public class RequestScopeBean {
    private final String uuid;
    private final ZonedDateTime creationTime;

    public RequestScopeBean() {
        this.uuid = java.util.UUID.randomUUID().toString();
        this.creationTime = ZonedDateTime.now();
        System.out.println("RequestScopeBean.RequestScopeBean : %s".formatted(uuid));
    }
}
