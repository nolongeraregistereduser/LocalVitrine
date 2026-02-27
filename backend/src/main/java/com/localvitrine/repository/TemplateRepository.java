package com.localvitrine.repository;

import com.localvitrine.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByIsActiveTrueOrderByNameAsc();

    Optional<Template> findByIdAndIsActiveTrue(Long id);
}
