package com.dsa.linuxintelligence.repository;

import com.dsa.linuxintelligence.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    Optional<Command> findByCommandIgnoreCase(String command);

    List<Command> findByCategoryIgnoreCase(String category);

    @Query("SELECT DISTINCT c.category FROM Command c ORDER BY c.category ASC")
    List<String> findAllCategories();

    @Query("SELECT c.category, COUNT(c) FROM Command c GROUP BY c.category ORDER BY COUNT(c) DESC")
    List<Object[]> countCommandsByCategory();

    @Query(value = "SELECT * FROM linux_commands ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Command> findRandomCommand();
}
