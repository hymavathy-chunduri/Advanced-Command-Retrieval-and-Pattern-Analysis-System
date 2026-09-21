package com.dsa.linuxintelligence.service;

import com.dsa.linuxintelligence.dto.CategoryStatsDto;
import com.dsa.linuxintelligence.dto.SystemStatsDto;
import com.dsa.linuxintelligence.model.Command;
import com.dsa.linuxintelligence.repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommandService {

    private final CommandRepository commandRepository;
    private final CommandSearchService commandSearchService;

    @Autowired
    public CommandService(CommandRepository commandRepository, CommandSearchService commandSearchService) {
        this.commandRepository = commandRepository;
        this.commandSearchService = commandSearchService;
    }

    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    public Optional<Command> getCommandByName(String name) {
        // Fast path: Try HashMap cache first (O(1))
        Optional<Command> cached = commandSearchService.findCommandByName(name);
        if (cached.isPresent()) {
            return cached;
        }
        // Fallback: Query PostgreSQL database
        return commandRepository.findByCommandIgnoreCase(name);
    }

    public List<Command> getCommandsByCategory(String category) {
        return commandRepository.findByCategoryIgnoreCase(category);
    }

    public List<String> getAllCategories() {
        return commandRepository.findAllCategories();
    }

    public Optional<Command> getRandomCommand() {
        return commandRepository.findRandomCommand();
    }

    public SystemStatsDto getSystemStats() {
        long totalCommands = commandRepository.count();
        List<String> categories = commandRepository.findAllCategories();
        List<Object[]> rawDistribution = commandRepository.countCommandsByCategory();

        List<CategoryStatsDto> distribution = new ArrayList<>();
        for (Object[] row : rawDistribution) {
            String cat = (String) row[0];
            Long cnt = (Long) row[1];
            distribution.add(new CategoryStatsDto(cat, cnt));
        }

        int cacheSize = commandSearchService.getFastLookupCache().size();

        return new SystemStatsDto(
                totalCommands,
                categories.size(),
                cacheSize,
                "Active (O(1) HashMap + Trie Index Loaded)",
                distribution
        );
    }
}
