package com.dsa.linuxintelligence.repository;

import com.dsa.linuxintelligence.model.CommandDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandMongoRepository extends MongoRepository<CommandDocument, String> {

    Optional<CommandDocument> findByCommandIgnoreCase(String command);

    List<CommandDocument> findByCategoryIgnoreCase(String category);

    List<CommandDocument> findByCommandStartingWithIgnoreCase(String prefix);

    @Query("{ 'command': { $regex: ?0, $options: 'i' } }")
    List<CommandDocument> searchByRegexPattern(String pattern);
}
