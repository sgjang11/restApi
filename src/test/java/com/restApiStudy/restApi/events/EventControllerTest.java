package com.restApiStudy.restApi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restApiStudy.restApi.common.RestDocsConfiguration;
import com.restApiStudy.restApi.commons.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//1. @WebMvcTest // 요 아이는 슬라이스 테스트라 웹용 bean만 등록해줌 repository는 등록 안함.
//2.
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // RestDocs 사용하기 위해 추가
@Import(RestDocsConfiguration.class) // 사용할 설정 파일을 import해줌
@ActiveProfiles("test")
public class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

  /*1.  @MockBean
    EventRepository eventRepository;*/
/* 1.
    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .build();
        //event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .accept("application/hal+json; charset=UTF-8")
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                //.andExpect(header().exists("Location"))
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"))
                .andExpect(header().exists(HttpHeaders.LOCATION))
               // .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                // 이렇게 하면 id의 값이 100이면 테스트에 걸림
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
        ;
    }
*/

    // 2.
/*
    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .accept("application/hal+json; charset=UTF-8")
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }
*/

//////////////////////////////////////////////////////////

    // 입력값이 제대로 들어온 경우
    @Test
    @TestDescription("정상적으로 이벤트 생성 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .accept("application/hal+json; charset=UTF-8")
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                // link정보를 줘야함
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event" // 처음엔 이름을 줌
                        // 이제 아래와 같이 추가함.
                        , links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query"),
                                linkWithRel("update-event").description("link to update"),
                                linkWithRel("profile").description("link to profile")
                        )
                        , requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("accept content type")
                        )
                        , requestFields(
                                fieldWithPath("name").description("Name of new event")
                                , fieldWithPath("description").description("description of new event")
                                , fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event")
                                , fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event")
                                , fieldWithPath("beginEventDateTime").description("date time of begin of new event")
                                , fieldWithPath("endEventDateTime").description("date time of end of new event")
                                , fieldWithPath("location").description("location of new event")
                                , fieldWithPath("basePrice").description("base price of new event")
                                , fieldWithPath("maxPrice").description("max price of new event")
                                , fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        )
                        , responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header")
                                , headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        )
                        , responseFields(
                                fieldWithPath("id").description("Id of new event")
                                , fieldWithPath("name").description("Name of new event")
                                , fieldWithPath("description").description("description of new event")
                                , fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event")
                                , fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event")
                                , fieldWithPath("beginEventDateTime").description("date time of begin of new event")
                                , fieldWithPath("endEventDateTime").description("date time of end of new event")
                                , fieldWithPath("location").description("location of new event")
                                , fieldWithPath("basePrice").description("base price of new event")
                                , fieldWithPath("maxPrice").description("max price of new event")
                                , fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                                , fieldWithPath("free").description("it tells is this event is free or not")
                                , fieldWithPath("offline").description("it tells is this event is offline event or not")
                                , fieldWithPath("eventStatus").description("event status")
                                , fieldWithPath("_links.self.href").description("link to self")
                                , fieldWithPath("_links.query-events.href").description("link to query-events")
                                , fieldWithPath("_links.update-event.href").description("link to update-event")
                                , fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;
    }

    // 입력값 이외가 들어온 경우 에러를 발생시킴
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .accept("application/hal+json; charset=UTF-8")
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    // 값이 없는 경우
    @Test
    @TestDescription("입력 값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Enpty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }

    // 값이 이상한 경우 (시작날이 종료일보다 늦을 경우) 새로 만들어서 진행해야함
    @Test
    @TestDescription("입력 값이 잘못된 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .basePrice(10000) // 무조건 maxPrice보다 적어야함
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
                // 만약 FieldError일 경우 아래가 가능
                // 하지만 GlobalError일 경우 field와 rejectedValue가 없기 때문에 에러발생할 수 있음.
                .andDo(print())
                //.andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                //.andExpect(jsonPath("$[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

}