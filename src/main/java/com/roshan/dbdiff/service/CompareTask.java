package com.roshan.dbdiff.service;

import org.springframework.core.Ordered;

public interface CompareTask extends Ordered {
    boolean compareSwitch();

    void compare(CompareContext ctx);

    enum Order {
        Table, Field
    }
}
