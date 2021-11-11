package com.huhoot.repository;

import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    Challenge findOneById(int id);

    Page<Challenge> findAllByTitleContainingIgnoreCaseAndAdminId(String title, int id, Pageable pageable);

    List<Challenge> findAllByAdminIdAndIdInAndIsDeletedFalse(int id, List<Integer> ids);

    Page<Challenge> findAllByAdminIdAndIsDeletedFalse(int id, Pageable pageable);

    Page<Challenge> findAll(Pageable pageable);

    Page<Challenge> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
}
