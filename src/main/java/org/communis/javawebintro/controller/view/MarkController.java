package org.communis.javawebintro.controller.view;

import lombok.extern.log4j.Log4j2;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.repository.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
@RequestMapping(value = "admin/marks")

public class MarkController {
    private String MARK_VIEWS_PATH = "admin/marks/";

    private final MarkRepository markRepository;

    @Autowired
    public MarkController(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    @RequestMapping(value = "")
    public ModelAndView list(Pageable pageable) throws ServerException {
        ModelAndView tagPage = new ModelAndView(MARK_VIEWS_PATH + "marksList");
        tagPage.addObject("page", markRepository.findAll());
        return tagPage;
    }
}
