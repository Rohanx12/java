 //Problem-2:
 //Build a word count application, where the constraints are that you have 10 MB RAM and 1 GB text file. You should be a
 //ble to efficiently parse the text file and output the words and counts in a sorted way. Write a program to read a
 // large file, and emit the sorted words along with the count. Try to implement fuzzy search as well (fix the spelling
 // issues) Algorithm should have Log N complexity.

import java.io.*;
import java.util.*;

class WordCount {
    String word;
    int count;

    public WordCount(String word) {
        this.word = word;
        this.count = 1;
    }

    public String getWord() {
        return word;
    }
}


class Node {
    WordCount wordCount;
    Node left, right;
    int size;

    public Node(WordCount wordCount) {
        this.wordCount = wordCount;
        this.size = 1;
    }
}

public class WordCountApp {
    private static final int MAX_MEMORY = 10 * 1024 * 1024; // 10 MB
    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB

    private static Map<String, WordCount> wordCountMap = new HashMap<>();
    private static Node root;

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src//large_file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Build a Red-Black tree from the wordCountMap
        buildTree();


        fuzzySearch("applicatoon");

        // Output sorted words with count
        printSortedWords(root);
    }

    private static void processLine(String line) {
        String[] words = line.split("\\s+");
        for (String word : words) {
            String cleanedWord = cleanWord(word);
            if (cleanedWord.length() > 0) {
                WordCount wordCount = wordCountMap.getOrDefault(cleanedWord, new WordCount(cleanedWord));
                wordCount.count++;
                wordCountMap.put(cleanedWord, wordCount);
            }
        }
    }

    private static String cleanWord(String word) {
        return word.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    private static void buildTree() {
        List<WordCount> wordCounts = new ArrayList<>(wordCountMap.values());
        Collections.sort(wordCounts, Comparator.comparing(WordCount::getWord));

        for (WordCount wordCount : wordCounts) {
            root = insert(root, wordCount);
        }
    }

    private static Node insert(Node node, WordCount wordCount) {
        if (node == null) {
            return new Node(wordCount);
        }

        int cmp = wordCount.word.compareTo(node.wordCount.word);
        if (cmp < 0) {
            node.left = insert(node.left, wordCount);
        } else if (cmp > 0) {
            node.right = insert(node.right, wordCount);
        } else {
            node.wordCount.count += wordCount.count;
        }

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    private static int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private static void printSortedWords(Node node) {
        if (node != null) {
            printSortedWords(node.left);
            System.out.println(node.wordCount.word + ": " + node.wordCount.count);
            printSortedWords(node.right);
        }
    }

    private static void fuzzySearch(String word) {
        Node result = search(root, word);
        if (result != null) {
            System.out.println("Fuzzy search result for '" + word + "': " + result.wordCount.word + ": " + result.wordCount.count);
        } else {
            System.out.println("No fuzzy search result found for '" + word + "'");
        }
    }

    private static Node search(Node node, String word) {
        if (node == null) {
            return null;
        }

        int cmp = word.compareTo(node.wordCount.word);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return search(node.left, word);
        } else {
            return search(node.right, word);
        }
    }
}
