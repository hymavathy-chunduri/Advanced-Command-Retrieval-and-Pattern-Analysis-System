package com.dsa.linuxintelligence.controller;

import com.dsa.linuxintelligence.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CommandService commandService;

    @Autowired
    public CategoryController(CommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * GET /api/categories - Returns list of all categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(commandService.getAllCategories());
    }
}
