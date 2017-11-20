package com.gymwebapp.domain.Validator;

import java.util.List;

/**
 * Created by elisei on 20.11.2017.
 */
public interface Validator<E> {
    List<String> validate(E e);
}
