package com.dsa.linuxintelligence.config;

import com.dsa.linuxintelligence.model.CommandDocument;
import com.dsa.linuxintelligence.repository.CommandMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MongoSeeder.class);

    private final CommandMongoRepository mongoRepository;

    @Value("${mongodb.seeder.enabled:true}")
    private boolean seederEnabled;

    public MongoSeeder(CommandMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void run(String... args) {
        if (!seederEnabled) {
            logger.info("MongoDB Seeder is disabled by configuration.");
            return;
        }

        try {
            long existingCount = mongoRepository.count();
            if (existingCount > 0) {
                logger.info("MongoDB Atlas already contains {} commands. Skipping auto-seeding.", existingCount);
                return;
            }

            logger.info("MongoDB Atlas collection is empty. Starting command data import...");
            List<CommandDocument> docsToInsert = loadCommandsFromCsv();

            if (!docsToInsert.isEmpty()) {
                mongoRepository.saveAll(docsToInsert);
                logger.info("Successfully seeded {} Linux command documents into MongoDB Atlas!", docsToInsert.size());
            } else {
                logger.warn("No command entries found in CSV data source.");
            }
        } catch (Exception e) {
            logger.warn("Could not auto-seed MongoDB Atlas (Connection string may be default/unreachable): {}", e.getMessage());
        }
    }

    private List<CommandDocument> loadCommandsFromCsv() {
        List<CommandDocument> list = new ArrayList<>();
        File csvFile = new File("../database/linux_commands.csv");

        try (BufferedReader reader = csvFile.exists() ?
                new BufferedReader(new FileReader(csvFile)) :
                new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/linux_commands.csv")))) {

            String header = reader.readLine(); // skip CSV header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = parseCsvLine(line);
                if (tokens.length >= 10) {
                    CommandDocument doc = new CommandDocument();
                    doc.setCommand(clean(tokens[0]));
                    doc.setCategory(clean(tokens[1]));
                    doc.setShortDefinition(clean(tokens[2]));
                    doc.setDescription(clean(tokens[3]));
                    doc.setSyntax(clean(tokens[4]));
                    doc.setExample(clean(tokens[5]));
                    doc.setExampleExplanation(clean(tokens[6]));
                    doc.setCommonOptions(clean(tokens[7]));
                    doc.setRelatedCommands(clean(tokens[8]));
                    doc.setSafetyLevel(clean(tokens[9]));
                    if (tokens.length >= 11) {
                        doc.setDistribution(clean(tokens[10]));
                    }
                    list.add(doc);
                }
            }
        } catch (Exception e) {
            logger.error("Error reading command CSV file: {}", e.getMessage());
        }

        return list;
    }

    private String clean(String str) {
        if (str == null) return "";
        String s = str.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    sb.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}
