package com.restApiStudy.restApi.commons;

import com.restApiStudy.restApi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    /*public ErrorsResource(Errors errors, Link... links) {
        super(errors, Arrays.asList(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }*/

    public static EntityModel<Errors> modelOf(Errors errors) {
        EntityModel<Errors> errorsModel = EntityModel.of(errors);
        errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        return errorsModel;
    }

}

