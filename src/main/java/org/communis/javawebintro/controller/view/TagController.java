package org.communis.javawebintro.controller.view;

import lombok.extern.log4j.Log4j2;
import org.communis.javawebintro.dto.TagWrapper;
import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.enums.UserAuth;
import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.repository.TagRepository;
import org.communis.javawebintro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;

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
    //Delete tag by id
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public ModelAndView editPage(@PathVariable("id") Long id) throws ServerException {
//        ModelAndView deleteTagPage = new ModelAndView(TAG_VIEWS_PATH + "delete");
////        editPage.addObject("user", userService.getById(id));
////        editPage.addObject("authList", UserAuth.values());
////        prepareUserModelAndView(editPage);
//        return editPage;
//    }
}
