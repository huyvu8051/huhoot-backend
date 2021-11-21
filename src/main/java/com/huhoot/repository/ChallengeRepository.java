package com.huhoot.repository;

import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    Optional<Challenge> findOneById(int id);

    Page<Challenge> findAllByTitleContainingIgnoreCaseAndAdminId(String title, int id, Pageable pageable);

    Page<Challenge> findAllByAdminId(int id, Pageable pageable);

    Page<Challenge> findAll(Pageable pageable);

    Page<Challenge> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Challenge> findAllByAdminIdAndIdIn(int id, List<Integer> ids);

    Optional<Challenge> findOneByIdAndAdminId(int challengeId, int adminId);

}
