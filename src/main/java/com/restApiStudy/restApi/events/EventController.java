package com.restApiStudy.restApi.events;

import com.restApiStudy.restApi.account.Account;
import com.restApiStudy.restApi.account.AccountAdapter;
import com.restApiStudy.restApi.account.CurrentUser;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account currentUser) {
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
        event.setManager(currentUser);
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
    public ResponseEntity queryEvents(
            Pageable pageable
            , PagedResourcesAssembler<Event> assembler
            //, @AuthenticationPrincipal User user // 여기선 user는 AccountService의 userdetails user임
            //, @AuthenticationPrincipal AccountAdapter currentUser
            //, @AuthenticationPrincipal(expression = "account") Account account
            , @CurrentUser Account account
    ) {

        // 이것을 위의 어노테이션으로 받을 수 있다.
        // 현재 인증한 사용자 정보를 확인할 수 있다.
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 원래 타입은 Object이지만 spring security user로 받을 수 있다.
        //User principal = (User) authentication.getPrincipal();

        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, e -> new EventResource(e));
        // 이걸 var로 퉁칠수 있음 var pagedModel = assembler.toModel(page);
        // 그러면 java에서 컴파일 할 때 타입을 추론한다.
        // 주의 사항) java에서의 var는 타입변경이 안됨.

        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile")); // profile link
        if (account != null) { //user가 null이 아니면 create-event 링크를 추가해라
            pagedModel.add(linkTo(EventController.class).withRel("create-event")); // create-event link
        }
        return ResponseEntity.ok(pagedModel); // 하면 page의 기본 링크정보를 넘겨줌
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        // 보낼 때 resource로 만들어서 보낸다.
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile")); // profile link
        // 만약 매니저정보가 현재사용자와 같다면
        // 링크를 더하라
        if (event.getManager().equals(currentUser)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent(
            @PathVariable Integer id
            , @RequestBody @Valid EventDto eventDto
            , Errors errors
            , @CurrentUser Account currentUser
    ) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        // 이것은 EventDto의 @들에게 에러가 발생한 것을 확인 시켜줌
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        // 비즈니스 로직을 위한 validate 사용
        this.eventValidation.validate(eventDto, errors);
        // 여기서 에러가 있다면 로직상 에러가 있다는 것을 확인 시켜줌
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        // 여기서 에러가 없다면 이제 수정을 한다.
        Event existingEvent = optionalEvent.get();

        if (!existingEvent.getManager().equals(currentUser)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED); // 인가되지 않았다는 응답으로 리턴
        }

        // ~ 에서 ~ 로 , eventDto에다가 existingEvent이걸 덮어씀
        this.modelMapper.map(eventDto, existingEvent);
        // 저장해줌
        Event savedEvent = this.eventRepository.save(existingEvent);
        // 그리고 resource로 변환하고 profile 생성 후 ok에 담아서 보내줌
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile")); // profile link)
        return ResponseEntity.ok(eventResource);
    }

    private static ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(ErrorsResource.modelOf(errors));
    }
}
