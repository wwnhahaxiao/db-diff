package com.roshan.dbdiff.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String name;
    private String state;
    private List<String> ddl = new ArrayList<>();

    private List<Field> children = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }
}
