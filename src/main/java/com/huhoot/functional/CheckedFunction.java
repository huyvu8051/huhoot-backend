package com.huhoot.functional;

import com.huhoot.exception.NotYourOwnException;

@FunctionalInterface
public interface CheckedFunction<T, U> {
    void accept(T t, U u) throws NotYourOwnException;
}