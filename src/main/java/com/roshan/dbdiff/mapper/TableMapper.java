package com.roshan.dbdiff.mapper;

import com.roshan.dbdiff.entity.TableFieldDetail;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Map;
import java.util.Set;

public interface TableMapper {
    @Select("SHOW COLUMNS FROM ${tableName}")
    @Results({
            @Result(property = "nullable", column = "null"),
            @Result(property = "defaultValue", column = "default"),
    })
    Set<TableFieldDetail> showColumns(String tableName);

    @Select("SHOW CREATE TABLE ${tableName}")
    Map<String, String> showCreate(String tableName);
}
