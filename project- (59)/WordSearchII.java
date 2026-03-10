import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordSearchII {

    // ─────────────────────────────────────────────────────────────
    // TRIE NODE — Stores word at leaf for instant retrieval
    // ─────────────────────────────────────────────────────────────
    static class TrieNode {
        TrieNode[] children;
        String word;  // non-null only at end-of-word nodes

        TrieNode() {
            children = new TrieNode[26];
            word = null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // BUILD TRIE — Insert all words from the word list
    // ─────────────────────────────────────────────────────────────
    static TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();

        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.word = word;  // Store complete word at leaf
        }

        return root;
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN SOLUTION — Trie-guided DFS with pruning
    // ─────────────────────────────────────────────────────────────
    static int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();

        if (board == null || board.length == 0 || words == null || words.length == 0) {
            return result;
        }

        // Step 1: Build Trie from word list
        TrieNode root = buildTrie(words);

        int rows = board.length;
        int cols = board[0].length;

        // Step 2: Start DFS from every cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = board[i][j] - 'a';
                // Only start DFS if Trie has this character at root level
                if (root.children[index] != null) {
                    dfs(board, i, j, root, result);
                }
            }
        }

        return result;
    }

    static void dfs(char[][] board, int i, int j, TrieNode node,
                    List<String> result) {
        // Bounds check
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        char c = board[i][j];

        // Already visited (marked '#') or no Trie child for this character
        if (c == '#' || node.children[c - 'a'] == null) {
            return;
        }

        // Move to Trie child
        node = node.children[c - 'a'];

        // Check if we found a word
        if (node.word != null) {
            result.add(node.word);
            node.word = null;  // Prevent duplicate findings
        }

        // Mark cell as visited
        board[i][j] = '#';

        // Explore all 4 directions
        for (int[] dir : DIRS) {
            dfs(board, i + dir[0], j + dir[1], node, result);
        }

        // Backtrack — restore cell
        board[i][j] = c;

        // OPTIMIZATION: Prune Trie leaf nodes
        // If this node has no children and no word → it's dead
        // Remove it to speed up future DFS
        if (isLeafNode(node)) {
            // We can't directly remove from parent here without parent reference
            // Instead, we handle pruning via the check above (null children)
            // For full pruning, see the optimized version below
        }
    }

    static boolean isLeafNode(TrieNode node) {
        for (TrieNode child : node.children) {
            if (child != null) return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────
    // OPTIMIZED SOLUTION — Full Trie pruning with parent cleanup
    // ─────────────────────────────────────────────────────────────
    public static List<String> findWordsOptimized(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();

        if (board == null || board.length == 0 || words == null || words.length == 0) {
            return result;
        }

        TrieNode root = buildTrie(words);
        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfsOptimized(board, i, j, root, result);
            }
        }

        return result;
    }

    static void dfsOptimized(char[][] board, int i, int j,
                              TrieNode parent, List<String> result) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        char c = board[i][j];
        if (c == '#') return;

        int index = c - 'a';
        TrieNode node = parent.children[index];
        if (node == null) return;

        // Found a word
        if (node.word != null) {
            result.add(node.word);
            node.word = null;
        }

        // Mark visited
        board[i][j] = '#';

        // Explore all 4 directions
        for (int[] dir : DIRS) {
            dfsOptimized(board, i + dir[0], j + dir[1], node, result);
        }

        // Restore cell
        board[i][j] = c;

        // PRUNE: If node is now a dead leaf, remove it from parent
        // This is the KEY optimization
        if (isLeafNode(node) && node.word == null) {
            parent.children[index] = null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // NAIVE SOLUTION — Per-word DFS (for comparison)
    // ─────────────────────────────────────────────────────────────
    public static List<String> findWordsNaive(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        Set<String> found = new HashSet<>();

        for (String word : words) {
            if (found.contains(word)) continue;
            if (existsOnBoard(board, word)) {
                result.add(word);
                found.add(word);
            }
        }

        return result;
    }

    static boolean existsOnBoard(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (dfsNaive(board, i, j, word, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static boolean dfsNaive(char[][] board, int i, int j,
                             String word, int pos) {
        if (pos == word.length()) return true;
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return false;
        }
        if (board[i][j] != word.charAt(pos)) return false;

        char temp = board[i][j];
        board[i][j] = '#';

        for (int[] dir : DIRS) {
            if (dfsNaive(board, i + dir[0], j + dir[1], word, pos + 1)) {
                board[i][j] = temp;
                return true;
            }
        }

        board[i][j] = temp;
        return false;
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE FUNCTION — Visualize Trie-guided DFS
    // ─────────────────────────────────────────────────────────────
    static int traceDepth = 0;

    public static List<String> findWordsWithTrace(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        TrieNode root = buildTrie(words);

        printBoard(board, "Initial Board");
        System.out.println("  Words to find: ");
        System.out.print("    [");
        for (int i = 0; i < words.length; i++) {
            System.out.print("\"" + words[i] + "\"");
            if (i < words.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = board[i][j] - 'a';
                if (root.children[index] != null) {
                    System.out.println("  Starting DFS from (" + i + "," + j
                        + ") = '" + board[i][j] + "'");
                    traceDepth = 0;
                    dfsTrace(board, i, j, root, result, "");
                    System.out.println();
                }
            }
        }

        return result;
    }

    static void dfsTrace(char[][] board, int i, int j,
                          TrieNode parent, List<String> result,
                          String pathSoFar) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        char c = board[i][j];
        if (c == '#') return;

        int index = c - 'a';
        TrieNode node = parent.children[index];
        if (node == null) {
            String indent = "    " + "  ".repeat(traceDepth);
            System.out.println(indent + "(" + i + "," + j + ")='" + c
                + "' → Trie has no child → PRUNE ✂️");
            return;
        }

        String newPath = pathSoFar + c;
        String indent = "    " + "  ".repeat(traceDepth);
        System.out.println(indent + "(" + i + "," + j + ")='" + c
            + "' → Trie match → path: \"" + newPath + "\"");

        if (node.word != null) {
            System.out.println(indent + "  ★ FOUND WORD: \"" + node.word + "\" ★");
            result.add(node.word);
            node.word = null;
        }

        board[i][j] = '#';
        traceDepth++;

        for (int[] dir : DIRS) {
            int ni = i + dir[0], nj = j + dir[1];
            if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length
                && board[ni][nj] != '#') {
                dfsTrace(board, ni, nj, node, result, newPath);
            }
        }

        traceDepth--;
        board[i][j] = c;

        if (isLeafNode(node) && node.word == null) {
            parent.children[index] = null;
            System.out.println(indent + "  (pruned '" + c + "' node from Trie)");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // BOARD PRINTER
    // ─────────────────────────────────────────────────────────────
    static void printBoard(char[][] board, String title) {
        System.out.println("  " + title + ":");
        int cols = board[0].length;
        System.out.print("    ┌");
        for (int j = 0; j < cols; j++) {
            System.out.print("───");
            System.out.print(j < cols - 1 ? "┬" : "┐");
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.print("    │");
            for (int j = 0; j < cols; j++) {
                System.out.print(" " + board[i][j] + " │");
            }
            System.out.println();

            if (i < board.length - 1) {
                System.out.print("    ├");
                for (int j = 0; j < cols; j++) {
                    System.out.print("───");
                    System.out.print(j < cols - 1 ? "┼" : "┤");
                }
                System.out.println();
            }
        }

        System.out.print("    └");
        for (int j = 0; j < cols; j++) {
            System.out.print("───");
            System.out.print(j < cols - 1 ? "┴" : "┘");
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 59: Word Search II                          ║");
        System.out.println("║  Pattern: Trie-Guided DFS → Backtracking + Pruning   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example with Trace ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        char[][] board1 = {
            {'o', 'a', 'a', 'n'},
            {'e', 't', 'a', 'e'},
            {'i', 'h', 'k', 'r'},
            {'i', 'f', 'l', 'v'}
        };
        String[] words1 = {"oath", "pea", "eat", "rain"};
        List<String> result1 = findWordsWithTrace(board1, words1);
        System.out.println("  Result: " + result1);
        System.out.println();

        // ── TEST 2: Small Board Trace ──
        System.out.println("═══ TEST 2: Small Board ═══");
        char[][] board2 = {
            {'a', 'b'},
            {'c', 'd'}
        };
        String[] words2 = {"ab", "cb", "ad", "bd", "ac", "ca",
                           "da", "bc", "db", "adcb", "dabc", "abb", "acb"};
        List<String> result2 = findWordsOptimized(board2, words2);
        printBoard(board2, "Board");
        System.out.println("  Words to search: ");
        System.out.print("    ");
        for (String w : words2) System.out.print("\"" + w + "\" ");
        System.out.println();
        System.out.println("  Found: " + result2);
        System.out.println();

        // ── TEST 3: No Words Found ──
        System.out.println("═══ TEST 3: No Words Found ═══");
        char[][] board3 = {
            {'a', 'b'},
            {'c', 'd'}
        };
        String[] words3 = {"xyz", "pqr", "hello"};
        List<String> result3 = findWordsOptimized(board3, words3);
        System.out.println("  Found: " + result3 + " (expected: [])");
        System.out.println();

        // ── TEST 4: All Words Found ──
        System.out.println("═══ TEST 4: Single Cell Board ═══");
        char[][] board4 = {{'a'}};
        String[] words4 = {"a"};
        List<String> result4 = findWordsOptimized(board4, words4);
        System.out.println("  Board: [['a']], Words: [\"a\"]");
        System.out.println("  Found: " + result4);
        System.out.println();

        // ── TEST 5: Shared Prefixes ──
        System.out.println("═══ TEST 5: Shared Prefixes ═══");
        char[][] board5 = {
            {'o', 'a', 't'},
            {'e', 't', 'h'},
            {'i', 'h', 'k'}
        };
        String[] words5 = {"oat", "oath", "oaths", "oa", "o"};
        List<String> result5 = findWordsWithTrace(board5, words5);
        System.out.println("  Result: " + result5);
        System.out.println();

        // ── TEST 6: Verification — Optimized vs Naive ──
        System.out.println("═══ TEST 6: Verification — Optimized vs Naive ═══");
        char[][][] boards = {
            {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
            },
            {
                {'a', 'b'},
                {'c', 'd'}
            },
            {
                {'a'}
            },
            {
                {'a', 'a'}
            },
            {
                {'a', 'b', 'c'},
                {'a', 'e', 'd'},
                {'a', 'f', 'g'}
            }
        };

        String[][] wordLists = {
            {"oath", "pea", "eat", "rain"},
            {"abcb", "ab", "cb", "ad", "abdc"},
            {"a", "b"},
            {"a", "aa", "aaa"},
            {"abcdefg", "gfedcba", "abcde", "bcd", "aefa"}
        };

        boolean allPassed = true;
        for (int t = 0; t < boards.length; t++) {
            // Deep copy boards since DFS modifies them
            char[][] boardCopy1 = deepCopy(boards[t]);
            char[][] boardCopy2 = deepCopy(boards[t]);

            List<String> optimized = findWordsOptimized(boardCopy1, wordLists[t]);
            List<String> naive = findWordsNaive(boardCopy2, wordLists[t]);

            Set<String> optSet = new HashSet<>(optimized);
            Set<String> naiveSet = new HashSet<>(naive);
            boolean match = optSet.equals(naiveSet);

            System.out.println("  Test " + (t + 1) + ":"
                + " Optimized=" + optimized
                + " Naive=" + naive
                + (match ? " ✓ MATCH" : " ✗ MISMATCH"));

            if (!match) allPassed = false;
        }
        System.out.println(allPassed
            ? "\n  ✅ ALL TESTS PASSED"
            : "\n  ❌ SOME TESTS FAILED");
        System.out.println();

        // ── TEST 7: Performance Comparison ──
        System.out.println("═══ TEST 7: Performance Comparison ═══");
        int boardSize = 10;
        char[][] perfBoard = new char[boardSize][boardSize];
        java.util.Random rng = new java.util.Random(42);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                perfBoard[i][j] = (char) ('a' + rng.nextInt(5));  // small alphabet
            }
        }

        // Generate words from actual board paths + some random words
        List<String> perfWordList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int len = rng.nextInt(5) + 2;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + rng.nextInt(5)));
            }
            perfWordList.add(sb.toString());
        }
        String[] perfWords = perfWordList.toArray(new String[0]);

        System.out.println("  Board: " + boardSize + "×" + boardSize
            + ", Words: " + perfWords.length
            + ", Alphabet: a-e");

        // Optimized (Trie)
        char[][] copy1 = deepCopy(perfBoard);
        long start = System.nanoTime();
        List<String> optResult = findWordsOptimized(copy1, perfWords);
        long optTime = System.nanoTime() - start;

        // Naive (per-word DFS)
        char[][] copy2 = deepCopy(perfBoard);
        start = System.nanoTime();
        List<String> naiveResult = findWordsNaive(copy2, perfWords);
        long naiveTime = System.nanoTime() - start;

        System.out.println("  Trie-guided: " + optResult.size()
            + " words found → " + (optTime / 1_000_000) + " ms");
        System.out.println("  Naive:       " + naiveResult.size()
            + " words found → " + (naiveTime / 1_000_000) + " ms");
        System.out.println("  Speedup: ~"
            + (naiveTime / Math.max(optTime, 1)) + "x");
        System.out.println();

        // ── TEST 8: Stress Test with Large Word List ──
        System.out.println("═══ TEST 8: Stress Test ═══");
        int stressWordCount = 5000;
        List<String> stressWords = new ArrayList<>();
        for (int i = 0; i < stressWordCount; i++) {
            int len = rng.nextInt(8) + 2;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + rng.nextInt(5)));
            }
            stressWords.add(sb.toString());
        }

        char[][] stressBoard = new char[12][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                stressBoard[i][j] = (char) ('a' + rng.nextInt(5));
            }
        }

        System.out.println("  Board: 12×12, Words: " + stressWordCount);
        char[][] stressCopy = deepCopy(stressBoard);
        start = System.nanoTime();
        List<String> stressResult = findWordsOptimized(
            stressCopy, stressWords.toArray(new String[0]));
        long stressTime = System.nanoTime() - start;
        System.out.println("  Found: " + stressResult.size() + " words"
            + " → " + (stressTime / 1_000_000) + " ms");
    }

    static char[][] deepCopy(char[][] board) {
        char[][] copy = new char[board.length][];
        for (int i = 0; i < board.length; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }
}