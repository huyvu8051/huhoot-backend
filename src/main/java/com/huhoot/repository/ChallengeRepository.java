package com.huhoot.repository;

import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    Page<Challenge> findAllByAdminId(int adminId, Pageable pageable);

    Challenge findOneById(int id);
}
