package DSA3;

import java.io.*;
import java.util.*;

// Stores complete information about one Linux command
class CommandDetails {
    String name;
    String description;
    String syntax;
    String example;

    CommandDetails(String name, String description,
                   String syntax, String example) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.example = example;
    }
}

// Trie Node
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord;
}

public class AdvancedCommandRetrieval {

    // Hash Table for storing command details
    static HashMap<String, CommandDetails> commandTable = new HashMap<>();

    // Root of Trie
    static TrieNode root = new TrieNode();


    // =========================================================
    // 1. LOAD COMMANDS FROM FILE
    // =========================================================

    static void loadCommands(String fileName) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(fileName));

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split("\\|");

                if (parts.length == 4) {

                    String name = parts[0].trim();
                    String description = parts[1].trim();
                    String syntax = parts[2].trim();
                    String example = parts[3].trim();

                    CommandDetails command =
                            new CommandDetails(
                                    name,
                                    description,
                                    syntax,
                                    example
                            );

                    // Store command in HashMap
                    commandTable.put(name, command);

                    // Store command name in Trie
                    insertIntoTrie(name);
                }
            }

            br.close();

            System.out.println("Commands loaded successfully.");
            System.out.println("Total commands: "
                    + commandTable.size());

        } catch (FileNotFoundException e) {

            System.out.println("Error: File not found.");
            System.out.println("Make sure linux_commands.txt "
                    + "is in the correct folder.");

        } catch (IOException e) {

            System.out.println("Error while reading file.");
        }
    }


    // =========================================================
    // 2. INSERT COMMAND INTO TRIE
    // =========================================================

    static void insertIntoTrie(String word) {

        TrieNode current = root;

        for (char ch : word.toCharArray()) {

            int index = ch - 'a';

            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }

            current = current.children[index];
        }

        current.isEndOfWord = true;
    }


    // =========================================================
    // 3. FIND PREFIX NODE IN TRIE
    // =========================================================

    static TrieNode findPrefixNode(String prefix) {

        TrieNode current = root;

        for (char ch : prefix.toCharArray()) {

            int index = ch - 'a';

            if (current.children[index] == null) {
                return null;
            }

            current = current.children[index];
        }

        return current;
    }


    // =========================================================
    // 4. COLLECT WORDS FROM TRIE
    // =========================================================

    static void collectWords(TrieNode node,
                             String word,
                             ArrayList<String> results) {

        if (node.isEndOfWord) {
            results.add(word);
        }

        for (int i = 0; i < 26; i++) {

            if (node.children[i] != null) {

                char ch = (char) ('a' + i);

                collectWords(
                        node.children[i],
                        word + ch,
                        results
                );
            }
        }
    }


    // =========================================================
    // 5. AUTO COMPLETE USING TRIE
    // =========================================================

    static void autoComplete(String prefix) {

        prefix = prefix.toLowerCase();

        TrieNode node = findPrefixNode(prefix);

        if (node == null) {

            System.out.println("No commands found.");
            return;
        }

        ArrayList<String> results = new ArrayList<>();

        collectWords(node, prefix, results);

        System.out.println("\nAuto-complete suggestions:");

        for (String command : results) {
            System.out.println("- " + command);
        }
    }


    // =========================================================
    // 6. RABIN-KARP STRING SEARCH
    // =========================================================

    static boolean rabinKarp(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int n = text.length();
        int m = pattern.length();

        if (m > n)
            return false;

        int prime = 101;
        int base = 256;

        int patternHash = 0;
        int textHash = 0;
        int power = 1;

        // Calculate base^(m-1)
        for (int i = 0; i < m - 1; i++) {
            power = (power * base) % prime;
        }

        // Calculate initial hash
        for (int i = 0; i < m; i++) {

            patternHash =
                    (base * patternHash
                            + pattern.charAt(i)) % prime;

            textHash =
                    (base * textHash
                            + text.charAt(i)) % prime;
        }

        // Search pattern
        for (int i = 0; i <= n - m; i++) {

            if (patternHash == textHash) {

                boolean match = true;

                for (int j = 0; j < m; j++) {

                    if (text.charAt(i + j)
                            != pattern.charAt(j)) {

                        match = false;
                        break;
                    }
                }

                if (match)
                    return true;
            }

            // Calculate next window hash
            if (i < n - m) {

                textHash =
                        (base *
                                (textHash
                                        - text.charAt(i) * power)
                                + text.charAt(i + m))
                                % prime;

                if (textHash < 0)
                    textHash += prime;
            }
        }

        return false;
    }


    // =========================================================
    // 7. PATTERN SEARCH USING RABIN-KARP
    // =========================================================

    static void patternSearch(String pattern) {

        boolean found = false;

        System.out.println("\nPattern Search Results:");

        for (CommandDetails command : commandTable.values()) {

            boolean nameMatch =
                    rabinKarp(command.name, pattern);

            boolean descriptionMatch =
                    rabinKarp(command.description, pattern);

            if (nameMatch || descriptionMatch) {

                System.out.println("- " + command.name);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching commands found.");
        }
    }


    // =========================================================
    // 8. EDIT DISTANCE
    // =========================================================

    static int editDistance(String a, String b) {

        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        // First column
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // First row
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Fill DP table
        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                if (a.charAt(i - 1)
                        == b.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];

                    dp[i][j] =
                            1 + Math.min(
                                    insert,
                                    Math.min(delete, replace)
                            );
                }
            }
        }

        return dp[n][m];
    }


    // =========================================================
    // 9. SPELLING CORRECTION USING EDIT DISTANCE
    // =========================================================

    static void spellingCorrection(String input) {

        input = input.toLowerCase();

        String bestMatch = null;
        int minimumDistance = Integer.MAX_VALUE;

        for (String command : commandTable.keySet()) {

            int distance =
                    editDistance(input, command);

            if (distance < minimumDistance) {

                minimumDistance = distance;
                bestMatch = command;
            }
        }

        if (bestMatch != null) {

            System.out.println("\nDid you mean: "
                    + bestMatch + "?");

            System.out.println("Edit Distance: "
                    + minimumDistance);

            displayCommand(bestMatch);
        }
    }


    // =========================================================
    // 10. DISPLAY COMMAND DETAILS
    // =========================================================

    static void displayCommand(String commandName) {

        commandName = commandName.toLowerCase();

        CommandDetails command =
                commandTable.get(commandName);

        if (command == null) {

            System.out.println("Command not found.");
            return;
        }

        System.out.println("\n================================");
        System.out.println("Command: " + command.name);
        System.out.println("Description: "
                + command.description);
        System.out.println("Syntax: "
                + command.syntax);
        System.out.println("Example: "
                + command.example);
        System.out.println("================================");
    }


    // =========================================================
    // 11. DIRECT HASH TABLE RETRIEVAL
    // =========================================================

    static void retrieveCommand(String commandName) {

        commandName = commandName.toLowerCase();

        if (commandTable.containsKey(commandName)) {

            System.out.println(
                    "\nCommand found using Hash Table."
            );

            displayCommand(commandName);

        } else {

            System.out.println("Command not found.");
        }
    }


    // =========================================================
    // 12. DISPLAY MENU
    // =========================================================

    static void displayMenu() {

        System.out.println("\n========================================");
        System.out.println(" Advanced Command Retrieval System");
        System.out.println("========================================");

        System.out.println("1. Pattern Search");
        System.out.println("2. Auto Complete");
        System.out.println("3. Spelling Correction");
        System.out.println("4. Retrieve Command Details");
        System.out.println("5. Exit");

        System.out.print("\nEnter choice: ");
    }


    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Load commands from file
        loadCommands("linux_commands.txt");

        while (true) {

            displayMenu();

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print(
                            "Enter pattern to search: ");

                    String pattern = sc.nextLine();

                    patternSearch(pattern);

                    break;


                case 2:

                    System.out.print(
                            "Enter command prefix: ");

                    String prefix = sc.nextLine();

                    autoComplete(prefix);

                    break;


                case 3:

                    System.out.print(
                            "Enter command with spelling mistake: ");

                    String wrongCommand = sc.nextLine();

                    spellingCorrection(wrongCommand);

                    break;


                case 4:

                    System.out.print(
                            "Enter command name: ");

                    String command = sc.nextLine();

                    retrieveCommand(command);

                    break;


                case 5:

                    System.out.println(
                            "\nThank you for using the system.");

                    sc.close();

                    return;


                default:

                    System.out.println(
                            "Invalid choice. Try again.");
            }
        }
    }
}