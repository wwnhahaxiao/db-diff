package com.roshan.dbdiff.controller;

import com.roshan.dbdiff.service.DiffService;
import com.roshan.dbdiff.vo.DiffRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataBaseController {
    @Autowired
    private DiffService diffService;

    @GetMapping("/compare")
    public DiffRes compare(@RequestParam String srcUrl, @RequestParam  String srcUsername,
                           @RequestParam String srcPassword, @RequestParam String srcDatabase,
                           @RequestParam String tarUrl, @RequestParam String tarUsername,
                           @RequestParam String tarPassword, @RequestParam String tarDatabase,
                           @RequestParam(required = false) List<String> skipPatterns) {
        return diffService.compare(srcUrl, srcUsername, srcPassword, srcDatabase,
                tarUrl, tarUsername, tarPassword, tarDatabase, skipPatterns
        );
    }
}
