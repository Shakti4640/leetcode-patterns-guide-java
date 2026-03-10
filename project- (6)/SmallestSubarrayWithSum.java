public class SmallestSubarrayWithSum {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Variable-Size Sliding Window
    // ─────────────────────────────────────────────
    public static int minSubarrayLen(int target, int[] nums) {

        // Edge case
        if (nums == null || nums.length == 0) return 0;

        int left = 0;
        int runningSum = 0;
        int minLength = Integer.MAX_VALUE;

        // Right pointer expands the window (eager — always moves forward)
        for (int right = 0; right < nums.length; right++) {

            // EXPAND: add the entering element
            runningSum += nums[right];

            // SHRINK: while window is valid, try to minimize
            while (runningSum >= target) {

                // Record current valid window length
                minLength = Math.min(minLength, right - left + 1);

                // Remove the leftmost element and advance left pointer
                runningSum -= nums[left];
                left++;
            }
        }

        // If no valid window was found, return 0
        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Check every subarray
    // ─────────────────────────────────────────────
    public static int minSubarrayLenBruteForce(int target, int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        int minLength = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum >= target) {
                    minLength = Math.min(minLength, j - i + 1);
                    break;  // no need to extend further from this start
                }
            }
        }

        return minLength == Integer.MAX_VALUE ? 0 : minLength;
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Longest subarray with sum ≤ target
    // (Reverse condition — shrink when EXCEEDED)
    // ─────────────────────────────────────────────
    public static int longestSubarrayWithSumAtMost(int[] nums, int target) {

        if (nums == null || nums.length == 0) return 0;

        int left = 0;
        int runningSum = 0;
        int maxLength = 0;

        for (int right = 0; right < nums.length; right++) {

            // EXPAND
            runningSum += nums[right];

            // SHRINK when condition is VIOLATED (sum > target)
            while (runningSum > target && left <= right) {
                runningSum -= nums[left];
                left++;
            }

            // Window is now valid (sum ≤ target) → record its length
            if (runningSum <= target) {
                maxLength = Math.max(maxLength, right - left + 1);
            }
        }

        return maxLength;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Smallest subarray with sum ≥ target
    // AND return the actual subarray
    // ─────────────────────────────────────────────
    public static int[] minSubarrayWithElements(int target, int[] nums) {

        if (nums == null || nums.length == 0) return new int[0];

        int left = 0;
        int runningSum = 0;
        int minLength = Integer.MAX_VALUE;
        int bestLeft = 0;
        int bestRight = 0;

        for (int right = 0; right < nums.length; right++) {

            runningSum += nums[right];

            while (runningSum >= target) {
                int currentLength = right - left + 1;
                if (currentLength < minLength) {
                    minLength = currentLength;
                    bestLeft = left;
                    bestRight = right;
                }
                runningSum -= nums[left];
                left++;
            }
        }

        if (minLength == Integer.MAX_VALUE) return new int[0];

        int[] result = new int[bestRight - bestLeft + 1];
        for (int i = bestLeft; i <= bestRight; i++) {
            result[i - bestLeft] = nums[i];
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Count of subarrays with sum ≥ target
    // ─────────────────────────────────────────────
    public static int countSubarraysWithSumAtLeast(int[] nums, int target) {

        if (nums == null || nums.length == 0) return 0;

        int left = 0;
        int runningSum = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {

            runningSum += nums[right];

            // Shrink to find the LARGEST left where sum ≥ target
            while (runningSum >= target && left <= right) {
                // All subarrays from left to right, left to right+1, ... are valid
                // But we count those ending at right
                // If [left..right] is valid, then [left..right], [left-1..right], etc. too
                // Actually: if [left..right] is valid with sum ≥ target
                // Then all windows [left..right], [left..right+1], ... are valid
                // For counting ALL valid subarrays: different approach needed
                runningSum -= nums[left];
                left++;
            }

            // All subarrays ending at 'right' starting from 0 to left-1 have sum ≥ target
            count += left;
        }

        return count;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See expand and shrink happen
    // ─────────────────────────────────────────────
    public static void minSubarrayWithTrace(int target, int[] nums) {

        System.out.println("Input: " + arrayToString(nums) + "  Target = " + target);
        System.out.println("─────────────────────────────────────────────");

        if (nums == null || nums.length == 0) {
            System.out.println("Empty array → return 0");
            System.out.println();
            return;
        }

        int left = 0;
        int runningSum = 0;
        int minLength = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {

            runningSum += nums[right];

            System.out.println("EXPAND: right=" + right
                + " → add " + nums[right]
                + " → sum=" + runningSum
                + " | window [" + left + ".." + right + "]"
                + " = " + windowToString(nums, left, right));

            if (runningSum < target) {
                System.out.println("  sum(" + runningSum + ") < target("
                    + target + ") → keep expanding");
            }

            while (runningSum >= target) {

                int currentLength = right - left + 1;
                boolean isNewMin = currentLength < minLength;
                minLength = Math.min(minLength, currentLength);

                System.out.println("  SHRINK: sum(" + runningSum + ") >= target("
                    + target + ")"
                    + " → window [" + left + ".." + right + "]"
                    + " length=" + currentLength
                    + (isNewMin ? " → NEW MIN!" : "")
                    + " | remove arr[" + left + "]=" + nums[left]);

                runningSum -= nums[left];
                left++;

                if (runningSum < target) {
                    System.out.println("  sum(" + runningSum + ") < target("
                        + target + ") → stop shrinking");
                }
            }
        }

        System.out.println("─────────────────────────────────────────────");
        if (minLength == Integer.MAX_VALUE) {
            System.out.println("Result: 0 (no valid subarray found)");
        } else {
            System.out.println("Result: minLength = " + minLength);
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // HELPER: Window contents as string
    // ─────────────────────────────────────────────
    private static String windowToString(int[] arr, int from, int to) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = from; i <= to; i++) {
            sb.append(arr[i]);
            if (i < to) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // HELPER: Array to string
    // ─────────────────────────────────────────────
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 6: Smallest Subarray with Sum ≥ Target       ║");
        System.out.println("║  Pattern: Sliding Window — Variable Size + Shrink     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example ═══");
        minSubarrayWithTrace(7, new int[]{2, 1, 5, 2, 3, 2});

        // ── TEST 2: Single element satisfies ──
        System.out.println("═══ TEST 2: Single Element Satisfies ═══");
        minSubarrayWithTrace(7, new int[]{2, 1, 5, 2, 8});

        // ── TEST 3: Multiple minimum windows ──
        System.out.println("═══ TEST 3: Multiple Minimum Windows ═══");
        minSubarrayWithTrace(8, new int[]{3, 4, 1, 1, 6});

        // ── TEST 4: No valid window ──
        System.out.println("═══ TEST 4: No Valid Window ═══");
        minSubarrayWithTrace(100, new int[]{1, 1, 1, 1, 1});

        // ── TEST 5: Entire array needed ──
        System.out.println("═══ TEST 5: Entire Array Needed ═══");
        minSubarrayWithTrace(15, new int[]{1, 2, 3, 4, 5});

        // ── TEST 6: Large element at end ──
        System.out.println("═══ TEST 6: Large Element Triggers Multiple Shrinks ═══");
        minSubarrayWithTrace(7, new int[]{1, 1, 1, 1, 10});

        // ── TEST 7: Return actual subarray variant ──
        System.out.println("═══ TEST 7: Variant — Return Actual Subarray ═══");
        int[] arr7 = {2, 1, 5, 2, 3, 2};
        int target7 = 7;
        int[] subarray = minSubarrayWithElements(target7, arr7);
        System.out.println("Input: " + arrayToString(arr7) + "  Target = " + target7);
        System.out.println("Smallest subarray: " + arrayToString(subarray));
        int sum7 = 0;
        for (int v : subarray) sum7 += v;
        System.out.println("Sum: " + sum7);
        System.out.println();

        // ── TEST 8: Longest subarray with sum ≤ target ──
        System.out.println("═══ TEST 8: Variant — Longest Subarray with Sum ≤ Target ═══");
        int[] arr8 = {3, 1, 2, 1, 4, 1, 1, 2};
        int target8 = 5;
        int longest = longestSubarrayWithSumAtMost(arr8, target8);
        System.out.println("Input: " + arrayToString(arr8) + "  Target = " + target8);
        System.out.println("Longest subarray with sum ≤ " + target8 + ": " + longest);
        System.out.println();

        // ── TEST 9: Count subarrays with sum ≥ target ──
        System.out.println("═══ TEST 9: Variant — Count Subarrays with Sum ≥ Target ═══");
        int[] arr9 = {2, 1, 5, 2, 3, 2};
        int target9 = 7;
        int countResult = countSubarraysWithSumAtLeast(arr9, target9);
        System.out.println("Input: " + arrayToString(arr9) + "  Target = " + target9);
        System.out.println("Count of subarrays with sum >= " + target9 + ": " + countResult);
        System.out.println();

        // ── TEST 10: Correctness verification ──
        System.out.println("═══ TEST 10: Correctness — Sliding Window vs Brute Force ═══");
        int[][] testArrays = {
            {2, 1, 5, 2, 3, 2},
            {2, 1, 5, 2, 8},
            {3, 4, 1, 1, 6},
            {1, 1, 1, 1, 1},
            {1, 2, 3, 4, 5},
            {1, 1, 1, 1, 10},
            {10},
            {1, 4, 4},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        };
        int[] targets = {7, 7, 8, 100, 15, 7, 5, 4, 15};

        boolean allPassed = true;
        for (int t = 0; t < testArrays.length; t++) {
            int r1 = minSubarrayLen(targets[t], testArrays[t]);
            int r2 = minSubarrayLenBruteForce(targets[t], testArrays[t]);
            boolean match = (r1 == r2);
            System.out.println("  " + arrayToString(testArrays[t])
                + " target=" + targets[t]
                + " → sliding=" + r1
                + " brute=" + r2
                + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allPassed = false;
        }
        System.out.println("  Overall: "
            + (allPassed ? "ALL TESTS PASSED ✓" : "SOME FAILED ✗"));
        System.out.println();

        // ── TEST 11: Performance comparison ──
        System.out.println("═══ TEST 11: Performance Comparison ═══");
        int size = 5000000;
        int[] largeArr = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(100) + 1;  // 1 to 100
        }
        int largeTarget = 500;

        // Sliding window on full array
        long startTime = System.nanoTime();
        int res1 = minSubarrayLen(largeTarget, largeArr);
        long slidingTime = System.nanoTime() - startTime;

        // Brute force on smaller subset
        int smallSize = 50000;
        int[] smallArr = new int[smallSize];
        System.arraycopy(largeArr, 0, smallArr, 0, smallSize);

        startTime = System.nanoTime();
        int res2 = minSubarrayLenBruteForce(largeTarget, smallArr);
        long bruteTime = System.nanoTime() - startTime;

        System.out.println("Sliding Window: N=" + size
            + " → " + (slidingTime / 1_000_000) + " ms (result: " + res1 + ")");
        System.out.println("Brute Force:    N=" + smallSize
            + " → " + (bruteTime / 1_000_000) + " ms (result: " + res2 + ")");
        System.out.println("Sliding window processed " + (size / smallSize)
            + "x more data yet likely faster!");
        System.out.println();
    }
}