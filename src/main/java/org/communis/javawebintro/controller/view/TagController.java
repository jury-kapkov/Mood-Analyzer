package org.communis.javawebintro.controller.view;

import lombok.extern.log4j.Log4j2;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
@RequestMapping(value = "admin/tags")

public class TagController {
    private String TAG_VIEWS_PATH = "admin/tags/";

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @RequestMapping(value = "")
    public ModelAndView list(Pageable pageable) throws ServerException {
        ModelAndView tagPage = new ModelAndView(TAG_VIEWS_PATH + "list");
        tagPage.addObject("page", tagRepository.findAll());
        return tagPage;
    }
}
