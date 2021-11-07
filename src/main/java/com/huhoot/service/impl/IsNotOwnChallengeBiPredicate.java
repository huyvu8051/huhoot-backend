package com.huhoot.service.impl;

import java.util.function.BiPredicate;

public class IsNotOwnChallengeBiPredicate implements BiPredicate<Integer, Integer> {

    @Override
    public boolean test(Integer adminId, Integer challengeAdminId) {
        return adminId != challengeAdminId;
    }
}
