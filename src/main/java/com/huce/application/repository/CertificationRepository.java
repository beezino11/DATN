package com.huce.application.repository;

import com.huce.application.entity.Certification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    Certification findByName(String name);

    @Query(value = "SELECT * FROM certification c " +
            "WHERE c.id LIKE CONCAT('%',?1,'%') " +
            "AND c.name LIKE CONCAT('%',?2,'%') " +
            "AND c.status LIKE CONCAT('%',?3,'%')", nativeQuery = true)
    Page<Certification> adminGetListCertifications(String id, String name, String status, Pageable pageable);


}
