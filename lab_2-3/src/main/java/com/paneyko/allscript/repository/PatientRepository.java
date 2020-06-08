package com.paneyko.allscript.repository;

import com.paneyko.allscript.domain.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Patient entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    @Query(value = "select distinct patient from Patient patient left join fetch patient.therapists left join fetch patient.oncologists",
        countQuery = "select count(distinct patient) from Patient patient")
    Page<Patient> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct patient from Patient patient left join fetch patient.therapists left join fetch patient.oncologists")
    List<Patient> findAllWithEagerRelationships();

    @Query("select patient from Patient patient left join fetch patient.therapists left join fetch patient.oncologists where patient.id =:id")
    Optional<Patient> findOneWithEagerRelationships(@Param("id") Long id);
}
