public class MaxSumSubarrayOfSizeK {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Fixed-Size Sliding Window
    // ─────────────────────────────────────────────
    public static int maxSumSubarray(int[] arr, int k) {

        // Edge case: invalid input
        if (arr == null || arr.length == 0 || k > arr.length || k <= 0) {
            return 0;
        }

        // Phase 1: BUILD the first window [0..K-1]
        int runningSum = 0;
        for (int i = 0; i < k; i++) {
            runningSum += arr[i];
        }
        int maxSum = runningSum;

        // Phase 2: SLIDE the window from index K to N-1
        for (int i = k; i < arr.length; i++) {
            runningSum += arr[i];       // entering element (new right edge)
            runningSum -= arr[i - k];   // leaving element (old left edge)
            maxSum = Math.max(maxSum, runningSum);
        }

        return maxSum;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Recalculate sum for each window
    // ─────────────────────────────────────────────
    public static int maxSumSubarrayBruteForce(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k > arr.length || k <= 0) {
            return 0;
        }

        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i <= arr.length - k; i++) {
            int windowSum = 0;
            for (int j = i; j < i + k; j++) {
                windowSum += arr[j];
            }
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }

    // ─────────────────────────────────────────────
    // SINGLE-LOOP VERSION (build and slide combined)
    // ─────────────────────────────────────────────
    public static int maxSumSubarraySingleLoop(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k > arr.length || k <= 0) {
            return 0;
        }

        int runningSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {

            // Always add the current element (entering)
            runningSum += arr[i];

            // Once we've built a full window, start tracking and sliding
            if (i >= k - 1) {
                maxSum = Math.max(maxSum, runningSum);
                runningSum -= arr[i - k + 1];  // remove the leftmost of this window
            }
        }

        return maxSum;
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Average of all subarrays of size K
    // ─────────────────────────────────────────────
    public static double[] averageOfSubarrays(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k > arr.length) {
            return new double[0];
        }

        double[] result = new double[arr.length - k + 1];
        int runningSum = 0;

        // Build first window
        for (int i = 0; i < k; i++) {
            runningSum += arr[i];
        }
        result[0] = (double) runningSum / k;

