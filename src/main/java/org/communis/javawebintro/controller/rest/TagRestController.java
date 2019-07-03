package org.communis.javawebintro.controller.rest;

import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.TagWrapper;
import org.communis.javawebintro.exception.InvalidDataException;
import org.communis.javawebintro.exception.NotFoundException;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "tags")
public class TagRestController {
    private final TagService tagService;

    @Autowired
    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void add(@Valid TagWrapper tagWrapper, BindingResult bindingResult) throws ServerException {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_VALIDATE_ERROR));
        }
        tagService.add(tagWrapper);
    }

    @DeleteMapping("{id}")
    public void deleteTag(@PathVariable Long id) throws InvalidDataException, ServerException {
        tagService.delete(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void editTag(@Valid TagWrapper tagWrapper, Long id, BindingResult bindingResult) throws ServerException {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_VALIDATE_ERROR));
        }
        tagService.edit(tagWrapper, id);
    }
}
