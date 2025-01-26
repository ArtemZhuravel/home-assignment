package org.secfix.repository;

import org.secfix.model.ObservedRepo;
import org.secfix.model.ObservedRepoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservedRepoRepository extends JpaRepository<ObservedRepo, UUID> {
    List<ObservedRepo> findByStatus(ObservedRepoStatus status);
}
