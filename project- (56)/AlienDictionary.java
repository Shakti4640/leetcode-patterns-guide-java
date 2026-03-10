import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AlienDictionary {

    // ─────────────────────────────────────────────────────────────
    // MAIN SOLUTION: Graph Construction + Kahn's BFS Topological Sort
    // ─────────────────────────────────────────────────────────────
    public static String alienOrder(String[] words) {
        if (words == null || words.length == 0) return "";

        // ── STEP 1: Collect all unique characters ──
        // Every character in every word is a node in our graph
        Map<Character, List<Character>> adjacencyList = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adjacencyList.putIfAbsent(c, new ArrayList<>());
                inDegree.putIfAbsent(c, 0);
            }
        }

        // ── STEP 2: Build graph from adjacent word pairs ──
        // Track existing edges to avoid duplicates
        Set<String> existingEdges = new HashSet<>();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            int minLen = Math.min(word1.length(), word2.length());

            // Check for invalid "longer prefix" case
            // If word1 starts with word2 and word1 is longer → invalid
            boolean foundDifference = false;

            for (int j = 0; j < minLen; j++) {
                char c1 = word1.charAt(j);
                char c2 = word2.charAt(j);

                if (c1 != c2) {
                    // First differing character → ordering rule: c1 → c2
                    String edgeKey = c1 + "->" + c2;
                    if (!existingEdges.contains(edgeKey)) {
                        adjacencyList.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                        existingEdges.add(edgeKey);
                    }
                    foundDifference = true;
                    break;  // ONLY first difference matters
                }
            }

            // If no difference found and word1 is longer → INVALID
            if (!foundDifference && word1.length() > word2.length()) {
                return "";
            }
        }

        // ── STEP 3: Kahn's BFS Topological Sort ──
        Queue<Character> queue = new ArrayDeque<>();

        // Enqueue all characters with in-degree 0
        for (Map.Entry<Character, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);

            // Process all neighbors
            for (char neighbor : adjacencyList.get(current)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // ── STEP 4: Validate — all characters must be in result ──
        if (result.length() != inDegree.size()) {
            return "";  // Cycle detected — not all characters processed
        }

        return result.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE FUNCTION — Show graph construction and BFS steps
    // ─────────────────────────────────────────────────────────────
    public static String alienOrderWithTrace(String[] words) {
        if (words == null || words.length == 0) {
            System.out.println("  Empty input → return \"\"");
            return "";
        }

        System.out.println("  Words: ");
        System.out.print("    [");
        for (int i = 0; i < words.length; i++) {
            System.out.print("\"" + words[i] + "\"");
            if (i < words.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        // Step 1: Collect characters
        Map<Character, List<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDeg = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new ArrayList<>());
                inDeg.putIfAbsent(c, 0);
            }
        }

        System.out.println("  ── STEP 1: Unique Characters ──");
        System.out.println("    " + inDeg.keySet());
        System.out.println();

        // Step 2: Build graph
        System.out.println("  ── STEP 2: Compare Adjacent Pairs ──");
        Set<String> existingEdges = new HashSet<>();

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean foundDiff = false;

            System.out.print("    \"" + w1 + "\" vs \"" + w2 + "\" → ");

            for (int j = 0; j < minLen; j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);

                if (c1 != c2) {
                    String edgeKey = c1 + "->" + c2;
                    if (!existingEdges.contains(edgeKey)) {
                        adj.get(c1).add(c2);
                        inDeg.put(c2, inDeg.get(c2) + 1);
                        existingEdges.add(edgeKey);
                        System.out.println("first diff at pos " + j
                            + ": '" + c1 + "'→'" + c2 + "' (NEW edge)");
                    } else {
                        System.out.println("first diff at pos " + j
                            + ": '" + c1 + "'→'" + c2 + "' (duplicate, skip)");
                    }
                    foundDiff = true;
                    break;
                }
            }

            if (!foundDiff) {
                if (w1.length() > w2.length()) {
                    System.out.println("NO diff, word1 longer → INVALID!");
                    return "";
                } else {
                    System.out.println("NO diff, same prefix (valid, no edge)");
                }
            }
        }

        System.out.println();
        System.out.println("  ── Graph Edges ──");
        for (Map.Entry<Character, List<Character>> entry : adj.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.println("    '" + entry.getKey() + "' → " + entry.getValue());
            }
        }
        System.out.println();
        System.out.println("  ── In-Degrees ──");
        for (Map.Entry<Character, Integer> entry : inDeg.entrySet()) {
            System.out.println("    '" + entry.getKey() + "': " + entry.getValue());
        }
        System.out.println();

        // Step 3: Kahn's BFS
        System.out.println("  ── STEP 3: Kahn's BFS ──");
        Queue<Character> queue = new ArrayDeque<>();
        for (Map.Entry<Character, Integer> entry : inDeg.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        System.out.println("    Initial queue (in-degree 0): " + queue);

        StringBuilder result = new StringBuilder();
        int step = 1;

        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);
            System.out.println("    Step " + step + ": process '" + current
                + "' → result so far: \"" + result + "\"");

            for (char neighbor : adj.get(current)) {
                int newDeg = inDeg.get(neighbor) - 1;
                inDeg.put(neighbor, newDeg);
                System.out.println("      decrement '" + neighbor
                    + "' in-degree to " + newDeg);
                if (newDeg == 0) {
                    queue.offer(neighbor);
                    System.out.println("      → '" + neighbor
                        + "' reaches in-degree 0 → enqueue");
                }
            }
            step++;
        }

        System.out.println();

        // Step 4: Validate
        int totalChars = adj.size();
        if (result.length() != totalChars) {
            System.out.println("  ── RESULT: \"" + result
                + "\" (length " + result.length()
                + " ≠ " + totalChars + " chars) → CYCLE → return \"\"");
            return "";
        }

        System.out.println("  ── RESULT: \"" + result
            + "\" (length " + result.length()
            + " == " + totalChars + " chars) → VALID ✓");
        return result.toString();
    }

    // ─────────────────────────────────────────────────────────────
    // VALIDATION HELPER — Check if result respects all word orderings
    // ─────────────────────────────────────────────────────────────
    public static boolean validateOrder(String[] words, String order) {
        if (order.isEmpty()) return false;

        // Build character rank map
        Map<Character, Integer> rank = new HashMap<>();
        for (int i = 0; i < order.length(); i++) {
            rank.put(order.charAt(i), i);
        }

        // Verify each adjacent pair is in correct order
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean foundDiff = false;

            for (int j = 0; j < minLen; j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);
                if (c1 != c2) {
                    if (!rank.containsKey(c1) || !rank.containsKey(c2)) {
                        return false;
                    }
                    if (rank.get(c1) >= rank.get(c2)) {
                        return false;  // Wrong order
                    }
                    foundDiff = true;
                    break;
                }
            }

            if (!foundDiff && w1.length() > w2.length()) {
                return false;  // Invalid prefix case
            }
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────
    // GRAPH VISUALIZER — ASCII representation of the constraint graph
    // ─────────────────────────────────────────────────────────────
    public static void visualizeGraph(String[] words) {
        if (words == null || words.length <= 1) {
            System.out.println("    (No edges — single word or empty)");
            return;
        }

        Map<Character, Set<Character>> adj = new HashMap<>();
        Set<Character> allChars = new HashSet<>();
        boolean invalid = false;

        for (String word : words) {
            for (char c : word.toCharArray()) {
                allChars.add(c);
                adj.putIfAbsent(c, new HashSet<>());
            }
        }

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            boolean foundDiff = false;

            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    foundDiff = true;
                    break;
                }
            }

            if (!foundDiff && w1.length() > w2.length()) {
                invalid = true;
            }
        }

        if (invalid) {
            System.out.println("    ⚠ INVALID INPUT (longer prefix before shorter)");
            return;
        }

        System.out.println("    Constraint Graph:");
        for (Map.Entry<Character, Set<Character>> entry : adj.entrySet()) {
            char from = entry.getKey();
            Set<Character> tos = entry.getValue();
            if (tos.isEmpty()) {
                System.out.println("      '" + from + "' → (no outgoing edges)");
            } else {
                for (char to : tos) {
                    System.out.println("      '" + from + "' → '" + to + "'");
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 56: Alien Dictionary                        ║");
        System.out.println("║  Pattern: Topological Sort → Graph from Constraints  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        String[] w1 = {"wrt", "wrf", "er", "ett", "rftt"};
        String r1 = alienOrderWithTrace(w1);
        System.out.println();

        // ── TEST 2: Simple Two Words ──
        System.out.println("═══ TEST 2: Simple Two Words ═══");
        String[] w2 = {"z", "x"};
        String r2 = alienOrderWithTrace(w2);
        System.out.println();

        // ── TEST 3: Cycle Detection ──
        System.out.println("═══ TEST 3: Cycle Detection ═══");
        String[] w3 = {"z", "x", "z"};
        String r3 = alienOrderWithTrace(w3);
        System.out.println();

        // ── TEST 4: Invalid Prefix Case ──
        System.out.println("═══ TEST 4: Invalid Prefix — Longer Before Shorter ═══");
        String[] w4 = {"abc", "ab"};
        String r4 = alienOrderWithTrace(w4);
        System.out.println();

        // ── TEST 5: Single Word ──
        System.out.println("═══ TEST 5: Single Word ═══");
        String[] w5 = {"hello"};
        String r5 = alienOrderWithTrace(w5);
        System.out.println();

        // ── TEST 6: Characters Not in Edges ──
        System.out.println("═══ TEST 6: Characters With No Constraints ═══");
        String[] w6 = {"ab", "cd"};
        String r6 = alienOrderWithTrace(w6);
        System.out.println();

        // ── TEST 7: Duplicate Edge Case ──
        System.out.println("═══ TEST 7: Duplicate Edges ═══");
        String[] w7 = {"ab", "ac", "bb", "bc"};
        String r7 = alienOrderWithTrace(w7);
        System.out.println();

        // ── TEST 8: Identical Words ──
        System.out.println("═══ TEST 8: Identical Words ═══");
        String[] w8 = {"z", "z"};
        String r8 = alienOrderWithTrace(w8);
        System.out.println();

        // ── TEST 9: Longer Chain ──
        System.out.println("═══ TEST 9: Longer Chain ═══");
        String[] w9 = {"baa", "abcd", "abca", "cab", "cad"};
        String r9 = alienOrderWithTrace(w9);
        System.out.println();

        // ── TEST 10: Valid Prefix (shorter before longer) ──
        System.out.println("═══ TEST 10: Valid Prefix — Shorter Before Longer ═══");
        String[] w10 = {"ab", "abc"};
        String r10 = alienOrderWithTrace(w10);
        System.out.println();

        // ── TEST 11: Verification Suite ──
        System.out.println("═══ TEST 11: Verification Suite ═══");

        String[][][] testSuite = {
            {{"wrt", "wrf", "er", "ett", "rftt"}, {"valid"}},
            {{"z", "x"}, {"valid"}},
            {{"z", "x", "z"}, {"cycle"}},
            {{"abc", "ab"}, {"invalid_prefix"}},
            {{"hello"}, {"valid"}},
            {{"ab", "cd"}, {"valid"}},
            {{"z", "z"}, {"valid"}},
            {{"baa", "abcd", "abca", "cab", "cad"}, {"valid"}},
            {{"ab", "abc"}, {"valid"}}
        };

        for (int t = 0; t < testSuite.length; t++) {
            String[] words = testSuite[t][0];
            String expectedType = testSuite[t][1][0];
            String result = alienOrder(words);

            boolean passed;
            if (expectedType.equals("cycle") || expectedType.equals("invalid_prefix")) {
                passed = result.isEmpty();
            } else {
                passed = !result.isEmpty();
                if (passed && words.length > 1) {
                    passed = validateOrder(words, result);
                }
            }

            System.out.printf("  Test %d: %-20s result=%-10s expected=%-15s %s%n",
                t + 1,
                wordsToString(words),
                "\"" + result + "\"",
                expectedType,
                passed ? "✓ PASS" : "✗ FAIL");
        }
        System.out.println();

        // ── TEST 12: Graph Visualization ──
        System.out.println("═══ TEST 12: Graph Visualizations ═══");
        System.out.println("  Example 1: [\"wrt\", \"wrf\", \"er\", \"ett\", \"rftt\"]");
        visualizeGraph(new String[]{"wrt", "wrf", "er", "ett", "rftt"});
        System.out.println();
        System.out.println("  Example 2: [\"baa\", \"abcd\", \"abca\", \"cab\", \"cad\"]");
        visualizeGraph(new String[]{"baa", "abcd", "abca", "cab", "cad"});
        System.out.println();
        System.out.println("  Example 3: [\"abc\", \"ab\"] (invalid)");
        visualizeGraph(new String[]{"abc", "ab"});
    }

    // Helper to format word arrays for display
    private static String wordsToString(String[] words) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < Math.min(words.length, 3); i++) {
            if (i > 0) sb.append(",");
            sb.append(words[i]);
        }
        if (words.length > 3) sb.append("...");
        sb.append("]");
        return sb.toString();
    }
}