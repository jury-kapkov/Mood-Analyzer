package org.communis.javawebintro.service;

import org.communis.javawebintro.config.UserDetailsImp;
import org.communis.javawebintro.dto.MarkWrapper;
import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.TagWrapper;
import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.entity.Mark;
import org.communis.javawebintro.entity.Tag;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.MarkRepository;
import org.communis.javawebintro.repository.PermissionRepository;
import org.communis.javawebintro.repository.UserRepository;
import org.communis.javawebintro.repository.TagRepository;
import org.communis.javawebintro.repository.specifications.UserSpecification;
import org.communis.javawebintro.utils.CredentialsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = ServerException.class)

public class TagService {
    private final TagRepository tagRepository;
    private final MarkRepository markRepository;

    @Autowired
    public TagService(TagRepository tagRepository, MarkRepository markRepository) {
        this.tagRepository = tagRepository;
        this.markRepository = markRepository;
    }


    public void add(TagWrapper tagWrapper) throws ServerException {
        try {
            for (Tag tag : tagWrapper.getTags()) {
                if (tag.getName() != "") {
                    tag.setDateTimeCreate(new Date());
                    tagRepository.save(tag);
                }
            }
            Mark mark = new Mark();
            mark.setMark(tagWrapper.getMark());
            mark.setDateTimeCreate(new Date());
            markRepository.save(mark);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_ADDITING_ERROR), exception);
        }
    }

    public void delete(Long id) throws ServerException {
        try {
            Tag tag = getTagById(id);
            tagRepository.delete(tag);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_DELETE_ERROR), exception);
        }
    }

    public void deleteMark(Long id) throws ServerException {
        try {
            Mark mark = getMarkById(id);
            markRepository.delete(mark);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.MARK_DELETE_ERROR), exception);
        }
    }

    public void edit(TagWrapper tagWrapper, Long id) throws ServerException {
        try {
                Tag tag = getTagById(id);
                tagWrapper.fromWrapper(tag);
                Date date = tag.getDateTimeCreate();
                tag.setDateTimeCreate(date == null ? tag.getDateTimeCreate() : date);
                tagRepository.save(tag);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.TAG_EDIT_ERROR), exception);
        }
    }

    public void editMark(MarkWrapper markWrapper) throws ServerException {
        try {
            Mark mark = getMarkById(markWrapper.getId());
            markWrapper.fromWrapper(mark);
            Date date = markWrapper.getDateOfCreate();
            mark.setDateTimeCreate(date == null ? mark.getDateTimeCreate() : date);
            markRepository.save(mark);
        }catch (Exception exception) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.MARK_EDIT_ERROR), exception);
        }
    }

    public Tag getTagById(Long id) throws ServerException {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
    }
    public Mark getMarkById(Long id) throws ServerException {
        return markRepository.findById(id)
                .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
    }
}