        // Slide
        for (int i = k; i < arr.length; i++) {
            runningSum += arr[i];
            runningSum -= arr[i - k];
            result[i - k + 1] = (double) runningSum / k;
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Maximum sum AND the starting index
    // ─────────────────────────────────────────────
    public static int[] maxSumWithIndex(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k > arr.length) {
            return new int[]{0, -1};
        }

        int runningSum = 0;
        for (int i = 0; i < k; i++) {
            runningSum += arr[i];
        }
        int maxSum = runningSum;
        int maxStart = 0;

        for (int i = k; i < arr.length; i++) {
            runningSum += arr[i];
            runningSum -= arr[i - k];

            if (runningSum > maxSum) {
                maxSum = runningSum;
                maxStart = i - k + 1;  // starting index of current window
            }
        }

        return new int[]{maxSum, maxStart};
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Count of windows with sum ≥ threshold
    // ─────────────────────────────────────────────
    public static int countWindowsAboveThreshold(int[] arr, int k, int threshold) {

        if (arr == null || arr.length == 0 || k > arr.length) {
            return 0;
        }

        int runningSum = 0;
        int count = 0;

        // Build first window
        for (int i = 0; i < k; i++) {
            runningSum += arr[i];
        }
        if (runningSum >= threshold) count++;

        // Slide
        for (int i = k; i < arr.length; i++) {
            runningSum += arr[i];
            runningSum -= arr[i - k];
            if (runningSum >= threshold) count++;
        }

        return count;
    }

    // ─────────────────────────────────────────────
    // VARIANT 4: Maximum of each window of size K
    // (Basic approach — O(NK). Optimal needs deque — future topic)
    // ─────────────────────────────────────────────
    public static int[] maxOfEachWindow(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];

        for (int i = 0; i <= arr.length - k; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = i; j < i + k; j++) {
                max = Math.max(max, arr[j]);
            }
            result[i] = max;
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the window slide
    // ─────────────────────────────────────────────
    public static void maxSumWithTrace(int[] arr, int k) {

        System.out.println("Input: " + arrayToString(arr) + "  K = " + k);
        System.out.println("─────────────────────────────────────────");

        if (arr.length < k || k <= 0) {
            System.out.println("Invalid: K > array length or K <= 0");
            System.out.println();
            return;
        }

        // Phase 1: Build first window
        int runningSum = 0;
        System.out.println("Phase 1: Building first window [0.." + (k - 1) + "]");
        for (int i = 0; i < k; i++) {
            runningSum += arr[i];
            System.out.println("  Add arr[" + i + "]=" + arr[i]
                + " → runningSum = " + runningSum);
        }
        int maxSum = runningSum;
        int maxStart = 0;
        System.out.println("  First window sum = " + runningSum + " → maxSum = " + maxSum);
        System.out.println();

        // Phase 2: Slide
        System.out.println("Phase 2: Sliding the window");
        int step = 1;
        for (int i = k; i < arr.length; i++) {

            int entering = arr[i];
            int leaving = arr[i - k];

            runningSum += entering;
            runningSum -= leaving;

            System.out.println("  Slide " + step + ":");
            System.out.println("    Window [" + (i - k + 1) + ".." + i + "]"
                + " → entering arr[" + i + "]=" + entering
                + ", leaving arr[" + (i - k) + "]=" + leaving);
            System.out.println("    runningSum = " + runningSum);

            if (runningSum > maxSum) {
                maxSum = runningSum;
                maxStart = i - k + 1;
                System.out.println("    → NEW MAX! maxSum = " + maxSum);
            } else {
                System.out.println("    → maxSum unchanged = " + maxSum);
            }

            step++;
        }

        System.out.println("─────────────────────────────────────────");
        System.out.println("Result: maxSum = " + maxSum
            + " at window [" + maxStart + ".." + (maxStart + k - 1) + "]");
        System.out.print("Winning window: [");
        for (int i = maxStart; i < maxStart + k; i++) {
            System.out.print(arr[i]);
            if (i < maxStart + k - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();
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
    // HELPER: Double array to string
    // ─────────────────────────────────────────────
    private static String doubleArrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(String.format("%.2f", arr[i]));
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 5: Maximum Sum Subarray of Size K       ║");
        System.out.println("║  Pattern: Sliding Window — Fixed Size            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example ═══");
        maxSumWithTrace(new int[]{2, 1, 5, 1, 3, 2}, 3);

        // ── TEST 2: Another example with trace ──
        System.out.println("═══ TEST 2: Another Example ═══");
        maxSumWithTrace(new int[]{2, 3, 4, 1, 5}, 2);

        // ── TEST 3: All same elements ──
        System.out.println("═══ TEST 3: All Same Elements ═══");
        maxSumWithTrace(new int[]{1, 1, 1, 1, 1}, 3);

        // ── TEST 4: Decreasing array ──
        System.out.println("═══ TEST 4: Decreasing Array ═══");
        maxSumWithTrace(new int[]{10, 8, 6, 4, 2}, 2);

        // ── TEST 5: Single element window ──
        System.out.println("═══ TEST 5: K = 1 (Single Element Window) ═══");
        maxSumWithTrace(new int[]{3, 7, 2, 9, 1}, 1);

        // ── TEST 6: Window equals array ──
        System.out.println("═══ TEST 6: K = N (Window Covers Entire Array) ═══");
        maxSumWithTrace(new int[]{1, 2, 3, 4}, 4);

        // ── TEST 7: With negative numbers ──
        System.out.println("═══ TEST 7: With Negative Numbers ═══");
        maxSumWithTrace(new int[]{-1, 4, -2, 5, -3, 6}, 3);

        // ── TEST 8: Average of subarrays ──
        System.out.println("═══ TEST 8: Average of All Subarrays (Variant 1) ═══");
        int[] arr8 = {1, 3, 2, 6, -1, 4, 1, 8, 2};
        int k8 = 5;
        double[] averages = averageOfSubarrays(arr8, k8);
        System.out.println("Input: " + arrayToString(arr8) + "  K = " + k8);
        System.out.println("Averages: " + doubleArrayToString(averages));
        System.out.println();

        // ── TEST 9: Max sum with index ──
        System.out.println("═══ TEST 9: Max Sum with Starting Index (Variant 2) ═══");
        int[] arr9 = {4, 2, 1, 7, 8, 1, 2, 8, 1, 0};
        int k9 = 3;
        int[] result9 = maxSumWithIndex(arr9, k9);
        System.out.println("Input: " + arrayToString(arr9) + "  K = " + k9);
        System.out.println("Max sum = " + result9[0]
            + " starting at index " + result9[1]);
        System.out.print("Window: [");
        for (int i = result9[1]; i < result9[1] + k9; i++) {
            System.out.print(arr9[i]);
            if (i < result9[1] + k9 - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        // ── TEST 10: Count windows above threshold ──
        System.out.println("═══ TEST 10: Windows Above Threshold (Variant 3) ═══");
        int[] arr10 = {2, 1, 5, 1, 3, 2};
        int k10 = 3;
        int threshold10 = 7;
        int count = countWindowsAboveThreshold(arr10, k10, threshold10);
        System.out.println("Input: " + arrayToString(arr10)
            + "  K = " + k10 + "  Threshold = " + threshold10);
        System.out.println("Windows with sum >= " + threshold10 + ": " + count);
        System.out.println();

        // ── TEST 11: Correctness verification ──
        System.out.println("═══ TEST 11: Correctness — All Approaches ═══");
        int[][] tests = {
            {2, 1, 5, 1, 3, 2},
            {10, 20, 30, 40, 50},
            {-1, -2, -3, -4, -5},
            {5, 1, 8, 2, 7, 3, 6},
            {100}
        };
        int[] kValues = {3, 2, 2, 4, 1};

        boolean allPassed = true;
        for (int t = 0; t < tests.length; t++) {
            int r1 = maxSumSubarray(tests[t], kValues[t]);
            int r2 = maxSumSubarrayBruteForce(tests[t], kValues[t]);
            int r3 = maxSumSubarraySingleLoop(tests[t], kValues[t]);
            boolean match = (r1 == r2) && (r2 == r3);
            System.out.println("  " + arrayToString(tests[t])
                + " K=" + kValues[t]
                + " → " + r1
                + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allPassed = false;
        }
        System.out.println("  Overall: "
            + (allPassed ? "ALL TESTS PASSED ✓" : "SOME FAILED ✗"));
        System.out.println();

        // ── TEST 12: Performance comparison ──
        System.out.println("═══ TEST 12: Performance Comparison ═══");
        int size = 5000000;
        int kPerf = 100000;
        int[] largeArr = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(1000);
        }

        // Sliding window
        long startTime = System.nanoTime();
        int res1 = maxSumSubarray(largeArr, kPerf);
        long slidingTime = System.nanoTime() - startTime;

        // Brute force (on smaller subset for fairness)
        int smallSize = 50000;
        int smallK = 1000;
        int[] smallArr = new int[smallSize];
        System.arraycopy(largeArr, 0, smallArr, 0, smallSize);

        startTime = System.nanoTime();
        int res2 = maxSumSubarrayBruteForce(smallArr, smallK);
        long bruteTime = System.nanoTime() - startTime;

        System.out.println("Sliding Window: N=" + size + " K=" + kPerf
            + " → " + (slidingTime / 1_000_000) + " ms (result: " + res1 + ")");
        System.out.println("Brute Force:    N=" + smallSize + " K=" + smallK
            + " → " + (bruteTime / 1_000_000) + " ms (result: " + res2 + ")");
        System.out.println("Note: Brute force on 100x SMALLER input yet slower!");
        System.out.println();
    }
}