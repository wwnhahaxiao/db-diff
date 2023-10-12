package com.roshan.dbdiff.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface DatabaseMapper {

    @Select("show tables")
    Set<String> showTables();
}
