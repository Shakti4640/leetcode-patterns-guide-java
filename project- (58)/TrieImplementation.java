import java.util.ArrayList;
import java.util.List;

public class TrieImplementation {

    // ─────────────────────────────────────────────────────────────
    // TRIE NODE — One node per character position
    // ─────────────────────────────────────────────────────────────
    static class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;

        // Extended fields for advanced operations
        int wordCount;      // how many words end here
        int prefixCount;    // how many words pass through here

        TrieNode() {
            children = new TrieNode[26];
            isEndOfWord = false;
            wordCount = 0;
            prefixCount = 0;
        }

        // Check if this node has any children
        boolean hasChildren() {
            for (TrieNode child : children) {
                if (child != null) return true;
            }
            return false;
        }

        // Count non-null children
        int childCount() {
            int count = 0;
            for (TrieNode child : children) {
                if (child != null) count++;
            }
            return count;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TRIE — Core implementation with basic + extended operations
    // ─────────────────────────────────────────────────────────────
    static class Trie {
        TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        // ── INSERT: Add a word to the Trie ──
        // Time: O(L) where L = word length
        void insert(String word) {
            TrieNode node = root;

            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';

                // Create child node if it doesn't exist
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }

                // Move to child
                node = node.children[index];

                // Track how many words pass through this node
                node.prefixCount++;
            }

            // Mark end of word
            node.isEndOfWord = true;
            node.wordCount++;
        }

        // ── SEARCH: Check if exact word exists ──
        // Time: O(L)
        boolean search(String word) {
            TrieNode node = findNode(word);
            // Node must exist AND be marked as end of word
            return node != null && node.isEndOfWord;
        }

        // ── STARTS WITH: Check if any word has this prefix ──
        // Time: O(L)
        boolean startsWith(String prefix) {
            // Node just needs to exist — don't check isEndOfWord
            return findNode(prefix) != null;
        }

        // ── HELPER: Traverse Trie to find node for given string ──
        // Returns null if path doesn't exist
        // Shared by search() and startsWith() — DRY principle
        private TrieNode findNode(String str) {
            TrieNode node = root;

            for (int i = 0; i < str.length(); i++) {
                int index = str.charAt(i) - 'a';

                if (node.children[index] == null) {
                    return null;  // Path doesn't exist
                }

                node = node.children[index];
            }

            return node;  // Reached end of string — node exists
        }

        // ── COUNT WORDS WITH PREFIX ──
        // Time: O(L)
        int countWordsWithPrefix(String prefix) {
            TrieNode node = findNode(prefix);
            return node == null ? 0 : node.prefixCount;
        }

        // ── COUNT EXACT WORD OCCURRENCES ──
        // Time: O(L)
        int countExactWord(String word) {
            TrieNode node = findNode(word);
            return node == null ? 0 : node.wordCount;
        }

        // ── DELETE: Remove a word from the Trie ──
        // Time: O(L)
        boolean delete(String word) {
            if (!search(word)) return false;  // Word doesn't exist

            TrieNode node = root;

            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                node = node.children[index];
                node.prefixCount--;
            }

