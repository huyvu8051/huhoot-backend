package com.huhoot.repository;

import com.huhoot.enums.ChallengeStatus;
import com.huhoot.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
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

    /**
     * Update challenge status
     *
     * @param challengeStatus challenge status
     * @param challengeId     challenge id
     * @param adminId         admin id
     */
    @Modifying
    @Transactional
    @Query("UPDATE Challenge c " +
            "SET c.ChallengeStatus = :challengeStatus " +
            "WHERE c.id =:challengeId AND c.admin.id = :adminId ")
    void updateChallengeStatusByIdAndAdminId(ChallengeStatus challengeStatus, int challengeId, int adminId);
}
