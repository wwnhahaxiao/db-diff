package com.roshan.dbdiff.service;

import com.roshan.dbdiff.entity.Field;
import com.roshan.dbdiff.entity.TableFieldDetail;
import com.roshan.dbdiff.mapper.TableMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CompareFieldTask implements CompareTask {
    @Override
    public boolean compareSwitch() {
        return true;
    }

    @Override
    public void compare(CompareContext ctx) {
        TableMapper srcMapper = ctx.getSrc().getMapper(TableMapper.class);
        TableMapper tarMapper = ctx.getTar().getMapper(TableMapper.class);
        ctx.getUpdate().forEach(table -> {
            table.setState("keep");
            Map<String, TableFieldDetail> srcFields = srcMapper.showColumns(table.getName()).stream()
                    .collect(Collectors.toMap(TableFieldDetail::getField, Function.identity()));
            Map<String, TableFieldDetail> tarFields = tarMapper.showColumns(table.getName()).stream()
                    .collect(Collectors.toMap(TableFieldDetail::getField, Function.identity()));
            Field pref = null;
            for (Map.Entry<String, TableFieldDetail> entry : srcFields.entrySet()) {
                String key = entry.getKey();
                TableFieldDetail value = entry.getValue();
                Field field = new Field(key);
                if (!tarFields.containsKey(key)) {
                    // 增加字段
                    String ddl = "ALTER TABLE `" + table.getName() + "` ADD COLUMN `" + key + "` "
                            + value.getType() + " " + (value.getNullable().equals("YES") ? "NULL" : "NOT NULL")
                            + " DEFAULT " + value.getDefaultValue()
                            + (StringUtils.hasText(value.getComment()) ? " COMMENT '" + value.getComment() + "'" : "")
                            + " AFTER `" + (Objects.isNull(pref) ? "FIRST" : pref.getName()) + "`;";
                    field.getDdl().add(ddl);
                    field.setState("insert");
                    table.setState("update");
                } else {
                    // 修改字段
                    if (!value.getType().equals(tarFields.get(key).getType())
                            || !Objects.equals(value.getComment(), tarFields.get(key).getComment())) {
                        String ddl = "ALTER TABLE `" + table.getName() + "` MODIFY COLUMN `" + key + "` "
                                + value.getType() + " " + (value.getNullable().equals("YES") ? "NULL" : "NOT NULL")
                                + " DEFAULT " + value.getDefaultValue()
                                + (StringUtils.hasText(value.getComment()) ? " COMMENT '" + value.getComment() + "'" : "")
                                + " AFTER `" + (Objects.isNull(pref) ? "FIRST" : pref.getName()) + "`;";
                        field.getDdl().add(ddl);
                        field.setState("update");
                        table.setState("update");
                    }
                }
                pref = field;
                table.getChildren().add(field);
                tarFields.remove(key);
            }
            for (Map.Entry<String, TableFieldDetail> entry : tarFields.entrySet()) {
                // 删除字段
                Field field = new Field(entry.getKey());
                field.setName(entry.getKey());
                field.getDdl().add("ALTER TABLE `" + table.getName() + "` DROP COLUMN `" + entry.getKey() + "`;");
                field.setState("delete");
                table.getChildren().add(field);
                table.setState("update");
            }
        });
    }

    @Override
    public int getOrder() {
        return Order.Field.ordinal();
    }
}
