package org.communis.javawebintro.controller.rest;

import org.communis.javawebintro.dto.MarkWrapper;
import org.communis.javawebintro.dto.TagWrapper;
import org.communis.javawebintro.exception.InvalidDataException;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "marks")
public class MarkRestController {

    private final TagService tagService;

    @Autowired
    public MarkRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @DeleteMapping("{id}")
    public void deleteMark(@PathVariable Long id) throws InvalidDataException, ServerException {
        tagService.deleteMark(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void editMark(@Valid MarkWrapper markWrapper, BindingResult bindingResult) throws ServerException {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_VALIDATE_ERROR));
        }
        tagService.editMark(markWrapper);
    }
}
