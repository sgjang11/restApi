package com.restApiStudy.restApi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/* TODO 1.
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;
// BeanSerializer를 사용함
public class EventResource extends RepresentationModel {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
*/
// TODO 2. 위의 코드 간략하게 이건 Bean이 아니기 때문에 객체로 사용해야함
public class EventResource extends EntityModel<Event> {

    // TODO self 로 가는 링크 추가
    public EventResource(Event event, Link... links) {
        super(event, Arrays.asList(links));
        // 이것은 경로가 변경되면 바꿔야해서 잘 사용 x
        // add(new Link("http://localhost:8080/api/events/"+ event.getId())); 이것과 동일함 근데 이상함.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}