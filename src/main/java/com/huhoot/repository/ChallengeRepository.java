package com.huhoot.repository;

import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    Page<Challenge> findAllByAdminId(int adminId, Pageable pageable);

    Challenge findOneById(int id);

    Page<Challenge> findAllByTitleContainingIgnoreCaseAndAdminId(String title, int id, Pageable pageable);

    Page<Challenge> findAll(Pageable pageable);

    // List<Challenge> findAllByAdminIdAndId(int id, List<Integer> ids);
}
