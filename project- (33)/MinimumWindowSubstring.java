import java.util.HashMap;
import java.util.Map;

public class MinimumWindowSubstring {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Sliding Window + Match Counter (Array-based)
    // ─────────────────────────────────────────────
    public static String minWindow(String s, String t) {

        // Edge cases
        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        if (t.length() > s.length()) {
            return "";
        }

        // Step 1: Build target frequency map
        int[] targetFreq = new int[128]; // ASCII characters
        int required = 0;               // Distinct characters in t

        for (char c : t.toCharArray()) {
            if (targetFreq[c] == 0) {
                required++; // First occurrence → new distinct character
            }
            targetFreq[c]++;
        }

        // Step 2: Initialize window state
        int[] windowFreq = new int[128];
        int matchCount = 0;    // How many distinct chars are fully matched

        // Step 3: Initialize pointers and result trackers
        int left = 0;
        int minLength = Integer.MAX_VALUE;
        int minStart = 0;

        // Step 4: Expand right pointer
        for (int right = 0; right < s.length(); right++) {

            // Add right character to window
            char rightChar = s.charAt(right);
            windowFreq[rightChar]++;

            // Check if this character just became fully matched
            // CRITICAL: use == not >= to avoid over-counting
            if (targetFreq[rightChar] > 0
                    && windowFreq[rightChar] == targetFreq[rightChar]) {
                matchCount++;
            }

            // Step 5: Shrink while window is valid
            while (matchCount == required) {

                // Record current window if it's the smallest
                int windowSize = right - left + 1;
                if (windowSize < minLength) {
                    minLength = windowSize;
                    minStart = left;
                }

                // Remove left character from window
                char leftChar = s.charAt(left);
                windowFreq[leftChar]--;

                // Check if this character just became under-matched
                // CRITICAL: check AFTER decrementing, use strict <
                if (targetFreq[leftChar] > 0
                        && windowFreq[leftChar] < targetFreq[leftChar]) {
                    matchCount--;
                }

                left++; // Shrink window
            }
        }

        // Step 6: Return result
        if (minLength == Integer.MAX_VALUE) {
            return ""; // No valid window found
        }
        return s.substring(minStart, minStart + minLength);
    }

    // ─────────────────────────────────────────────
    // SOLUTION VARIANT: HashMap-based (for Unicode support)
    // ─────────────────────────────────────────────
    public static String minWindowHashMap(String s, String t) {

        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }

