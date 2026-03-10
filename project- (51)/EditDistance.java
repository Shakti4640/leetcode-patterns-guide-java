import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class EditDistance {

    // ═══════════════════════════════════════════════════════
    // APPROACH A: Standard 2D DP Table (Full)
    // ═══════════════════════════════════════════════════════
    
    public static int minDistance(String word1, String word2) {
        
        int m = word1.length();
        int n = word2.length();
        
        // dp[i][j] = min operations to convert word1[0..i-1] to word2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];
        
        // Base cases: transforming to/from empty string
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // delete all characters from word1
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // insert all characters of word2
        }
        
        // Fill the table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // Characters match → no operation needed → diagonal + 0
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Characters don't match → cheapest of 3 operations
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],  // REPLACE
                        Math.min(
                            dp[i - 1][j],   // DELETE
                            dp[i][j - 1]    // INSERT
                        )
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH B: Space-Optimized (Two Rows)
    // Same technique as Project 50 (LCS)
    // ═══════════════════════════════════════════════════════
    
    public static int minDistanceOptimized(String word1, String word2) {
        
        int m = word1.length();
        int n = word2.length();
        
        // Ensure word2 is shorter for space optimization
        if (m < n) {
            return minDistanceOptimized(word2, word1);
        }
        
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        // Base case: first row → dp[0][j] = j
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            curr[0] = i;  // Base case: dp[i][0] = i
            
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        prev[j - 1],           // replace
                        Math.min(prev[j],       // delete
                                 curr[j - 1])   // insert
                    );
                }
            }
            
            // Swap rows
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH C: Single Row Optimization
    // ═══════════════════════════════════════════════════════
    
    public static int minDistanceSingleRow(String word1, String word2) {
        
        int m = word1.length();
        int n = word2.length();
        
        if (m < n) {
            return minDistanceSingleRow(word2, word1);
        }
        
        int[] dp = new int[n + 1];
        
        // Base case: dp[0][j] = j
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            int prevDiagonal = dp[0];  // dp[i-1][0] = i-1
            dp[0] = i;                 // dp[i][0] = i
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];  // save dp[i-1][j] before overwrite
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prevDiagonal;  // match → diagonal
                } else {
                    dp[j] = 1 + Math.min(
                        prevDiagonal,              // replace (diagonal)
                        Math.min(dp[j],            // delete (top = old dp[j])
                                 dp[j - 1])        // insert (left = new dp[j-1])
                    );
                }
                
                prevDiagonal = temp;  // for next column's diagonal
            }
        }
        
        return dp[n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH D: Top-Down with Memoization
    // ═══════════════════════════════════════════════════════
    
    public static int minDistanceMemo(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        int[][] memo = new int[m][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        
        return editHelper(word1, word2, m - 1, n - 1, memo);
    }
    
    private static int editHelper(
            String word1, String word2,
            int i, int j, int[][] memo) {
        
        // Base cases
        if (i < 0) return j + 1;  // insert remaining j+1 characters
        if (j < 0) return i + 1;  // delete remaining i+1 characters
        
        if (memo[i][j] != -1) return memo[i][j];
        
        if (word1.charAt(i) == word2.charAt(j)) {
            memo[i][j] = editHelper(word1, word2, i - 1, j - 1, memo);
        } else {
            memo[i][j] = 1 + Math.min(
                editHelper(word1, word2, i - 1, j - 1, memo),  // replace
                Math.min(
                    editHelper(word1, word2, i - 1, j, memo),   // delete
                    editHelper(word1, word2, i, j - 1, memo)    // insert
                )
            );
        }
        
        return memo[i][j];
    }
    
    // ═══════════════════════════════════════════════════════
    // RECONSTRUCTION: Find the Actual Edit Operations
    // ═══════════════════════════════════════════════════════
    
    public static List<String> findOperations(String word1, String word2) {
        
        int m = word1.length();
        int n = word2.length();
        
        // Build full table
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }
        
        // Backtrack to find operations
        List<String> operations = new ArrayList<>();
        int i = m, j = n;
        
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && word1.charAt(i - 1) == word2.charAt(j - 1)) {
                // Match → no operation → go diagonal
                operations.add(0, "KEEP '" + word1.charAt(i - 1) + "'");
                i--;
                j--;
            } else if (i > 0 && j > 0 
                    && dp[i][j] == dp[i - 1][j - 1] + 1) {
                // Replace → diagonal + 1
                operations.add(0, "REPLACE '" + word1.charAt(i - 1) 
                    + "' with '" + word2.charAt(j - 1) + "'");
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                // Delete → from top
                operations.add(0, "DELETE '" + word1.charAt(i - 1) + "'");
                i--;
            } else {
                // Insert → from left
                operations.add(0, "INSERT '" + word2.charAt(j - 1) + "'");
                j--;
            }
        }
        
        return operations;
    }
    
    // ═══════════════════════════════════════════════════════
    // VARIANT: Edit Distance with Different Costs
    // ═══════════════════════════════════════════════════════
    
    public static int minDistanceWeighted(
            String word1, String word2,
            int insertCost, int deleteCost, int replaceCost) {
        
        int m = word1.length();
        int n = word2.length();
        
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) dp[i][0] = i * deleteCost;
        for (int j = 0; j <= n; j++) dp[0][j] = j * insertCost;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + replaceCost,
                        Math.min(
                            dp[i - 1][j] + deleteCost,
                            dp[i][j - 1] + insertCost
                        )
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPLICATION: Spell Checker — Find Closest Words
    // ═══════════════════════════════════════════════════════
    
    public static List<String> suggestCorrections(
            String misspelled, String[] dictionary, int maxDistance) {
        
        List<String> suggestions = new ArrayList<>();
        
        for (String word : dictionary) {
            int dist = minDistance(misspelled, word);
            if (dist <= maxDistance && dist > 0) {
                suggestions.add(word + " (distance=" + dist + ")");
            }
        }
        
        // Sort by distance (already have distance in string, parse it)
        suggestions.sort((a, b) -> {
            int distA = Integer.parseInt(a.split("=")[1].replace(")", ""));
            int distB = Integer.parseInt(b.split("=")[1].replace(")", ""));
            return distA - distB;
        });
        
        return suggestions;
    }
    
    // ═══════════════════════════════════════════════════════
    // VERIFICATION: Relationship between Edit Distance and LCS
    // Edit Distance (ins/del only) = m + n - 2 * LCS
    // ═══════════════════════════════════════════════════════
    
    public static int editDistanceInsDelOnly(String word1, String word2) {
        // Without replace: only insert and delete allowed
        int m = word1.length();
        int n = word2.length();
        
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Only insert and delete — NO replace
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // LCS from Project 50 (reused for verification)
    public static int lcs(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
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
        
        return dp[m][n];
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the 2D DP Table Construction
    // ═══════════════════════════════════════════════════════
    
    public static void editDistanceWithTrace(String word1, String word2) {
        
        int m = word1.length();
        int n = word2.length();
        
        System.out.println("word1 = \"" + word1 + "\" (length " + m + ")");
        System.out.println("word2 = \"" + word2 + "\" (length " + n + ")");
        System.out.println();
        
        int[][] dp = new int[m + 1][n + 1];
        // Track source: 'M'=match, 'R'=replace, 'D'=delete, 'I'=insert, 'B'=base
        char[][] source = new char[m + 1][n + 1];
        
        // Base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            source[i][0] = 'B';
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            source[0][j] = 'B';
        }
        
        // Fill table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    source[i][j] = 'M'; // Match
                } else {
                    int replace = dp[i - 1][j - 1];
                    int delete = dp[i - 1][j];
                    int insert = dp[i][j - 1];
                    
                    int minVal = Math.min(replace, Math.min(delete, insert));
                    dp[i][j] = 1 + minVal;
                    
                    if (minVal == replace) source[i][j] = 'R';
                    else if (minVal == delete) source[i][j] = 'D';
                    else source[i][j] = 'I';
                }
            }
        }
        
        // Print the DP table
        System.out.println("─── DP Table ───");
        
        // Header
        System.out.printf("%8s", "");
        System.out.printf("%5s", "\"\"");
        for (int j = 0; j < n; j++) {
            System.out.printf("%5c", word2.charAt(j));
        }
        System.out.println();
        
        System.out.print("        ");
        for (int j = 0; j <= n; j++) System.out.print("-----");
        System.out.println();
        
        // Rows
        for (int i = 0; i <= m; i++) {
            if (i == 0) {
                System.out.printf("  \"\" |  ");
            } else {
                System.out.printf("   %c |  ", word1.charAt(i - 1));
            }
            
            for (int j = 0; j <= n; j++) {
                char s = source[i][j];
                if (s == 'M') {
                    System.out.printf(" [%d] ", dp[i][j]); // match highlighted
                } else {
                    System.out.printf("  %d  ", dp[i][j]);
                }
            }
            System.out.println();
        }
        
        System.out.println("\nLegend: [X] = match (free — no operation needed)");
        
        // Reconstruct operations
        List<String> ops = findOperations(word1, word2);
        
        System.out.println("\n─── Operations ───");
        for (String op : ops) {
            System.out.println("  " + op);
        }
        
        System.out.println("\n═══ Edit Distance = " + dp[m][n] + " ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 51: Edit Distance (Levenshtein)         ║");
        System.out.println("║  Pattern: 2D DP → Insert/Delete/Replace → MinCost║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: horse → ros ═══");
        editDistanceWithTrace("horse", "ros");
        System.out.println();
        
        // ── TEST 2: intention → execution ──
        System.out.println("═══ TEST 2: intention → execution ═══");
        editDistanceWithTrace("intention", "execution");
        System.out.println();
        
        // ── TEST 3: Identical strings ──
        System.out.println("═══ TEST 3: Identical Strings ═══");
        editDistanceWithTrace("abc", "abc");
        System.out.println();
        
        // ── TEST 4: Empty to non-empty ──
        System.out.println("═══ TEST 4: Empty → Non-Empty ═══");
        editDistanceWithTrace("", "abc");
        System.out.println();
        
        // ── TEST 5: Non-empty to empty ──
        System.out.println("═══ TEST 5: Non-Empty → Empty ═══");
        editDistanceWithTrace("abc", "");
        System.out.println();
        
        // ── TEST 6: kitten → sitting ──
        System.out.println("═══ TEST 6: kitten → sitting ═══");
        editDistanceWithTrace("kitten", "sitting");
        System.out.println();
        
        // ── TEST 7: Single character ──
        System.out.println("═══ TEST 7: Single Character Differences ═══");
        editDistanceWithTrace("a", "b");
        System.out.println();
        
        // ── TEST 8: Cross-verification of all approaches ──
        System.out.println("═══ TEST 8: Cross-Verification (All Approaches) ═══");
        String[][] testPairs = {
            {"horse", "ros"},
            {"intention", "execution"},
            {"abc", "abc"},
            {"", "abc"},
            {"abc", ""},
            {"kitten", "sitting"},
            {"a", "b"},
            {"saturday", "sunday"},
            {"dinitrophenylhydrazine", "acetylaminophenol"},
            {"pneumonoultramicroscopicsilicovolcanoconiosis", "pseudopseudohypoparathyroidism"},
        };
        
        for (int t = 0; t < testPairs.length; t++) {
            String w1 = testPairs[t][0];
            String w2 = testPairs[t][1];
            
            int fullDP = minDistance(w1, w2);
            int twoRow = minDistanceOptimized(w1, w2);
            int singleRow = minDistanceSingleRow(w1, w2);
            int memo = minDistanceMemo(w1, w2);
            
            boolean allMatch = (fullDP == twoRow) && (twoRow == singleRow) 
                && (singleRow == memo);
            String status = allMatch ? "✓" : "✗";
            
            System.out.println("  Test " + (t + 1) 
                + ": \"" + w1 + "\" → \"" + w2 + "\""
                + " → dist=" + fullDP + "  " + status);
        }
        System.out.println();
        
        // ── TEST 9: Verify LCS relationship ──
        System.out.println("═══ TEST 9: Edit Distance ↔ LCS Relationship ═══");
        System.out.println("  Formula: EditDist(ins/del only) = m + n - 2 × LCS");
        System.out.println();
        
        String[][] lcsPairs = {
            {"horse", "ros"},
            {"abcde", "ace"},
            {"kitten", "sitting"},
            {"abc", "def"},
            {"ABCBDAB", "BDCABA"},
        };
        
        for (String[] pair : lcsPairs) {
            String w1 = pair[0];
            String w2 = pair[1];
            int m = w1.length();
            int n = w2.length();
            
            int lcsLen = lcs(w1, w2);
            int edInsDelOnly = editDistanceInsDelOnly(w1, w2);
            int formulaResult = m + n - 2 * lcsLen;
            int edFull = minDistance(w1, w2);
            
            String match = (edInsDelOnly == formulaResult) ? "✓" : "✗";
            
            System.out.println("  \"" + w1 + "\" vs \"" + w2 + "\":");
            System.out.println("    LCS=" + lcsLen 
                + "  ED(ins/del)=" + edInsDelOnly
                + "  m+n-2L=" + formulaResult + " " + match
                + "  ED(full)=" + edFull 
                + " (≤ " + formulaResult + ": " + (edFull <= formulaResult) + ")");
        }
        System.out.println();
        
        // ── TEST 10: Spell Checker Application ──
        System.out.println("═══ TEST 10: Spell Checker Application ═══");
        String[] dictionary = {
            "apple", "apply", "ape", "maple", "angle",
            "simple", "sample", "able", "cable", "table",
            "orange", "arrange", "strange", "change", "range"
        };
        
        String[] misspelled = {"aple", "oragne", "tabel"};
        
        for (String word : misspelled) {
            List<String> suggestions = suggestCorrections(word, dictionary, 2);
            System.out.println("  \"" + word + "\" → Suggestions:");
            for (String s : suggestions) {
                System.out.println("    " + s);
            }
        }
        System.out.println();
        
        // ── TEST 11: Weighted Edit Distance ──
        System.out.println("═══ TEST 11: Weighted Edit Distance ═══");
        String w1 = "abc";
        String w2 = "axc";
        
        System.out.println("  \"" + w1 + "\" → \"" + w2 + "\"");
        System.out.println("  Standard (1,1,1):     " 
            + minDistanceWeighted(w1, w2, 1, 1, 1));
        System.out.println("  Expensive replace (1,1,3): " 
            + minDistanceWeighted(w1, w2, 1, 1, 3));
        System.out.println("  Expensive insert (5,1,1):  " 
            + minDistanceWeighted(w1, w2, 5, 1, 1));
        System.out.println("  Cheap insert (1,5,5):      " 
            + minDistanceWeighted(w1, w2, 1, 5, 5));
        System.out.println();
        
        // ── TEST 12: Performance benchmark ──
        System.out.println("═══ TEST 12: Performance Benchmark ═══");
        
        java.util.Random rand = new java.util.Random(42);
        int[] sizes = {100, 500, 1000, 2000, 5000};
        
        for (int size : sizes) {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb1.append((char)('a' + rand.nextInt(26)));
                sb2.append((char)('a' + rand.nextInt(26)));
            }
            
            String bench1 = sb1.toString();
            String bench2 = sb2.toString();
            
            long startTime = System.nanoTime();
            int result = minDistanceSingleRow(bench1, bench2);
            long time = System.nanoTime() - startTime;
            
            System.out.println("  n=" + size + " → dist=" + result 
                + " → " + (time / 1_000_000) + " ms");
        }
    }
}