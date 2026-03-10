import java.util.HashMap;
import java.util.Map;

public class LongestSubstringKDistinct {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Sliding Window + HashMap
    // ─────────────────────────────────────────────
    public static int longestSubstringKDistinct(String s, int k) {
        
        // Edge cases
        if (s == null || s.length() == 0 || k == 0) {
            return 0;
        }
        
        // Frequency map: character → count within current window
        HashMap<Character, Integer> charFrequency = new HashMap<>();
        
        int windowStart = 0;
        int maxLength = 0;
        
        // Expand the window by moving windowEnd forward
        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) {
            
            // ── EXPAND: Add rightmost character to the window ──
            char rightChar = s.charAt(windowEnd);
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);
            
            // ── SHRINK: Remove from left until we have at most K distinct ──
            while (charFrequency.size() > k) {
                char leftChar = s.charAt(windowStart);
                charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);
                
                // If count reaches 0, remove the key entirely
                // This keeps map.size() accurate
                if (charFrequency.get(leftChar) == 0) {
                    charFrequency.remove(leftChar);
                }
                
                windowStart++;
            }
            
            // ── UPDATE: Record maximum valid window length ──
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1);
        }
        
        return maxLength;
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE: O(n³) — check all substrings
    // ─────────────────────────────────────────────
    public static int longestSubstringKDistinctBrute(String s, int k) {
        
        if (s == null || s.length() == 0 || k == 0) return 0;
        
        int maxLength = 0;
        
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                // Count distinct characters in s[i..j]
                HashMap<Character, Integer> freq = new HashMap<>();
                for (int m = i; m <= j; m++) {
                    freq.put(s.charAt(m), freq.getOrDefault(s.charAt(m), 0) + 1);
                }
                if (freq.size() <= k) {
                    maxLength = Math.max(maxLength, j - i + 1);
                }
            }
        }
        
        return maxLength;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Exactly K distinct characters
    // Uses the "at most K" - "at most K-1" trick
    // ─────────────────────────────────────────────
    public static int longestSubstringExactlyKDistinct(String s, int k) {
        return longestSubstringKDistinct(s, k) - longestSubstringKDistinct(s, k - 1);
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Using int array instead of HashMap
    // (only for known character set — lowercase a-z)
    // ─────────────────────────────────────────────
    public static int longestSubstringKDistinctArray(String s, int k) {
        
        if (s == null || s.length() == 0 || k == 0) return 0;
        
        int[] charCount = new int[128]; // covers ASCII
        int distinctCount = 0;
        int windowStart = 0;
        int maxLength = 0;
        
        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) {
            
            // Expand: add right character
            char rightChar = s.charAt(windowEnd);
            if (charCount[rightChar] == 0) {
                distinctCount++; // new distinct character entering
            }
            charCount[rightChar]++;
            
            // Shrink: remove from left until valid
            while (distinctCount > k) {
                char leftChar = s.charAt(windowStart);
                charCount[leftChar]--;
                if (charCount[leftChar] == 0) {
                    distinctCount--; // character fully removed from window
                }
                windowStart++;
            }
            
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1);
        }
        
        return maxLength;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Return the actual substring (not just length)
    // ─────────────────────────────────────────────
    public static String longestSubstringKDistinctString(String s, int k) {
        
        if (s == null || s.length() == 0 || k == 0) return "";
        
        HashMap<Character, Integer> charFrequency = new HashMap<>();
        int windowStart = 0;
        int maxLength = 0;
        int maxStart = 0; // track WHERE the max window starts
        
        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) {
            
            char rightChar = s.charAt(windowEnd);
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);
            
            while (charFrequency.size() > k) {
                char leftChar = s.charAt(windowStart);
                charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);
                if (charFrequency.get(leftChar) == 0) {
                    charFrequency.remove(leftChar);
                }
                windowStart++;
            }
            
            // Track both length AND starting position
            int currentLength = windowEnd - windowStart + 1;
            if (currentLength > maxLength) {
                maxLength = currentLength;
                maxStart = windowStart;
            }
        }
        
        return s.substring(maxStart, maxStart + maxLength);
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void traceExecution(String s, int k) {
        
        System.out.println("  String: \"" + s + "\", K = " + k);
        System.out.println("  ─────────────────────────────────────────");
        
        if (s == null || s.length() == 0 || k == 0) {
            System.out.println("  Edge case → return 0");
            System.out.println();
            return;
        }
        
        HashMap<Character, Integer> charFrequency = new HashMap<>();
        int windowStart = 0;
        int maxLength = 0;
        
        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) {
            
            char rightChar = s.charAt(windowEnd);
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);
            
            // Show expansion
            String window = s.substring(windowStart, windowEnd + 1);
            System.out.println("  EXPAND → windowEnd=" + windowEnd 
                + " char='" + rightChar + "'"
                + " window=\"" + window + "\""
                + " map=" + charFrequency
                + " distinct=" + charFrequency.size());
            
            // Shrink if needed
            while (charFrequency.size() > k) {
                char leftChar = s.charAt(windowStart);
                charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);
                if (charFrequency.get(leftChar) == 0) {
                    charFrequency.remove(leftChar);
                }
                windowStart++;
                
                window = s.substring(windowStart, windowEnd + 1);
                System.out.println("  SHRINK → removed '" + leftChar + "'"
                    + " windowStart=" + windowStart
                    + " window=\"" + window + "\""
                    + " map=" + charFrequency
                    + " distinct=" + charFrequency.size());
            }
            
            int currentLength = windowEnd - windowStart + 1;
            maxLength = Math.max(maxLength, currentLength);
            System.out.println("  → valid window size=" + currentLength 
                + " maxLength=" + maxLength);
            System.out.println();
        }
        
        System.out.println("  ★ RESULT: " + maxLength);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 12: Longest Substring K Distinct Characters ║");
        System.out.println("║  Pattern: Sliding Window + HashMap Frequency Tracking║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example with trace ──
        System.out.println("═══ TEST 1: Classic Example (Traced) ═══");
        traceExecution("araaci", 2);
        
        // ── TEST 2: K=1 with trace ──
        System.out.println("═══ TEST 2: K=1 (Traced) ═══");
        traceExecution("araaci", 1);
        
        // ── TEST 3: K=3 with trace ──
        System.out.println("═══ TEST 3: K=3 (Traced) ═══");
        traceExecution("cbbebi", 3);
        
        // ── TEST 4: Entire string valid ──
        System.out.println("═══ TEST 4: Entire String Valid ═══");
        traceExecution("aaaa", 2);
        
        // ── TEST 5: Edge cases ──
        System.out.println("═══ TEST 5: Edge Cases ═══");
        System.out.println("  Empty string, k=2: " 
            + longestSubstringKDistinct("", 2));
        System.out.println("  \"abc\", k=0: " 
            + longestSubstringKDistinct("abc", 0));
        System.out.println("  \"a\", k=1: " 
            + longestSubstringKDistinct("a", 1));
        System.out.println("  \"a\", k=5: " 
            + longestSubstringKDistinct("a", 5));
        System.out.println("  \"abcdef\", k=6: " 
            + longestSubstringKDistinct("abcdef", 6));
        System.out.println("  \"abcdef\", k=10: " 
            + longestSubstringKDistinct("abcdef", 10));
        System.out.println();
        
        // ── TEST 6: Return actual substring ──
        System.out.println("═══ TEST 6: Return Actual Substring ═══");
        String[] testStrings = {"araaci", "cbbebi", "eceba", "aabbcc"};
        int[] testKs = {2, 3, 2, 2};
        for (int t = 0; t < testStrings.length; t++) {
            String result = longestSubstringKDistinctString(testStrings[t], testKs[t]);
            System.out.println("  \"" + testStrings[t] + "\" k=" + testKs[t] 
                + " → \"" + result + "\" (length=" + result.length() + ")");
        }
        System.out.println();
        
        // ── TEST 7: Exactly K distinct (variant) ──
        System.out.println("═══ TEST 7: Exactly K Distinct (Variant) ═══");
        System.out.println("  \"araaci\" exactly 2 distinct: " 
            + longestSubstringExactlyKDistinct("araaci", 2));
        System.out.println("  \"araaci\" exactly 1 distinct: " 
            + longestSubstringExactlyKDistinct("araaci", 1));
        System.out.println("  \"aabbcc\" exactly 2 distinct: " 
            + longestSubstringExactlyKDistinct("aabbcc", 2));
        System.out.println();
        
        // ── TEST 8: Array variant vs HashMap variant ──
        System.out.println("═══ TEST 8: Array vs HashMap Implementation ═══");
        String[] compareStrings = {"araaci", "cbbebi", "eceba", "abcabc"};
        int[] compareKs = {2, 3, 2, 2};
        boolean allMatch = true;
        for (int t = 0; t < compareStrings.length; t++) {
            int hashResult = longestSubstringKDistinct(compareStrings[t], compareKs[t]);
            int arrayResult = longestSubstringKDistinctArray(compareStrings[t], compareKs[t]);
            boolean match = hashResult == arrayResult;
            System.out.println("  \"" + compareStrings[t] + "\" k=" + compareKs[t]
                + " → HashMap=" + hashResult + " Array=" + arrayResult
                + " " + (match ? "✓" : "✗"));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();
        
        // ── TEST 9: Correctness verification against brute force ──
        System.out.println("═══ TEST 9: Correctness Verification ═══");
        String[] verifyStrings = {
            "araaci", "cbbebi", "eceba", "aaaa", "abcdef",
            "aabbccdd", "abaccc", "a", "ab", "aaabbb"
        };
        int[] verifyKs = {2, 3, 2, 2, 3, 2, 2, 1, 1, 1};
        boolean allPassed = true;
        for (int t = 0; t < verifyStrings.length; t++) {
            int optimal = longestSubstringKDistinct(verifyStrings[t], verifyKs[t]);
            int brute = longestSubstringKDistinctBrute(verifyStrings[t], verifyKs[t]);
            boolean pass = optimal == brute;
            System.out.println("  \"" + verifyStrings[t] + "\" k=" + verifyKs[t]
                + " → Optimal=" + optimal + " Brute=" + brute
                + " " + (pass ? "✓" : "✗"));
            if (!pass) allPassed = false;
        }
        System.out.println("  All passed: " + allPassed);
        System.out.println();
        
        // ── TEST 10: Performance benchmark ──
        System.out.println("═══ TEST 10: Performance Benchmark ═══");
        int size = 1_000_000;
        StringBuilder sb = new StringBuilder();
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            sb.append((char) ('a' + rand.nextInt(26)));
        }
        String largeString = sb.toString();
        
        // Sliding window
        long start = System.nanoTime();
        int swResult = longestSubstringKDistinct(largeString, 5);
        long swTime = System.nanoTime() - start;
        
        // Array-based
        start = System.nanoTime();
        int arrResult = longestSubstringKDistinctArray(largeString, 5);
        long arrTime = System.nanoTime() - start;
        
        System.out.println("  String length: " + size);
        System.out.println("  K = 5");
        System.out.println("  HashMap approach: result=" + swResult 
            + " time=" + (swTime / 1_000_000) + "ms");
        System.out.println("  Array approach:   result=" + arrResult 
            + " time=" + (arrTime / 1_000_000) + "ms");
        System.out.println("  Results match: " + (swResult == arrResult));
        System.out.println("  Array speedup: " 
            + (swTime / Math.max(arrTime, 1)) + "x");
        System.out.println();
    }
}