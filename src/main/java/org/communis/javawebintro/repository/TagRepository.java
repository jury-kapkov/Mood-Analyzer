package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.Tag;
import org.communis.javawebintro.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TagRepository  extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag>
{
    Optional<Tag> findByName(String name);
}
