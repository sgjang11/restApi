package com.restApiStudy.restApi.events;

import com.restApiStudy.restApi.commons.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    //@Autowired
    //EventRepository eventRepository;

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidation eventValidation;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidation eventValidation) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidation = eventValidation;
    }

/*
    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(event.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }
*/
    // TODO Errors errors 이거는 json화 할 수 없음.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        /*Event event = Event.builder()
                .name(eventDto.getName())
                ...
                .build();*/
        // TODO dto에서 설정한 @들을 검증
        if (errors.hasErrors()) {
            // 여기서 build()에서 body(event)로 수정
            // json화 하여 보여줄것임. 하지만 errors는 json화 하지 못하여
            // ErrorsSerializer 클래스를 생성하여 보내줄 것임.
            //return ResponseEntity.badRequest().build();
            //return ResponseEntity.badRequest().body(errors);
            return badRequest(errors); // 리팩토링함
        }
        // TODO 이젠 데이터를 검증
        eventValidation.validate(eventDto, errors);
        if (errors.hasErrors()) {
            //return ResponseEntity.badRequest().build();
            //return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); // TODO event가 유료인지 무료인지, 장소가 비어있는지 확인 원래는 service 쪽에서 실행
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createUri = webMvcLinkBuilder.toUri();

        // return ResponseEntity.created(createUri).body(event); //HATEOAS를 적용하기위해 주석

        // TODO EventResource로 link를 만들 수 있음.
        EventResource eventResource = new EventResource(event);
        // 만약 EventResource에 생성했으면 안해도 됨.
        //eventResource.add(new Link()); // 이렇게도 가능 하지만 linkTo()를 사용
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        //eventResource.add(webMvcLinkBuilder.withSelfRel()); // self link EventResource에 생성
        eventResource.add(webMvcLinkBuilder.withRel("update-event")); // update link
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile")); // profile link

        return ResponseEntity.created(createUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, e -> new EventResource(e));
        // 이걸 var로 퉁칠수 있음 var pagedModel = assembler.toModel(page);
        // 그러면 java에서 컴파일 할 때 타입을 추론한다.
        // 주의 사항) java에서의 var는 타입변경이 안됨.
        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile")); // profile link
        return ResponseEntity.ok(pagedModel); // 하면 page의 기본 링크정보를 넘겨줌
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        // 보낼 때 resource로 만들어서 보낸다.
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile")); // profile link
        return ResponseEntity.ok(eventResource);
    }

    private static ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(ErrorsResource.modelOf(errors));
    }
}
