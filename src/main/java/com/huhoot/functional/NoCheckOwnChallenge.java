package com.huhoot.functional;

import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import com.huhoot.model.Challenge;
import org.springframework.stereotype.Component;

@Component
public class NoCheckOwnChallenge implements CheckedFunction<Admin, Challenge> {
    @Override
    public void accept(Admin admin, Challenge challenge) throws NotYourOwnException {
        return;
    }
}
