package com.roshan.dbdiff.service;

import com.roshan.dbdiff.entity.Table;
import com.roshan.dbdiff.mapper.DatabaseMapper;
import com.roshan.dbdiff.mapper.TableMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CompareTableTask implements CompareTask {
    @Override
    public boolean compareSwitch() {
        return true;
    }

    @Override
    public void compare(CompareContext ctx) {
        DatabaseMapper srcMapper = ctx.getSrc().getMapper(DatabaseMapper.class);
        Set<String> srcTables = srcMapper.showTables().stream()
                .filter(table -> filterByPattern(ctx.getSkipPatterns(), table))
                .collect(Collectors.toSet());
        DatabaseMapper tarMapper = ctx.getTar().getMapper(DatabaseMapper.class);
        Set<String> tarTables = tarMapper.showTables().stream()
                .filter(table -> filterByPattern(ctx.getSkipPatterns(), table))
                .collect(Collectors.toSet());
        srcTables.forEach(tableName -> {
            Table table = new Table(tableName);
            if (tarTables.contains(tableName)) {
                ctx.getUpdate().add(table);
                tarTables.remove(tableName);
            } else {
                table.setState("insert");
                TableMapper mapper = ctx.getSrc().getMapper(TableMapper.class);
                table.getDdl().add(mapper.showCreate(tableName).get("Create Table"));
                ctx.getInsert().add(table);
            }
        });
        tarTables.stream()
                .map(tableName -> {
                    Table table = new Table(tableName);
                    table.setState("delete");
                    table.getDdl().add("DROP TABLE IF EXISTS `" + tableName + "`;");
                    return table;
                })
                .forEach(ctx.getDelete()::add);
    }

    private boolean filterByPattern(List<String> patterns, String content) {
        if (CollectionUtils.isEmpty(patterns)) {
            return true;
        }
        boolean match = patterns.stream()
                .anyMatch(p -> Pattern.matches(p, content));
        return !match;
    }

    @Override
    public int getOrder() {
        return Order.Table.ordinal();
    }
}
