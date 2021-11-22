package com.huhoot.service;

import com.huhoot.model.Student;
import javassist.NotFoundException;

public interface StudentPlayService {

    void join(int challengeId, Student userDetails) throws NotFoundException;
}
