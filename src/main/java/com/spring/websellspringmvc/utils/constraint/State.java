package com.spring.websellspringmvc.utils.constraint;

import lombok.Getter;

@Getter
public enum State {
    VISIBLE(true),
    HIDE(false);

    private final boolean value;

    private State(boolean value) {
        this.value = value;
    }
}
