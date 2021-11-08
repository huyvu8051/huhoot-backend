package com.huhoot.functional.impl;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.CheckedFunction;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import org.springframework.stereotype.Component;

@Component
public class CheckOwnerChallenge implements CheckedFunction<Admin, Challenge> {

    @Override
    public void accept(Admin admin, Challenge challenge) throws NotYourOwnException {
        if (admin.getId() != challenge.getAdmin().getId()) {
            throw new NotYourOwnException("Not your own challenge");
        }
    }
}
