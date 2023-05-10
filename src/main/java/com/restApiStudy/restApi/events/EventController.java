package com.restApiStudy.restApi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

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
    // Errors errors 이거는 json화 할 수 없음.
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        /*Event event = Event.builder()
                .name(eventDto.getName())
                ...
                .build();*/
        // dto에서 설정한 @들을 검증
        if (errors.hasErrors()) {
            // 여기서 build()에서 body(event)로 수정
            // json화 하여 보여줄것임. 하지만 errors는 json화 하지 못하여
            // ErrorsSerializer 클래스를 생성하여 보내줄 것임.
            //return ResponseEntity.badRequest().build();
            return ResponseEntity.badRequest().body(errors);
        }
        // 이젠 데이터를 검증
        eventValidation.validate(eventDto, errors);
        if (errors.hasErrors()) {
            //return ResponseEntity.badRequest().build();
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);
    }
}
