package com.roshan.dbdiff.service;

import com.roshan.dbdiff.entity.Table;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompareContext {
    private SqlSession src;
    private SqlSession tar;

    private List<Table> insert = new ArrayList<>();
    private List<Table> update = new ArrayList<>();
    private List<Table> delete = new ArrayList<>();

    private List<String> skipPatterns = new ArrayList<>();

    public CompareContext(SqlSession src, SqlSession tar) {
        this.src = src;
        this.tar = tar;
    }
}