        // Build target frequency map
        Map<Character, Integer> targetFreq = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetFreq.put(c, targetFreq.getOrDefault(c, 0) + 1);
        }

        int required = targetFreq.size(); // Distinct characters in t
        Map<Character, Integer> windowFreq = new HashMap<>();
        int matchCount = 0;

        int left = 0;
        int minLength = Integer.MAX_VALUE;
        int minStart = 0;

        for (int right = 0; right < s.length(); right++) {

            char rightChar = s.charAt(right);
            windowFreq.put(rightChar, windowFreq.getOrDefault(rightChar, 0) + 1);

            // Check if this character just became fully matched
            if (targetFreq.containsKey(rightChar)
                    && windowFreq.get(rightChar).intValue()
                    == targetFreq.get(rightChar).intValue()) {
                matchCount++;
            }

            while (matchCount == required) {

                int windowSize = right - left + 1;
                if (windowSize < minLength) {
                    minLength = windowSize;
                    minStart = left;
                }

                char leftChar = s.charAt(left);
                windowFreq.put(leftChar, windowFreq.get(leftChar) - 1);

                if (targetFreq.containsKey(leftChar)
                        && windowFreq.get(leftChar) < targetFreq.get(leftChar)) {
                    matchCount--;
                }

                left++;
            }
        }

        return minLength == Integer.MAX_VALUE
                ? ""
                : s.substring(minStart, minStart + minLength);
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE (for correctness verification)
    // ─────────────────────────────────────────────
    public static String minWindowBruteForce(String s, String t) {

        int minLength = Integer.MAX_VALUE;
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                String sub = s.substring(i, j + 1);
                if (containsAll(sub, t) && sub.length() < minLength) {
                    minLength = sub.length();
                    result = sub;
                }
            }
        }

        return result;
    }

    private static boolean containsAll(String window, String target) {
        int[] freq = new int[128];
        for (char c : target.toCharArray()) freq[c]++;
        for (char c : window.toCharArray()) freq[c]--;
        for (int f : freq) {
            if (f > 0) return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION: Visualize the expand-shrink process
    // ─────────────────────────────────────────────
    public static void minWindowWithTrace(String s, String t) {

        System.out.println("Source: \"" + s + "\"");
        System.out.println("Target: \"" + t + "\"");
        System.out.println("─────────────────────────────────────────────────");

        if (s.length() == 0 || t.length() == 0 || t.length() > s.length()) {
            System.out.println("Result: \"\" (trivially impossible)");
            return;
        }

        // Build target map
        int[] targetFreq = new int[128];
        int required = 0;
        for (char c : t.toCharArray()) {
            if (targetFreq[c] == 0) required++;
            targetFreq[c]++;
        }

        System.out.print("Target frequencies: {");
        boolean first = true;
        for (int i = 0; i < 128; i++) {
            if (targetFreq[i] > 0) {
                if (!first) System.out.print(", ");
                System.out.print((char) i + ":" + targetFreq[i]);
                first = false;
            }
        }
        System.out.println("}  required=" + required);
        System.out.println();

        int[] windowFreq = new int[128];
        int matchCount = 0;
        int left = 0;
        int minLength = Integer.MAX_VALUE;
        int minStart = 0;
        int stepNum = 1;

        for (int right = 0; right < s.length(); right++) {

            char rightChar = s.charAt(right);
            windowFreq[rightChar]++;

            boolean matched = false;
            if (targetFreq[rightChar] > 0
                    && windowFreq[rightChar] == targetFreq[rightChar]) {
                matchCount++;
                matched = true;
            }

            System.out.println("EXPAND Step " + stepNum + ":"
                    + "  add '" + rightChar + "'"
                    + "  window=[" + left + "," + right + "]"
                    + " \"" + s.substring(left, right + 1) + "\""
                    + "  match=" + matchCount + "/" + required
                    + (matched ? "  ← '" + rightChar + "' MATCHED!" : ""));

            while (matchCount == required) {

                int windowSize = right - left + 1;
                boolean newBest = windowSize < minLength;
                if (newBest) {
                    minLength = windowSize;
                    minStart = left;
                }

                System.out.println("  SHRINK: window=[" + left + "," + right + "]"
                        + " \"" + s.substring(left, right + 1) + "\""
                        + " size=" + windowSize
                        + (newBest ? "  ★ NEW BEST!" : ""));

                char leftChar = s.charAt(left);
                windowFreq[leftChar]--;

                boolean unmatched = false;
                if (targetFreq[leftChar] > 0
                        && windowFreq[leftChar] < targetFreq[leftChar]) {
                    matchCount--;
                    unmatched = true;
                }

                System.out.println("         remove '" + leftChar + "'"
                        + "  match=" + matchCount + "/" + required
                        + (unmatched ? "  ← '" + leftChar + "' LOST!" : ""));

                left++;
            }

            stepNum++;
        }

        System.out.println();
        if (minLength == Integer.MAX_VALUE) {
            System.out.println("RESULT: \"\" (no valid window found)");
        } else {
            System.out.println("RESULT: \"" + s.substring(minStart, minStart + minLength) + "\""
                    + "  (start=" + minStart + ", length=" + minLength + ")");
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find All Anagrams in a String
    //   → Fixed-size window (size = t.length)
    //   → Find all starting indices where window is an anagram of t
    //   → Simplified version: window size is fixed
    // ─────────────────────────────────────────────
    public static java.util.List<Integer> findAllAnagrams(String s, String t) {

        java.util.List<Integer> result = new java.util.ArrayList<>();

        if (s.length() < t.length()) return result;

        int[] targetFreq = new int[128];
        int required = 0;
        for (char c : t.toCharArray()) {
            if (targetFreq[c] == 0) required++;
            targetFreq[c]++;
        }

        int[] windowFreq = new int[128];
        int matchCount = 0;
        int windowSize = t.length();

        for (int right = 0; right < s.length(); right++) {

            // Add right character
            char rightChar = s.charAt(right);
            windowFreq[rightChar]++;
            if (targetFreq[rightChar] > 0
                    && windowFreq[rightChar] == targetFreq[rightChar]) {
                matchCount++;
            }

            // Remove character that falls out of window
            if (right >= windowSize) {
                char leftChar = s.charAt(right - windowSize);
                if (targetFreq[leftChar] > 0
                        && windowFreq[leftChar] == targetFreq[leftChar]) {
                    matchCount--;
                }
                windowFreq[leftChar]--;
            }

            // Check if current window is an anagram
            if (matchCount == required) {
                result.add(right - windowSize + 1);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Check if Any Permutation of t Exists in s
    // ─────────────────────────────────────────────
    public static boolean checkInclusion(String t, String s) {
        return !findAllAnagrams(s, t).isEmpty();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 33: Minimum Window Substring                ║");
        System.out.println("║  Pattern: Sliding Window + Match Counter             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic example with trace ──
        System.out.println("═══ TEST 1: Classic Example (Traced) ═══");
        minWindowWithTrace("ADOBECODEBANC", "ABC");
        System.out.println();

        // ── TEST 2: Exact match ──
        System.out.println("═══ TEST 2: Exact Match ═══");
        minWindowWithTrace("a", "a");
        System.out.println();

        // ── TEST 3: Impossible case ──
        System.out.println("═══ TEST 3: Impossible Case ═══");
        minWindowWithTrace("a", "aa");
        System.out.println();

        // ── TEST 4: Duplicate characters in target ──
        System.out.println("═══ TEST 4: Duplicate Characters in Target ═══");
        minWindowWithTrace("ADOBECAODEBANC", "AAB");
        System.out.println();

        // ── TEST 5: Target at the very end ──
        System.out.println("═══ TEST 5: Target at the End ═══");
        minWindowWithTrace("XXXXXABC", "ABC");
        System.out.println();

        // ── TEST 6: Target at the very beginning ──
        System.out.println("═══ TEST 6: Target at the Beginning ═══");
        minWindowWithTrace("ABCXXXXX", "ABC");
        System.out.println();

        // ── TEST 7: Correctness verification against brute force ──
        System.out.println("═══ TEST 7: Correctness Verification ═══");
        String[][] testCases = {
                {"ADOBECODEBANC", "ABC"},
                {"a", "a"},
                {"a", "aa"},
                {"ab", "a"},
                {"ab", "b"},
                {"abc", "ac"},
                {"AABBCC", "ABC"},
                {"cabwefgewcwaefgcf", "cae"},
                {"bba", "ab"},
                {"aaaaaaaaaa", "aaaa"},
        };

        int passed = 0;
        for (String[] tc : testCases) {
            String optimal = minWindow(tc[0], tc[1]);
            String brute = minWindowBruteForce(tc[0], tc[1]);

            // Both should have same length (might be different substrings)
            boolean correct = optimal.length() == brute.length();
            // Also verify the optimal result actually contains all of t
            if (optimal.length() > 0) {
                correct = correct && containsAll(optimal, tc[1]);
            }

            System.out.println("  s=\"" + tc[0] + "\"  t=\"" + tc[1] + "\""
                    + "  → optimal=\"" + optimal + "\""
                    + "  brute=\"" + brute + "\""
                    + (correct ? " ✓" : " ✗ MISMATCH"));

            if (correct) passed++;
        }
        System.out.println("  Result: " + passed + "/" + testCases.length + " passed");
        System.out.println();

        // ── TEST 8: Find All Anagrams variant ──
        System.out.println("═══ TEST 8: Find All Anagrams ═══");
        String anagramS = "cbaebabacd";
        String anagramT = "abc";
        java.util.List<Integer> anagramResults = findAllAnagrams(anagramS, anagramT);
        System.out.println("  s=\"" + anagramS + "\"  t=\"" + anagramT + "\"");
        System.out.print("  Anagram positions: ");
        for (int idx : anagramResults) {
            System.out.print(idx + " (\"" + anagramS.substring(idx, idx + anagramT.length()) + "\")  ");
        }
        System.out.println();
        System.out.println();

        // ── TEST 9: Check Inclusion variant ──
        System.out.println("═══ TEST 9: Check Permutation Inclusion ═══");
        System.out.println("  \"eidbaooo\" contains perm of \"ab\"? "
                + checkInclusion("ab", "eidbaooo"));
        System.out.println("  \"eidboaoo\" contains perm of \"ab\"? "
                + checkInclusion("ab", "eidboaoo"));
        System.out.println();

        // ── TEST 10: Performance test ──
        System.out.println("═══ TEST 10: Performance Test ═══");
        int perfSize = 1_000_000;
        StringBuilder sb = new StringBuilder();
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < perfSize; i++) {
            sb.append((char) ('A' + rand.nextInt(26)));
        }
        String perfS = sb.toString();
        String perfT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // All 26 letters

        long startTime = System.nanoTime();
        String perfResult = minWindow(perfS, perfT);
        long elapsed = System.nanoTime() - startTime;

        System.out.println("  Source length: " + String.format("%,d", perfSize));
        System.out.println("  Target: all 26 letters");
        System.out.println("  Result length: " + perfResult.length());
        System.out.println("  Time: " + String.format("%,d", elapsed / 1000) + " μs");
        System.out.println();

        // ── TEST 11: HashMap vs Array performance ──
        System.out.println("═══ TEST 11: Array vs HashMap Performance ═══");
        // Array-based
        startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            minWindow(perfS, perfT);
        }
        long arrayTime = (System.nanoTime() - startTime) / 10;

        // HashMap-based
        startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            minWindowHashMap(perfS, perfT);
        }
        long hashMapTime = (System.nanoTime() - startTime) / 10;

        System.out.println("  Array-based:   " + String.format("%,d", arrayTime / 1000) + " μs (avg)");
        System.out.println("  HashMap-based: " + String.format("%,d", hashMapTime / 1000) + " μs (avg)");
        System.out.println("  Array speedup: "
                + String.format("%.1f", (double) hashMapTime / arrayTime) + "x");
    }
}