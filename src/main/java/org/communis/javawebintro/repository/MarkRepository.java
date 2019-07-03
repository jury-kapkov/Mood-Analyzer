package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MarkRepository  extends JpaRepository<Mark, Long>, JpaSpecificationExecutor<Mark>
{
    Optional<Mark> findById(Long id);
}
