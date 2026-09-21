package com.dsa.linuxintelligence.controller;

import com.dsa.linuxintelligence.dto.AutocompleteResponseDto;
import com.dsa.linuxintelligence.dto.SearchResponseDto;
import com.dsa.linuxintelligence.dto.SpellingCorrectionDto;
import com.dsa.linuxintelligence.dto.SystemStatsDto;
import com.dsa.linuxintelligence.model.Command;
import com.dsa.linuxintelligence.service.CommandSearchService;
import com.dsa.linuxintelligence.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CommandController {

    private final CommandService commandService;
    private final CommandSearchService commandSearchService;

    @Autowired
    public CommandController(CommandService commandService, CommandSearchService commandSearchService) {
        this.commandService = commandService;
        this.commandSearchService = commandSearchService;
    }

    /**
     * GET /api/commands - Returns all commands
     */
    @GetMapping("/commands")
    public ResponseEntity<List<Command>> getAllCommands() {
        return ResponseEntity.ok(commandService.getAllCommands());
    }

    /**
     * GET /api/commands/search?q=file - Rabin-Karp pattern search
     */
    @GetMapping("/commands/search")
    public ResponseEntity<SearchResponseDto> searchCommands(@RequestParam(value = "q", defaultValue = "") String query) {
        SearchResponseDto result = commandSearchService.searchByPattern(query);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/commands/autocomplete?q=mk - Trie prefix autocomplete
     */
    @GetMapping("/commands/autocomplete")
    public ResponseEntity<AutocompleteResponseDto> getAutocomplete(
            @RequestParam(value = "q", defaultValue = "") String prefix,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        AutocompleteResponseDto response = commandSearchService.getAutocompleteSuggestions(prefix, limit);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/commands/correct?q=grpe - Levenshtein Edit Distance spelling correction
     */
    @GetMapping("/commands/correct")
    public ResponseEntity<SpellingCorrectionDto> correctSpelling(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "maxDistance", defaultValue = "3") int maxDistance,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        SpellingCorrectionDto response = commandSearchService.correctSpelling(query, maxDistance, limit);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/commands/random - Return random command
     */
    @GetMapping("/commands/random")
    public ResponseEntity<Command> getRandomCommand() {
        Optional<Command> cmd = commandService.getRandomCommand();
        return cmd.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/commands/category/{category} - Return commands by category
     */
    @GetMapping("/commands/category/{category}")
    public ResponseEntity<List<Command>> getCommandsByCategory(@PathVariable("category") String category) {
        List<Command> commands = commandService.getCommandsByCategory(category);
        return ResponseEntity.ok(commands);
    }

    /**
     * GET /api/commands/{command} - Fast HashMap O(1) command lookup
     */
    @GetMapping("/commands/{command}")
    public ResponseEntity<?> getCommandByName(@PathVariable("command") String commandName) {
        Optional<Command> cmd = commandService.getCommandByName(commandName);
        if (cmd.isPresent()) {
            return ResponseEntity.ok(cmd.get());
        }
        return ResponseEntity.status(404).body(Map.of(
                "error", "Command Not Found",
                "message", "No command found matching '" + commandName + "'. Try spelling correction endpoint.",
                "command", commandName
        ));
    }

    /**
     * GET /api/stats - System statistics and category breakdown
     */
    @GetMapping("/stats")
    public ResponseEntity<SystemStatsDto> getSystemStats() {
        return ResponseEntity.ok(commandService.getSystemStats());
    }
}