            node.isEndOfWord = false;
            node.wordCount--;
            return true;
            // Note: This is LAZY deletion — nodes remain even if unused
            // Full deletion would prune empty branches (more complex)
        }

        // ── GET ALL WORDS: Collect all words in the Trie ──
        // Time: O(total characters across all words)
        List<String> getAllWords() {
            List<String> words = new ArrayList<>();
            collectWords(root, new StringBuilder(), words);
            return words;
        }

        private void collectWords(TrieNode node, StringBuilder path,
                                  List<String> words) {
            if (node == null) return;

            if (node.isEndOfWord) {
                words.add(path.toString());
            }

            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    path.append((char) ('a' + i));
                    collectWords(node.children[i], path, words);
                    path.deleteCharAt(path.length() - 1);  // backtrack
                }
            }
        }

        // ── AUTOCOMPLETE: Get all words starting with prefix ──
        // Time: O(L + K) where K = total chars in matching words
        List<String> autocomplete(String prefix) {
            List<String> words = new ArrayList<>();
            TrieNode node = findNode(prefix);

            if (node == null) return words;  // No words with this prefix

            collectWords(node, new StringBuilder(prefix), words);
            return words;
        }

        // ── WILDCARD SEARCH: '.' matches any single character ──
        // Time: O(26^dots × L) worst case
        boolean searchWithWildcard(String word) {
            return wildcardHelper(root, word, 0);
        }

        private boolean wildcardHelper(TrieNode node, String word, int index) {
            if (node == null) return false;

            if (index == word.length()) {
                return node.isEndOfWord;
            }

            char c = word.charAt(index);

            if (c == '.') {
                // Try all 26 children
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null) {
                        if (wildcardHelper(node.children[i], word, index + 1)) {
                            return true;
                        }
                    }
                }
                return false;
            } else {
                int idx = c - 'a';
                return wildcardHelper(node.children[idx], word, index + 1);
            }
        }

        // ── LONGEST COMMON PREFIX: Among all words in the Trie ──
        // Time: O(L) where L = length of longest common prefix
        String longestCommonPrefix() {
            StringBuilder prefix = new StringBuilder();
            TrieNode node = root;

            // Walk down as long as there's exactly ONE child
            // and current node is NOT end of word
            while (node != null && node.childCount() == 1 && !node.isEndOfWord) {
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null) {
                        prefix.append((char) ('a' + i));
                        node = node.children[i];
                        break;
                    }
                }
            }

            return prefix.toString();
        }

        // ── COUNT TOTAL NODES ──
        int countNodes() {
            return countNodesHelper(root);
        }

        private int countNodesHelper(TrieNode node) {
            if (node == null) return 0;
            int count = 1;
            for (TrieNode child : node.children) {
                count += countNodesHelper(child);
            }
            return count;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TRIE VISUALIZER — ASCII tree representation
    // ─────────────────────────────────────────────────────────────
    static void visualizeTrie(Trie trie) {
        System.out.println("  Trie Structure:");
        visualizeNode(trie.root, "", true, "ROOT");
    }

    static void visualizeNode(TrieNode node, String indent,
                              boolean isLast, String label) {
        if (node == null) return;

        System.out.print(indent);
        System.out.print(isLast ? "└── " : "├── ");
        System.out.print(label);
        if (node.isEndOfWord) System.out.print(" ●");
        if (node.prefixCount > 0) System.out.print(" [" + node.prefixCount + "]");
        System.out.println();

        String childIndent = indent + (isLast ? "    " : "│   ");

        // Collect non-null children
        List<int[]> childrenList = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                childrenList.add(new int[]{i});
            }
        }

        for (int j = 0; j < childrenList.size(); j++) {
            int idx = childrenList.get(j)[0];
            char c = (char) ('a' + idx);
            boolean last = (j == childrenList.size() - 1);
            visualizeNode(node.children[idx], childIndent, last,
                String.valueOf(c));
        }
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE FUNCTION — Show operations step by step
    // ─────────────────────────────────────────────────────────────
    static void traceInsert(Trie trie, String word) {
        System.out.println("  insert(\"" + word + "\"):");
        TrieNode node = trie.root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            boolean created = (node.children[index] == null);

            if (created) {
                node.children[index] = new TrieNode();
            }

            node = node.children[index];
            node.prefixCount++;

            String pathSoFar = word.substring(0, i + 1);
            System.out.println("    '" + c + "' (index " + index + ")"
                + (created ? " → CREATE new node" : " → EXISTS")
                + " → path: \"" + pathSoFar + "\"");
        }

        node.isEndOfWord = true;
        node.wordCount++;
        System.out.println("    → Mark END OF WORD at \"" + word + "\" ✓");
    }

    static void traceSearch(Trie trie, String word) {
        System.out.print("  search(\"" + word + "\"): ");
        TrieNode node = trie.root;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';

            if (node.children[index] == null) {
                System.out.println("'" + c + "' → NOT FOUND → false");
                return;
            }

            node = node.children[index];
        }

        if (node.isEndOfWord) {
            System.out.println("path exists + isEndOfWord=true → true ✓");
        } else {
            System.out.println("path exists but isEndOfWord=false → false"
                + " (only a prefix, not a complete word)");
        }
    }

    static void traceStartsWith(Trie trie, String prefix) {
        System.out.print("  startsWith(\"" + prefix + "\"): ");
        TrieNode node = trie.root;

        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            int index = c - 'a';

            if (node.children[index] == null) {
                System.out.println("'" + c + "' → NOT FOUND → false");
                return;
            }

            node = node.children[index];
        }

        System.out.println("path exists → true ✓");
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 58: Implement Trie (Prefix Tree)            ║");
        System.out.println("║  Pattern: Node-per-Character → Insert/Search/Prefix  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic Operations ──
        System.out.println("═══ TEST 1: Basic Insert/Search/StartsWith ═══");
        Trie trie1 = new Trie();

        traceInsert(trie1, "apple");
        System.out.println();
        traceInsert(trie1, "app");
        System.out.println();
        traceInsert(trie1, "ape");
        System.out.println();
        traceInsert(trie1, "bat");
        System.out.println();
        traceInsert(trie1, "ball");
        System.out.println();

        visualizeTrie(trie1);
        System.out.println();

        traceSearch(trie1, "apple");
        traceSearch(trie1, "app");
        traceSearch(trie1, "ap");
        traceSearch(trie1, "bat");
        traceSearch(trie1, "bad");
        traceSearch(trie1, "xyz");
        System.out.println();

        traceStartsWith(trie1, "app");
        traceStartsWith(trie1, "ap");
        traceStartsWith(trie1, "ba");
        traceStartsWith(trie1, "c");
        traceStartsWith(trie1, "appl");
        System.out.println();

        // ── TEST 2: All Words and Autocomplete ──
        System.out.println("═══ TEST 2: All Words & Autocomplete ═══");
        System.out.println("  All words: " + trie1.getAllWords());
        System.out.println("  Autocomplete 'ap': " + trie1.autocomplete("ap"));
        System.out.println("  Autocomplete 'ba': " + trie1.autocomplete("ba"));
        System.out.println("  Autocomplete 'c':  " + trie1.autocomplete("c"));
        System.out.println("  Autocomplete '':   " + trie1.autocomplete(""));
        System.out.println();

        // ── TEST 3: Count Operations ──
        System.out.println("═══ TEST 3: Count Operations ═══");
        System.out.println("  Words with prefix 'ap':  " + trie1.countWordsWithPrefix("ap"));
        System.out.println("  Words with prefix 'app': " + trie1.countWordsWithPrefix("app"));
        System.out.println("  Words with prefix 'ba':  " + trie1.countWordsWithPrefix("ba"));
        System.out.println("  Words with prefix 'z':   " + trie1.countWordsWithPrefix("z"));
        System.out.println("  Total nodes: " + trie1.countNodes());
        System.out.println();

        // ── TEST 4: Wildcard Search ──
        System.out.println("═══ TEST 4: Wildcard Search (. = any char) ═══");
        Trie trie4 = new Trie();
        trie4.insert("bad");
        trie4.insert("dad");
        trie4.insert("mad");
        trie4.insert("bat");
        trie4.insert("pad");

        System.out.println("  Words: " + trie4.getAllWords());
        System.out.println("  search('.ad'): " + trie4.searchWithWildcard(".ad"));
        System.out.println("  search('b..'): " + trie4.searchWithWildcard("b.."));
        System.out.println("  search('b.d'): " + trie4.searchWithWildcard("b.d"));
        System.out.println("  search('b.t'): " + trie4.searchWithWildcard("b.t"));
        System.out.println("  search('...'): " + trie4.searchWithWildcard("..."));
        System.out.println("  search('....'): " + trie4.searchWithWildcard("...."));
        System.out.println("  search('.at'): " + trie4.searchWithWildcard(".at"));
        System.out.println();

        // ── TEST 5: Delete Operation ──
        System.out.println("═══ TEST 5: Delete Operation ═══");
        Trie trie5 = new Trie();
        trie5.insert("apple");
        trie5.insert("app");
        trie5.insert("ape");

        System.out.println("  Before delete:");
        System.out.println("    All words: " + trie5.getAllWords());
        System.out.println("    search('app'): " + trie5.search("app"));

        trie5.delete("app");
        System.out.println("  After delete('app'):");
        System.out.println("    All words: " + trie5.getAllWords());
        System.out.println("    search('app'): " + trie5.search("app"));
        System.out.println("    startsWith('app'): " + trie5.startsWith("app"));
        System.out.println("    search('apple'): " + trie5.search("apple"));
        System.out.println();

        // ── TEST 6: Longest Common Prefix ──
        System.out.println("═══ TEST 6: Longest Common Prefix ═══");

        Trie trie6a = new Trie();
        trie6a.insert("flower");
        trie6a.insert("flow");
        trie6a.insert("flight");
        System.out.println("  Words: " + trie6a.getAllWords());
        System.out.println("  LCP: \"" + trie6a.longestCommonPrefix() + "\"");

        Trie trie6b = new Trie();
        trie6b.insert("dog");
        trie6b.insert("racecar");
        trie6b.insert("car");
        System.out.println("  Words: " + trie6b.getAllWords());
        System.out.println("  LCP: \"" + trie6b.longestCommonPrefix() + "\"");

        Trie trie6c = new Trie();
        trie6c.insert("interview");
        trie6c.insert("internet");
        trie6c.insert("internal");
        trie6c.insert("inter");
        System.out.println("  Words: " + trie6c.getAllWords());
        System.out.println("  LCP: \"" + trie6c.longestCommonPrefix() + "\"");
        System.out.println();

        // ── TEST 7: Duplicate Inserts ──
        System.out.println("═══ TEST 7: Duplicate Inserts ═══");
        Trie trie7 = new Trie();
        trie7.insert("hello");
        trie7.insert("hello");
        trie7.insert("hello");
        System.out.println("  Inserted 'hello' 3 times");
        System.out.println("  search('hello'): " + trie7.search("hello"));
        System.out.println("  countExact('hello'): " + trie7.countExactWord("hello"));
        System.out.println("  countPrefix('hel'): " + trie7.countWordsWithPrefix("hel"));
        System.out.println();

        // ── TEST 8: Edge Cases ──
        System.out.println("═══ TEST 8: Edge Cases ═══");
        Trie trie8 = new Trie();

        // Single character words
        trie8.insert("a");
        trie8.insert("b");
        System.out.println("  After insert 'a', 'b':");
        System.out.println("    search('a'): " + trie8.search("a"));
        System.out.println("    search('b'): " + trie8.search("b"));
        System.out.println("    search('c'): " + trie8.search("c"));
        System.out.println("    startsWith('a'): " + trie8.startsWith("a"));

        // Very long word
        String longWord = "abcdefghijklmnopqrstuvwxyz";
        trie8.insert(longWord);
        System.out.println("    search(26-char word): " + trie8.search(longWord));
        System.out.println("    startsWith('abcdef'): " + trie8.startsWith("abcdef"));
        System.out.println("    Total nodes: " + trie8.countNodes());
        System.out.println();

        // ── TEST 9: Performance ──
        System.out.println("═══ TEST 9: Performance Test ═══");
        Trie perfTrie = new Trie();
        java.util.Set<String> perfSet = new java.util.HashSet<>();
        java.util.Random rng = new java.util.Random(42);

        int wordCount = 100000;
        String[] words = new String[wordCount];

        // Generate random words
        for (int i = 0; i < wordCount; i++) {
            int len = rng.nextInt(10) + 3;  // 3-12 chars
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + rng.nextInt(26)));
            }
            words[i] = sb.toString();
        }

        // Insert into Trie
        long start = System.nanoTime();
        for (String w : words) perfTrie.insert(w);
        long trieInsertTime = System.nanoTime() - start;

        // Insert into HashSet
        start = System.nanoTime();
        for (String w : words) perfSet.add(w);
        long setInsertTime = System.nanoTime() - start;

        System.out.println("  Insert " + wordCount + " words:");
        System.out.println("    Trie:    " + (trieInsertTime / 1_000_000) + " ms");
        System.out.println("    HashSet: " + (setInsertTime / 1_000_000) + " ms");

        // Search performance
        start = System.nanoTime();
        for (String w : words) perfTrie.search(w);
        long trieSearchTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (String w : words) perfSet.contains(w);
        long setSearchTime = System.nanoTime() - start;

        System.out.println("  Search " + wordCount + " words:");
        System.out.println("    Trie:    " + (trieSearchTime / 1_000_000) + " ms");
        System.out.println("    HashSet: " + (setSearchTime / 1_000_000) + " ms");

        // Prefix performance (Trie advantage)
        start = System.nanoTime();
        for (int i = 0; i < wordCount; i++) {
            String prefix = words[i].substring(0, Math.min(3, words[i].length()));
            perfTrie.startsWith(prefix);
        }
        long triePrefixTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < wordCount; i++) {
            String prefix = words[i].substring(0, Math.min(3, words[i].length()));
            boolean found = false;
            for (String w : perfSet) {
                if (w.startsWith(prefix)) {
                    found = true;
                    break;
                }
            }
        }
        long setPrefixTime = System.nanoTime() - start;

        System.out.println("  StartsWith " + wordCount + " prefixes:");
        System.out.println("    Trie:    " + (triePrefixTime / 1_000_000) + " ms");
        System.out.println("    HashSet: " + (setPrefixTime / 1_000_000) + " ms ← MUCH SLOWER");
        System.out.println("    Speedup: ~" + (setPrefixTime / Math.max(triePrefixTime, 1)) + "x");
        System.out.println();

        System.out.println("  Trie stats:");
        System.out.println("    Total nodes: " + perfTrie.countNodes());
        System.out.println("    Total words: " + perfTrie.getAllWords().size());
    }
}