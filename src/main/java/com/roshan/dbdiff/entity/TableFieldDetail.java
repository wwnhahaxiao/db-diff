package com.roshan.dbdiff.entity;

import lombok.Data;

@Data
public class TableFieldDetail {
    private String field;
    private String type;
    private String nullable;
    private String key;
    private String defaultValue;
    private String extra;
    private String comment;
}
