package com.roshan.dbdiff.service;

import com.roshan.dbdiff.vo.DiffRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiffService {

    @Autowired
    private List<CompareTask> tasks;

    public DiffRes compare(String srcUrl, String srcUsername, String srcPassword, String srcDatabase,
                           String tarUrl, String tarUsername, String tarPassword, String tarDatabase,
                           List<String> skipPatterns) {
        ConnectionManager manager = new ConnectionManager();
        manager.init(srcUrl, srcUsername, srcPassword, srcDatabase, tarUrl, tarUsername, tarPassword, tarDatabase);
        CompareContext ctx = new CompareContext(manager.getSrc(), manager.getTar());
        ctx.setSkipPatterns(skipPatterns);
        tasks.stream()
                .filter(CompareTask::compareSwitch)
                .forEach(task -> task.compare(ctx));
        manager.clear();
        DiffResBuilder builder = new DiffResBuilder();
        return builder.builderInsert(ctx.getInsert())
                .builderUpdate(ctx.getUpdate())
                .builderDelete(ctx.getDelete())
                .build();
    }
}
