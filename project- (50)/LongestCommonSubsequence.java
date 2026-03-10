import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LongestCommonSubsequence {

    // ═══════════════════════════════════════════════════════
    // APPROACH A: Standard 2D DP Table (Full)
    // ═══════════════════════════════════════════════════════
    
    public static int longestCommonSubsequence(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j] = LCS length of text1[0..i-1] and text2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];
        
        // Base cases: dp[0][j] = 0 and dp[i][0] = 0 (default in Java)
        
        // Fill the table row by row
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // Characters match → diagonal + 1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // Characters don't match → best of skipping either
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH B: Space-Optimized (Two Rows)
    // ═══════════════════════════════════════════════════════
    
    public static int lcsSpaceOptimized(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        // Ensure text2 is shorter (use shorter for columns → less space)
        if (m < n) {
            return lcsSpaceOptimized(text2, text1);
        }
        
        // Only two rows needed
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            
            // Swap rows: current becomes previous for next iteration
            int[] temp = prev;
            prev = curr;
            curr = temp;
            
            // Reset current row for next iteration
            Arrays.fill(curr, 0);
        }
        
        // After the last swap, result is in prev (was curr before swap)
        return prev[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH C: Single Row Optimization
    // ═══════════════════════════════════════════════════════
    
    public static int lcsSingleRow(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        if (m < n) {
            return lcsSingleRow(text2, text1);
        }
        
        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= m; i++) {
            int prevDiagonal = 0; // dp[i-1][j-1] before overwrite
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // save current (will become prevDiagonal)
                
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = prevDiagonal + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                    // dp[j] (before update) = top value
                    // dp[j-1] (already updated) = left value
                }
                
                prevDiagonal = temp;
            }
        }
        
        return dp[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH D: Top-Down with Memoization
    // Connects to Project 43 (memoization concept)
    // ═══════════════════════════════════════════════════════
    
    public static int lcsMemoized(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        int[][] memo = new int[m][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        
        return lcsHelper(text1, text2, m - 1, n - 1, memo);
    }
    
    private static int lcsHelper(
            String text1, String text2, 
            int i, int j, int[][] memo) {
        
        // Base case: either string exhausted
        if (i < 0 || j < 0) return 0;
        
        // Already computed
        if (memo[i][j] != -1) return memo[i][j];
        
        if (text1.charAt(i) == text2.charAt(j)) {
            memo[i][j] = 1 + lcsHelper(text1, text2, i - 1, j - 1, memo);
        } else {
            memo[i][j] = Math.max(
                lcsHelper(text1, text2, i - 1, j, memo),
                lcsHelper(text1, text2, i, j - 1, memo)
            );
        }
        
        return memo[i][j];
    }
    
    // ═══════════════════════════════════════════════════════
    // RECONSTRUCTION: Find the Actual LCS String
    // ═══════════════════════════════════════════════════════
    
    public static String findLCSString(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        // Build full table (needed for backtracking)
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // Backtrack to find the actual LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                // Characters match → part of LCS → go diagonal
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // Came from top → skip text1's character
                i--;
            } else {
                // Came from left → skip text2's character
                j--;
            }
        }
        
        // Reverse because we built it backward
        return lcs.reverse().toString();
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the 2D DP Table Construction
    // ═══════════════════════════════════════════════════════
    
    public static void lcsWithTrace(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        System.out.println("text1 = \"" + text1 + "\" (length " + m + ")");
        System.out.println("text2 = \"" + text2 + "\" (length " + n + ")");
        System.out.println();
        
        int[][] dp = new int[m + 1][n + 1];
        // Track which transition was used for visualization
        // 'D' = diagonal (match), 'T' = top, 'L' = left, '0' = base
        char[][] source = new char[m + 1][n + 1];
        for (char[] row : source) Arrays.fill(row, '0');
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    source[i][j] = 'D'; // Diagonal — match!
                } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                    dp[i][j] = dp[i - 1][j];
                    source[i][j] = 'T'; // Top — skip text1 char
                } else {
                    dp[i][j] = dp[i][j - 1];
                    source[i][j] = 'L'; // Left — skip text2 char
                }
            }
        }
        
        // Print the DP table
        System.out.println("─── DP Table ───");
        
        // Header row
        System.out.printf("%6s", "");
        System.out.printf("%4s", "\"\"");
        for (int j = 0; j < n; j++) {
            System.out.printf("%4c", text2.charAt(j));
        }
        System.out.println();
        
        // Separator
        System.out.print("      ");
        for (int j = 0; j <= n; j++) System.out.print("----");
        System.out.println();
        
        // Table rows
        for (int i = 0; i <= m; i++) {
            if (i == 0) {
                System.out.printf(" \"\" | ");
            } else {
                System.out.printf("  %c | ", text1.charAt(i - 1));
            }
            
            for (int j = 0; j <= n; j++) {
                if (source[i][j] == 'D') {
                    System.out.printf(" [%d]", dp[i][j]); // highlight matches
                } else {
                    System.out.printf("%4d", dp[i][j]);
                }
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println("Legend: [X] = diagonal match (character included in LCS)");
        
        // Reconstruct LCS
        String lcs = findLCSString(text1, text2);
        
        System.out.println();
        System.out.println("─── Reconstruction ───");
        
        // Trace the path
        int i = m, j = n;
        List<String> steps = new ArrayList<>();
        
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                steps.add("dp[" + i + "][" + j + "]: '" + text1.charAt(i - 1) 
                    + "' matches → INCLUDE → go diagonal");
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                steps.add("dp[" + i + "][" + j + "]: no match → go UP (skip '" 
                    + text1.charAt(i - 1) + "' from text1)");
                i--;
            } else {
                steps.add("dp[" + i + "][" + j + "]: no match → go LEFT (skip '" 
                    + text2.charAt(j - 1) + "' from text2)");
                j--;
            }
        }
        
        for (String step : steps) {
            System.out.println("  " + step);
        }
        
        System.out.println();
        System.out.println("═══ LCS = \"" + lcs + "\" (length " + lcs.length() + ") ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Diff — Show differences between two strings
    // Real-world use of LCS
    // ═══════════════════════════════════════════════════════
    
    public static void showDiff(String text1, String text2) {
        
        int m = text1.length();
        int n = text2.length();
        
        // Build full table
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // Backtrack and build diff
        StringBuilder diff = new StringBuilder();
        int i = m, j = n;
        List<String> ops = new ArrayList<>();
        
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && text1.charAt(i - 1) == text2.charAt(j - 1)) {
                ops.add(0, " " + text1.charAt(i - 1));  // unchanged
                i--;
                j--;
            } else if (j > 0 && (i == 0 || dp[i][j - 1] >= dp[i - 1][j])) {
                ops.add(0, "+" + text2.charAt(j - 1));  // added in text2
                j--;
            } else {
                ops.add(0, "-" + text1.charAt(i - 1));  // removed from text1
                i--;
            }
        }
        
        System.out.println("Diff (text1 → text2):");
        for (String op : ops) {
            char type = op.charAt(0);
            char ch = op.charAt(1);
            if (type == ' ') {
                System.out.println("  KEEP   '" + ch + "'");
            } else if (type == '+') {
                System.out.println("  ADD    '" + ch + "'");
            } else {
                System.out.println("  REMOVE '" + ch + "'");
            }
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Shortest Common Supersequence Length
    // Uses LCS as building block
    // ═══════════════════════════════════════════════════════
    
    public static int shortestCommonSupersequence(String text1, String text2) {
        // SCS length = m + n - LCS length
        // Because LCS characters are shared → counted once instead of twice
        int lcsLength = longestCommonSubsequence(text1, text2);
        return text1.length() + text2.length() - lcsLength;
    }
    
    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 50: Longest Common Subsequence               ║");
        System.out.println("║  Pattern: 2D DP → String Matching → Diagonal Transition║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        lcsWithTrace("ace", "abcde");
        System.out.println();
        
        // ── TEST 2: Identical strings ──
        System.out.println("═══ TEST 2: Identical Strings ═══");
        lcsWithTrace("abc", "abc");
        System.out.println();
        
        // ── TEST 3: No common characters ──
        System.out.println("═══ TEST 3: No Common Characters ═══");
        lcsWithTrace("abc", "def");
        System.out.println();
        
        // ── TEST 4: Longer example ──
        System.out.println("═══ TEST 4: Longer Example ═══");
        lcsWithTrace("ABCBDAB", "BDCABA");
        System.out.println();
        
        // ── TEST 5: Single character match ──
        System.out.println("═══ TEST 5: Single Character ═══");
        lcsWithTrace("a", "a");
        System.out.println();
        
        // ── TEST 6: Greedy failure case ──
        System.out.println("═══ TEST 6: Greedy Would Fail ═══");
        lcsWithTrace("abcb", "bcab");
        System.out.println();
        
        // ── TEST 7: Cross-verification of all approaches ──
        System.out.println("═══ TEST 7: Cross-Verification (All Approaches) ═══");
        String[][] testPairs = {
            {"ace", "abcde"},
            {"abc", "abc"},
            {"abc", "def"},
            {"ABCBDAB", "BDCABA"},
            {"horse", "ros"},
            {"abcb", "bcab"},
            {"", "abc"},
            {"a", ""},
            {"oxcpqrsvwf", "shmtulqrypy"},
            {"bsbininm", "jmjkbkjkv"},
        };
        
        for (int t = 0; t < testPairs.length; t++) {
            String t1 = testPairs[t][0];
            String t2 = testPairs[t][1];
            
            int fullDP = longestCommonSubsequence(t1, t2);
            int twoRow = lcsSpaceOptimized(t1, t2);
            int singleRow = lcsSingleRow(t1, t2);
            int memo = lcsMemoized(t1, t2);
            String actual = findLCSString(t1, t2);
            
            boolean allMatch = (fullDP == twoRow) && (twoRow == singleRow) 
                && (singleRow == memo) && (memo == actual.length());
            String status = allMatch ? "✓" : "✗";
            
            System.out.println("  Test " + (t + 1) 
                + ": \"" + t1 + "\" vs \"" + t2 + "\""
                + " → length=" + fullDP
                + " LCS=\"" + actual + "\""
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 8: Diff application ──
        System.out.println("═══ TEST 8: Diff Application ═══");
        showDiff("ABCBDAB", "BDCABA");
        System.out.println();
        
        // ── TEST 9: Shortest Common Supersequence ──
        System.out.println("═══ TEST 9: Shortest Common Supersequence ═══");
        String[][] scsPairs = {
            {"abac", "cab"},
            {"AGGTAB", "GXTXAYB"},
            {"abc", "def"},
        };
        
        for (String[] pair : scsPairs) {
            int lcsLen = longestCommonSubsequence(pair[0], pair[1]);
            int scsLen = shortestCommonSupersequence(pair[0], pair[1]);
            System.out.println("  \"" + pair[0] + "\" + \"" + pair[1] + "\""
                + " → LCS=" + lcsLen
                + "  SCS=" + scsLen
                + "  (m=" + pair[0].length() + " + n=" + pair[1].length() 
                + " - LCS=" + lcsLen + " = " + scsLen + ")");
        }
        System.out.println();
        
        // ── TEST 10: Performance benchmark ──
        System.out.println("═══ TEST 10: Performance Benchmark ═══");
        
        // Generate random strings
        java.util.Random rand = new java.util.Random(42);
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int benchSize = 2000;
        
        for (int i = 0; i < benchSize; i++) {
            sb1.append((char)('a' + rand.nextInt(26)));
            sb2.append((char)('a' + rand.nextInt(26)));
        }
        String bench1 = sb1.toString();
        String bench2 = sb2.toString();
        
        // Full DP
        long startTime = System.nanoTime();
        int result1 = longestCommonSubsequence(bench1, bench2);
        long fullTime = System.nanoTime() - startTime;
        
        // Two-row
        startTime = System.nanoTime();
        int result2 = lcsSpaceOptimized(bench1, bench2);
        long twoRowTime = System.nanoTime() - startTime;
        
        // Single-row
        startTime = System.nanoTime();
        int result3 = lcsSingleRow(bench1, bench2);
        long singleRowTime = System.nanoTime() - startTime;
        
        // Memoized
        startTime = System.nanoTime();
        int result4 = lcsMemoized(bench1, bench2);
        long memoTime = System.nanoTime() - startTime;
        
        System.out.println("  String lengths: " + benchSize + " × " + benchSize);
        System.out.println("  Full DP:    LCS=" + result1 + " → " 
            + (fullTime / 1_000_000) + " ms");
        System.out.println("  Two-row:    LCS=" + result2 + " → " 
            + (twoRowTime / 1_000_000) + " ms");
        System.out.println("  Single-row: LCS=" + result3 + " → " 
            + (singleRowTime / 1_000_000) + " ms");
        System.out.println("  Memoized:   LCS=" + result4 + " → " 
            + (memoTime / 1_000_000) + " ms");
        System.out.println("  All match: " 
            + (result1 == result2 && result2 == result3 && result3 == result4));
        System.out.println();
        
        // ── TEST 11: Scalability test ──
        System.out.println("═══ TEST 11: Scalability Test ═══");
        int[] sizes = {100, 500, 1000, 2000, 5000};
        
        for (int size : sizes) {
            sb1 = new StringBuilder();
            sb2 = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb1.append((char)('a' + rand.nextInt(4))); // small alphabet → longer LCS
                sb2.append((char)('a' + rand.nextInt(4)));
            }
            
            startTime = System.nanoTime();
            int res = lcsSingleRow(sb1.toString(), sb2.toString());
            long time = System.nanoTime() - startTime;
            
            System.out.println("  n=" + size + " → LCS=" + res 
                + " → " + (time / 1_000_000) + " ms");
        }
    }
}