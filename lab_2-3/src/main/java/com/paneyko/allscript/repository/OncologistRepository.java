package com.paneyko.allscript.repository;

import com.paneyko.allscript.domain.Oncologist;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Oncologist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OncologistRepository extends JpaRepository<Oncologist, Long>, JpaSpecificationExecutor<Oncologist> {
}
