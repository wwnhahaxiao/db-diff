package com.roshan.dbdiff.vo;

import com.roshan.dbdiff.entity.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiffRes {
    private List<Table> tables = new ArrayList<>();
}
