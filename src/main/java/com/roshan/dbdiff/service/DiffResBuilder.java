package com.roshan.dbdiff.service;

import com.roshan.dbdiff.entity.Table;
import com.roshan.dbdiff.vo.DiffRes;

import java.util.List;
import java.util.stream.Collectors;

public class DiffResBuilder {

    private final DiffRes different = new DiffRes();

    public DiffResBuilder builderInsert(List<Table> insert) {
        different.getTables().addAll(insert);
        return this;
    }

    public DiffResBuilder builderUpdate(List<Table> update) {
        different.getTables().addAll(update);
        return this;
    }

    public DiffResBuilder builderDelete(List<Table> delete) {
        different.getTables().addAll(delete);
        return this;
    }

    public DiffRes build() {
        return this.different;
    }
}
