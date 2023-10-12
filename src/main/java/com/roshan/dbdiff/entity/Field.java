package com.roshan.dbdiff.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Field {
    private String name;
    private String state;

    private List<String> ddl = new ArrayList<>();

    public Field(String name) {
        this.name = name;
    }
}
