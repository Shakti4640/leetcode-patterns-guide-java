import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

public class WordBreak {

    // ═══════════════════════════════════════════════════════
    // APPROACH A: Bottom-Up DP (Standard)
    // ═══════════════════════════════════════════════════════
    
    public static boolean wordBreak(String s, List<String> wordDict) {
        
        int n = s.length();
        Set<String> dict = new HashSet<>(wordDict);
        
        // dp[i] = can s[0..i-1] be segmented into dictionary words?
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;  // empty string base case
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                // If s[0..j-1] is segmentable AND s[j..i-1] is a dict word
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;  // found one valid segmentation → stop
                }
            }
        }
        
        return dp[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH B: Optimized DP (Max Word Length Pruning)
    // ═══════════════════════════════════════════════════════
    
    public static boolean wordBreakOptimized(String s, List<String> wordDict) {
        
        int n = s.length();
        Set<String> dict = new HashSet<>(wordDict);
        
        // Find max word length for pruning
        int maxLen = 0;
        for (String word : wordDict) {
            maxLen = Math.max(maxLen, word.length());
        }
        
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        for (int i = 1; i <= n; i++) {
            // Only check j values where i-j ≤ maxLen
            // → j ≥ i - maxLen
            for (int j = Math.max(0, i - maxLen); j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH C: DP iterating over dictionary words
    // Better when dictionary is small
    // ═══════════════════════════════════════════════════════
    
    public static boolean wordBreakDictIterate(String s, List<String> wordDict) {
        
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        for (int i = 1; i <= n; i++) {
            for (String word : wordDict) {
                int len = word.length();
                
                // Word must fit: i >= len
                // Previous position must be reachable: dp[i - len]
                // Substring must match: s[i-len..i-1] == word
                if (i >= len 
                    && dp[i - len] 
                    && s.substring(i - len, i).equals(word)) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH D: Top-Down with Memoization
    // ═══════════════════════════════════════════════════════
    
    public static boolean wordBreakMemo(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        // memo[i] = null (uncomputed), true, or false
        Boolean[] memo = new Boolean[s.length() + 1];
        return canBreak(s, dict, 0, memo);
    }
    
    private static boolean canBreak(
            String s, Set<String> dict, int start, Boolean[] memo) {
        
        // Reached the end → successful segmentation
        if (start == s.length()) return true;
        
        // Already computed
        if (memo[start] != null) return memo[start];
        
        // Try every possible end position
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            if (dict.contains(word) && canBreak(s, dict, end, memo)) {
                memo[start] = true;
                return true;
            }
        }
        
        memo[start] = false;
        return false;
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH E: BFS on Implicit Graph
    // Positions are nodes, dictionary words create edges
    // Connects to Project 17 (Tree BFS) and Project 49 (Jump Game)
    // ═══════════════════════════════════════════════════════
    
    public static boolean wordBreakBFS(String s, List<String> wordDict) {
        
        Set<String> dict = new HashSet<>(wordDict);
        int n = s.length();
        
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(0);  // start from position 0
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int start = queue.poll();
            
            for (int end = start + 1; end <= n; end++) {
                if (!visited[end] && dict.contains(s.substring(start, end))) {
                    if (end == n) return true;  // reached the end!
                    
                    visited[end] = true;
                    queue.offer(end);
                }
            }
        }
        
        return false;
    }
    
    // ═══════════════════════════════════════════════════════
    // EXTENSION: Word Break II — Return ALL valid segmentations
    // Uses DP + Backtracking
    // ═══════════════════════════════════════════════════════
    
    public static List<String> wordBreakII(String s, List<String> wordDict) {
        
        Set<String> dict = new HashSet<>(wordDict);
        Map<Integer, List<String>> memo = new HashMap<>();
        
        return allSegmentations(s, dict, 0, memo);
    }
    
    private static List<String> allSegmentations(
            String s, Set<String> dict, int start,
            Map<Integer, List<String>> memo) {
        
        if (memo.containsKey(start)) return memo.get(start);
        
        List<String> result = new ArrayList<>();
        
        // Base case: reached the end
        if (start == s.length()) {
            result.add("");  // empty string signals end of segmentation
            return result;
        }
        
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            
            if (dict.contains(word)) {
                List<String> suffixes = allSegmentations(s, dict, end, memo);
                
                for (String suffix : suffixes) {
                    if (suffix.isEmpty()) {
                        result.add(word);
                    } else {
                        result.add(word + " " + suffix);
                    }
                }
            }
        }
        
        memo.put(start, result);
        return result;
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the DP Process
    // ═══════════════════════════════════════════════════════
    
    public static void wordBreakWithTrace(String s, List<String> wordDict) {
        
        int n = s.length();
        Set<String> dict = new HashSet<>(wordDict);
        
        System.out.println("String: \"" + s + "\" (length " + n + ")");
        System.out.println("Dictionary: " + wordDict);
        System.out.println();
        
        // Show string with indices
        System.out.print("Index:  ");
        for (int i = 0; i < n; i++) System.out.printf("%-3d", i);
        System.out.println();
        System.out.print("Char:   ");
        for (int i = 0; i < n; i++) System.out.printf("%-3c", s.charAt(i));
        System.out.println();
        System.out.println();
        
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        // Track which j made dp[i] true (for reconstruction)
        int[] splitAt = new int[n + 1];
        Arrays.fill(splitAt, -1);
        
        System.out.println("dp[0] = true (base case — empty string)");
        System.out.println();
        
        for (int i = 1; i <= n; i++) {
            System.out.println("Computing dp[" + i + "] — s[0.." + (i - 1) + "] = \"" 
                + s.substring(0, i) + "\":");
            
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (!dp[j]) continue;  // skip unreachable positions
                
                String sub = s.substring(j, i);
                boolean inDict = dict.contains(sub);
                
                System.out.println("  j=" + j + ": dp[" + j + "]=true"
                    + " → s[" + j + ".." + (i - 1) + "]=\"" + sub + "\""
                    + " in dict? " + (inDict ? "YES ✓" : "no"));
                
                if (inDict) {
                    dp[i] = true;
                    splitAt[i] = j;
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("  → No valid split found");
            }
            
            System.out.println("  dp[" + i + "] = " + dp[i]);
            System.out.println();
        }
        
        // Show final dp array
        System.out.print("Final dp: [");
        for (int i = 0; i <= n; i++) {
            System.out.print(dp[i] ? "T" : "F");
            if (i < n) System.out.print(", ");
        }
        System.out.println("]");
        
        // Reconstruct one valid segmentation
        if (dp[n]) {
            List<String> words = new ArrayList<>();
            int pos = n;
            while (pos > 0) {
                words.add(0, s.substring(splitAt[pos], pos));
                pos = splitAt[pos];
            }
            System.out.println("One valid segmentation: " + String.join(" + ", words));
        }
        
        System.out.println("\n═══ ANSWER: " + dp[n] + " ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Chinese/Japanese Word Segmentation Simulation
    // ═══════════════════════════════════════════════════════
    
    public static void wordSegmentationDemo() {
        
        System.out.println("─── Word Segmentation Demo ───");
        System.out.println("Simulating word segmentation for a language without spaces\n");
        
        // Simulate with English-like concatenated words
        String text = "iloveprogrammingandsolvinghardproblems";
        List<String> vocabulary = Arrays.asList(
            "i", "love", "programming", "and", "solving", 
            "hard", "problems", "pro", "gram", "solve",
            "in", "g", "an", "do", "lemon"
        );
        
        System.out.println("Text: \"" + text + "\"");
        System.out.println("Vocabulary: " + vocabulary);
        
        boolean canSegment = wordBreak(text, vocabulary);
        System.out.println("Can segment: " + canSegment);
        
        if (canSegment) {
            List<String> segmentations = wordBreakII(text, vocabulary);
            System.out.println("Valid segmentations (" + segmentations.size() + "):");
            for (int i = 0; i < Math.min(segmentations.size(), 5); i++) {
                System.out.println("  → " + segmentations.get(i));
            }
            if (segmentations.size() > 5) {
                System.out.println("  ... and " + (segmentations.size() - 5) + " more");
            }
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 52: Word Break — String Segmentation     ║");
        System.out.println("║  Pattern: 1D DP → Dictionary-Driven Transitions   ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: leetcode ═══");
        wordBreakWithTrace("leetcode", Arrays.asList("leet", "code"));
        System.out.println();
        
        // ── TEST 2: Word reuse ──
        System.out.println("═══ TEST 2: applepenapple (word reuse) ═══");
        wordBreakWithTrace("applepenapple", Arrays.asList("apple", "pen"));
        System.out.println();
        
        // ── TEST 3: Impossible ──
        System.out.println("═══ TEST 3: catsandog (impossible) ═══");
        wordBreakWithTrace("catsandog", 
            Arrays.asList("cats", "dog", "sand", "and", "cat"));
        System.out.println();
        
        // ── TEST 4: Greedy would fail ──
        System.out.println("═══ TEST 4: goalspecial (greedy fails) ═══");
        wordBreakWithTrace("goalspecial", 
            Arrays.asList("go", "goal", "goals", "special"));
        System.out.println();
        
        // ── TEST 5: Multiple valid splits ──
        System.out.println("═══ TEST 5: catsanddog (multiple splits) ═══");
        wordBreakWithTrace("catsanddog", 
            Arrays.asList("cats", "cat", "sand", "and", "dog"));
        System.out.println();
        
        // ── TEST 6: Single character words ──
        System.out.println("═══ TEST 6: aaaaaaa (repeated characters) ═══");
        wordBreakWithTrace("aaaaaaa", Arrays.asList("aaa", "aaaa"));
        System.out.println();
        
        // ── TEST 7: Empty string ──
        System.out.println("═══ TEST 7: Empty String ═══");
        boolean emptyResult = wordBreak("", Arrays.asList("a", "b"));
        System.out.println("\"\" with dict [\"a\",\"b\"] → " + emptyResult);
        System.out.println();
        
        // ── TEST 8: Cross-verification of all approaches ──
        System.out.println("═══ TEST 8: Cross-Verification (All Approaches) ═══");
        
        String[] strings = {
            "leetcode", "applepenapple", "catsandog", "goalspecial",
            "catsanddog", "aaaaaaa", "bb", "", "a", "cars",
            "abcd", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
        };
        
        @SuppressWarnings("unchecked")
        List<String>[] dicts = new List[] {
            Arrays.asList("leet", "code"),
            Arrays.asList("apple", "pen"),
            Arrays.asList("cats", "dog", "sand", "and", "cat"),
            Arrays.asList("go", "goal", "goals", "special"),
            Arrays.asList("cats", "cat", "sand", "and", "dog"),
            Arrays.asList("aaa", "aaaa"),
            Arrays.asList("a", "b", "bbb", "bbbb"),
            Arrays.asList("a", "b"),
            Arrays.asList("a"),
            Arrays.asList("car", "ca", "rs"),
            Arrays.asList("a", "abc", "b", "cd"),
            Arrays.asList("a", "aa", "aaa", "aaaa", "aaaaa", 
                          "aaaaaa", "aaaaaaa", "aaaaaaaa", 
                          "aaaaaaaaa", "aaaaaaaaaa"),
        };
        
        for (int t = 0; t < strings.length; t++) {
            String s = strings[t];
            List<String> dict = dicts[t];
            
            boolean standard = wordBreak(s, dict);
            boolean optimized = wordBreakOptimized(s, dict);
            boolean dictIter = wordBreakDictIterate(s, dict);
            boolean memo = wordBreakMemo(s, dict);
            boolean bfs = wordBreakBFS(s, dict);
            
            boolean allMatch = (standard == optimized) 
                && (optimized == dictIter)
                && (dictIter == memo) 
                && (memo == bfs);
            String status = allMatch ? "✓" : "✗";
            
            String display = s.length() > 20 ? s.substring(0, 20) + "..." : s;
            System.out.println("  Test " + (t + 1) 
                + ": \"" + display + "\""
                + " → " + standard + "  " + status);
        }
        System.out.println();
        
        // ── TEST 9: Word Break II — All segmentations ──
        System.out.println("═══ TEST 9: Word Break II — All Segmentations ═══");
        
        String s9 = "catsanddog";
        List<String> dict9 = Arrays.asList("cats", "cat", "sand", "and", "dog");
        List<String> allSegs = wordBreakII(s9, dict9);
        System.out.println("\"" + s9 + "\" → " + allSegs.size() + " segmentations:");
        for (String seg : allSegs) {
            System.out.println("  → " + seg);
        }
        System.out.println();
        
        String s9b = "pineapplepenapple";
        List<String> dict9b = Arrays.asList(
            "apple", "pen", "applepen", "pine", "pineapple");
        List<String> allSegs2 = wordBreakII(s9b, dict9b);
        System.out.println("\"" + s9b + "\" → " + allSegs2.size() + " segmentations:");
        for (String seg : allSegs2) {
            System.out.println("  → " + seg);
        }
        System.out.println();
        
        // ── TEST 10: Word Segmentation Application ──
        System.out.println("═══ TEST 10: Word Segmentation Application ═══");
        wordSegmentationDemo();
        System.out.println();
        
        // ── TEST 11: Performance benchmark ──
        System.out.println("═══ TEST 11: Performance Benchmark ═══");
        
        // Build a long string and dictionary
        StringBuilder sb = new StringBuilder();
        List<String> perfDict = Arrays.asList(
            "a", "ab", "abc", "abcd", "abcde",
            "b", "bc", "bcd", "bcde",
            "c", "cd", "cde", "d", "de", "e"
        );
        
        int[] benchSizes = {100, 500, 1000, 5000};
        java.util.Random rand = new java.util.Random(42);
        
        for (int size : benchSizes) {
            sb.setLength(0);
            for (int i = 0; i < size; i++) {
                sb.append((char)('a' + rand.nextInt(5)));
            }
            String benchStr = sb.toString();
            
            long startTime = System.nanoTime();
            boolean result1 = wordBreakOptimized(benchStr, perfDict);
            long optimizedTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            boolean result2 = wordBreakDictIterate(benchStr, perfDict);
            long dictIterTime = System.nanoTime() - startTime;
            
            System.out.println("  n=" + size 
                + " → optimized: " + result1 + " (" + (optimizedTime / 1_000_000) + " ms)"
                + "  dictIter: " + result2 + " (" + (dictIterTime / 1_000_000) + " ms)");
        }
        System.out.println();
        
        // ── TEST 12: Worst case — the famous TLE test ──
        System.out.println("═══ TEST 12: Worst Case — TLE Prevention ═══");
        String worstCase = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        List<String> worstDict = Arrays.asList(
            "a", "aa", "aaa", "aaaa", "aaaaa",
            "aaaaaa", "aaaaaaa", "aaaaaaaa",
            "aaaaaaaaa", "aaaaaaaaaa"
        );
        
        long startTime = System.nanoTime();
        boolean worstResult = wordBreakOptimized(worstCase, worstDict);
        long worstTime = System.nanoTime() - startTime;
        
        System.out.println("  \"" + worstCase + "\"");
        System.out.println("  Result: " + worstResult + " → " 
            + (worstTime / 1_000) + " μs");
        System.out.println("  (Every 'a' prefix is segmentable, "
            + "but 'b' at end blocks everything)");
    }
}