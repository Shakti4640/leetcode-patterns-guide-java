public class LongestRepeatingCharReplacement {

    // ─────────────────────────────────────────────
    // APPROACH B: Optimized — Single If, Stale maxFreq (Recommended)
    // ─────────────────────────────────────────────
    public static int characterReplacement(String s, int k) {

        if (s == null || s.length() == 0) return 0;

        int[] freq = new int[26]; // Frequency of each character in window
        int left = 0;
        int maxFreq = 0;         // Max frequency seen (monotonically non-decreasing)

        for (int right = 0; right < s.length(); right++) {

            // Step 1: Add right character to window
            int rightIdx = s.charAt(right) - 'A';
            freq[rightIdx]++;

            // Step 2: Update maxFreq (only increases, never decreases)
            maxFreq = Math.max(maxFreq, freq[rightIdx]);

            // Step 3: Check validity
            // windowSize - maxFreq = characters that need replacement
            // If this exceeds k → invalid → shrink by exactly 1
            int windowSize = right - left + 1;
            if (windowSize - maxFreq > k) {
                // Remove left character
                int leftIdx = s.charAt(left) - 'A';
                freq[leftIdx]--;
                left++;
                // Do NOT update maxFreq downward — let it stay stale
            }
        }

        // Window size at the end = the answer
        // Because window size is monotonically non-decreasing
        return s.length() - left;
    }

    // ─────────────────────────────────────────────
    // APPROACH A: Traditional — While Loop, Accurate maxFreq
    // ─────────────────────────────────────────────
    public static int characterReplacementTraditional(String s, int k) {

        if (s == null || s.length() == 0) return 0;

        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;
        int result = 0;

        for (int right = 0; right < s.length(); right++) {

            // Add right character
            int rightIdx = s.charAt(right) - 'A';
            freq[rightIdx]++;
            maxFreq = Math.max(maxFreq, freq[rightIdx]);

            // Shrink while invalid
            while ((right - left + 1) - maxFreq > k) {
                int leftIdx = s.charAt(left) - 'A';
                freq[leftIdx]--;
                left++;

                // Recompute maxFreq accurately (O(26) per shrink)
                maxFreq = 0;
                for (int f : freq) {
                    maxFreq = Math.max(maxFreq, f);
                }
            }

            // Window is now valid → track result
            result = Math.max(result, right - left + 1);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE (for correctness verification)
    // ─────────────────────────────────────────────
    public static int characterReplacementBrute(String s, int k) {

        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {

                // Count frequency of each char in s[i..j]
                int[] freq = new int[26];
                int maxF = 0;
                for (int x = i; x <= j; x++) {
                    freq[s.charAt(x) - 'A']++;
                    maxF = Math.max(maxF, freq[s.charAt(x) - 'A']);
                }

                int windowSize = j - i + 1;
                if (windowSize - maxF <= k) {
                    result = Math.max(result, windowSize);
                }
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION: Visualize the sliding window
    // ─────────────────────────────────────────────
    public static void characterReplacementWithTrace(String s, int k) {

        System.out.println("String: \"" + s + "\"  k=" + k);
        System.out.println("─────────────────────────────────────────────────");

        if (s == null || s.length() == 0) {
            System.out.println("Result: 0 (empty string)");
            return;
        }

        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;
        int bestSize = 0;
        int bestLeft = 0;
        int bestRight = 0;

        for (int right = 0; right < s.length(); right++) {

            char rightChar = s.charAt(right);
            int rightIdx = rightChar - 'A';
            freq[rightIdx]++;

            boolean maxUpdated = freq[rightIdx] > maxFreq;
            maxFreq = Math.max(maxFreq, freq[rightIdx]);

            int windowSize = right - left + 1;
            int cost = windowSize - maxFreq;
            boolean valid = cost <= k;

            System.out.print("right=" + right + " '" + rightChar + "'"
                    + "  window=[" + left + "," + right + "]"
                    + " \"" + s.substring(left, right + 1) + "\""
                    + "  maxFreq=" + maxFreq
                    + (maxUpdated ? "↑" : " ")
                    + "  cost=" + cost);

            if (!valid) {
                char leftChar = s.charAt(left);
                freq[leftChar - 'A']--;
                left++;
                System.out.println("  > k=" + k + " ✗ SHRINK (remove '" + leftChar + "')");
            } else {
                if (windowSize > bestSize) {
                    bestSize = windowSize;
                    bestLeft = left;
                    bestRight = right;
                }
                System.out.println("  ≤ k=" + k + " ✓"
                        + (windowSize == bestSize ? " ★ BEST=" + bestSize : ""));
            }
        }

        System.out.println();
        System.out.println("RESULT: " + bestSize
                + " → \"" + s.substring(bestLeft, bestRight + 1) + "\""
                + " (indices [" + bestLeft + "," + bestRight + "])");
    }

    // ─────────────────────────────────────────────
    // VARIANT: Max Consecutive Ones with at most K flips
    //   → Binary array [0,1,0,1,1,0,1]
    //   → Flip at most K zeros to ones
    //   → Find longest consecutive 1s
    //   → SAME algorithm: maxFreq = count of 1s in window
    // ─────────────────────────────────────────────
    public static int longestOnes(int[] nums, int k) {

        int left = 0;
        int onesCount = 0; // Equivalent to maxFreq for binary

        for (int right = 0; right < nums.length; right++) {

            // Count 1s in window (this IS the maxFreq for binary)
            if (nums[right] == 1) {
                onesCount++;
            }

            // windowSize - onesCount = number of 0s = flips needed
            int windowSize = right - left + 1;
            if (windowSize - onesCount > k) {
                // Invalid → shrink by 1
                if (nums[left] == 1) {
                    onesCount--;
                }
                left++;
            }
        }

        return nums.length - left;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Longest Substring with Same Char (k=0)
    //   → Simplified version: find longest run of same character
    //   → No replacements allowed
    // ─────────────────────────────────────────────
    public static int longestSameCharRun(String s) {

        if (s == null || s.length() == 0) return 0;

        int maxRun = 1;
        int currentRun = 1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                currentRun++;
                maxRun = Math.max(maxRun, currentRun);
            } else {
                currentRun = 1;
            }
        }

        return maxRun;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Character Replacement with Lowercase
    //   → Same algorithm, different offset
    // ─────────────────────────────────────────────
    public static int characterReplacementLower(String s, int k) {

        if (s == null || s.length() == 0) return 0;

        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;

        for (int right = 0; right < s.length(); right++) {
            int rightIdx = s.charAt(right) - 'a';  // Lowercase offset
            freq[rightIdx]++;
            maxFreq = Math.max(maxFreq, freq[rightIdx]);

            if ((right - left + 1) - maxFreq > k) {
                freq[s.charAt(left) - 'a']--;
                left++;
            }
        }

        return s.length() - left;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 34: Longest Repeating Character Replacement  ║");
        System.out.println("║  Pattern: Sliding Window + Max Frequency Trick        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic example with trace ──
        System.out.println("═══ TEST 1: Classic Example (Traced) ═══");
        characterReplacementWithTrace("AABABBA", 1);
        System.out.println();

        // ── TEST 2: Replace everything ──
        System.out.println("═══ TEST 2: k Large Enough for Entire String ═══");
        characterReplacementWithTrace("ABAB", 2);
        System.out.println();

        // ── TEST 3: Already uniform ──
        System.out.println("═══ TEST 3: Already All Same ═══");
        characterReplacementWithTrace("AAAA", 2);
        System.out.println();

        // ── TEST 4: All different ──
        System.out.println("═══ TEST 4: All Different Characters ═══");
        characterReplacementWithTrace("ABCDE", 2);
        System.out.println();

        // ── TEST 5: k = 0 ──
        System.out.println("═══ TEST 5: k=0 (No Replacements) ═══");
        characterReplacementWithTrace("AABBBCCC", 0);
        System.out.println();

        // ── TEST 6: Single character ──
        System.out.println("═══ TEST 6: Single Character ═══");
        characterReplacementWithTrace("A", 0);
        System.out.println();

        // ── TEST 7: Longer example ──
        System.out.println("═══ TEST 7: Longer Example ═══");
        characterReplacementWithTrace("ABAABAACDA", 2);
        System.out.println();

        // ── TEST 8: Correctness verification ──
        System.out.println("═══ TEST 8: Correctness Verification ═══");
        String[][] testCases = {
                {"AABABBA", "1"},
                {"ABAB", "2"},
                {"AAAA", "2"},
                {"ABCDE", "2"},
                {"A", "0"},
                {"AB", "1"},
                {"AABBBCCC", "0"},
                {"AABBBCCC", "2"},
                {"ABAABAACDA", "2"},
                {"KRSCDCSONAJNHLBMDQGIFCPEKPOHQIHLTDIQGEKLRLCQNBOHNDQGHJPNDQPERNFSSSRDEQLFPCCCARFMDLHADJADAGNNSBNCJQOF", "4"},
        };

        int passed = 0;
        for (String[] tc : testCases) {
            String str = tc[0];
            int kVal = Integer.parseInt(tc[1]);

            int approachA = characterReplacementTraditional(str, kVal);
            int approachB = characterReplacement(str, kVal);
            int brute = characterReplacementBrute(str, kVal);

            boolean correct = (approachA == brute) && (approachB == brute);

            System.out.println("  s=\"" + (str.length() > 20 ? str.substring(0, 20) + "..." : str) + "\""
                    + "  k=" + kVal
                    + "  → A=" + approachA
                    + "  B=" + approachB
                    + "  brute=" + brute
                    + (correct ? " ✓" : " ✗ MISMATCH"));

            if (correct) passed++;
        }
        System.out.println("  Result: " + passed + "/" + testCases.length + " passed");
        System.out.println();

        // ── TEST 9: Max Consecutive Ones variant ──
        System.out.println("═══ TEST 9: Max Consecutive Ones (K Flips) ═══");
        int[][] oneTests = {
                {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
        };
        int[] kVals = {2, 3};

        for (int i = 0; i < oneTests.length; i++) {
            int result = longestOnes(oneTests[i], kVals[i]);
            System.out.print("  Array: [");
            for (int j = 0; j < oneTests[i].length; j++) {
                System.out.print(oneTests[i][j]);
                if (j < oneTests[i].length - 1) System.out.print(",");
            }
            System.out.println("]  k=" + kVals[i] + "  → " + result);
        }
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        java.util.Random rand = new java.util.Random(42);
        int perfSize = 1_000_000;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < perfSize; i++) {
            sb.append((char) ('A' + rand.nextInt(26)));
        }
        String perfStr = sb.toString();
        int perfK = 1000;

        // Approach B (optimized)
        long start = System.nanoTime();
        int resultB = characterReplacement(perfStr, perfK);
        long timeB = System.nanoTime() - start;

        // Approach A (traditional)
        start = System.nanoTime();
        int resultA = characterReplacementTraditional(perfStr, perfK);
        long timeA = System.nanoTime() - start;

        System.out.println("  String length: " + String.format("%,d", perfSize)
                + "  k=" + perfK);
        System.out.println("  Approach A (while+recompute): "
                + String.format("%,d", timeA / 1000) + " μs  result=" + resultA);
        System.out.println("  Approach B (if+stale):        "
                + String.format("%,d", timeB / 1000) + " μs  result=" + resultB);
        System.out.println("  Speedup: "
                + String.format("%.1f", (double) timeA / Math.max(timeB, 1)) + "x");
        System.out.println("  Results match: " + (resultA == resultB ? "✓" : "✗"));
        System.out.println();

        // ── TEST 11: Approach B step count ──
        System.out.println("═══ TEST 11: Step Count Demonstration ═══");
        int[] sizes = {100, 1000, 10000, 100000, 1000000};
        for (int size : sizes) {
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb2.append((char) ('A' + rand.nextInt(4))); // Only 4 chars
            }
            String testStr = sb2.toString();

            // Count operations in Approach B
            int[] freqTest = new int[26];
            int leftTest = 0;
            int maxFreqTest = 0;
            int expandOps = 0;
            int shrinkOps = 0;

            for (int right = 0; right < testStr.length(); right++) {
                freqTest[testStr.charAt(right) - 'A']++;
                maxFreqTest = Math.max(maxFreqTest, freqTest[testStr.charAt(right) - 'A']);
                expandOps++;

                if ((right - leftTest + 1) - maxFreqTest > 5) {
                    freqTest[testStr.charAt(leftTest) - 'A']--;
                    leftTest++;
                    shrinkOps++;
                }
            }

            System.out.println("  n=" + String.format("%,8d", size)
                    + "  expands=" + String.format("%,8d", expandOps)
                    + "  shrinks=" + String.format("%,8d", shrinkOps)
                    + "  total=" + String.format("%,8d", expandOps + shrinkOps)
                    + "  ratio=" + String.format("%.2f", (double) (expandOps + shrinkOps) / size) + "n");
        }
    }
}