public class SquaresOfSortedArray {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Two Pointers — Merge from Both Ends
    // ─────────────────────────────────────────────
    public static int[] sortedSquares(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];

        // Two read pointers at opposite ends (Project 1 convergence)
        int left = 0;
        int right = n - 1;

        // Write pointer at the back of result (filling largest first)
        int write = n - 1;

        // Converge inward — left <= right to include the final element
        while (left <= right) {

            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];

            if (leftSquare >= rightSquare) {
                // Left end has larger (or equal) magnitude
                result[write] = leftSquare;
                left++;
            } else {
                // Right end has larger magnitude
                result[write] = rightSquare;
                right--;
            }

            // Write pointer always decrements (one element placed per step)
            write--;
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Square then Sort
    // ─────────────────────────────────────────────
    public static int[] sortedSquaresBruteForce(int[] nums) {

        int[] result = new int[nums.length];

        // Step 1: Square every element
        for (int i = 0; i < nums.length; i++) {
            result[i] = nums[i] * nums[i];
        }

        // Step 2: Sort the result
        java.util.Arrays.sort(result);

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Using absolute value comparison
    // (functionally identical — different style)
    // ─────────────────────────────────────────────
    public static int[] sortedSquaresAbsVersion(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];

        int left = 0;
        int right = n - 1;
        int write = n - 1;

        while (left <= right) {

            // Compare absolute values instead of squares
            int leftAbs = Math.abs(nums[left]);
            int rightAbs = Math.abs(nums[right]);

            if (leftAbs >= rightAbs) {
                result[write] = leftAbs * leftAbs;
                left++;
            } else {
                result[write] = rightAbs * rightAbs;
                right--;
            }

            write--;
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Fill from the FRONT (smallest first)
    // → Find the "valley" (smallest abs value) first
    // → Expand outward from valley
    // ─────────────────────────────────────────────
    public static int[] sortedSquaresFromFront(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];

        // Step 1: Find the index closest to zero (valley of V-shape)
        int minAbsIdx = 0;
        for (int i = 1; i < n; i++) {
            if (Math.abs(nums[i]) < Math.abs(nums[minAbsIdx])) {
                minAbsIdx = i;
            }
        }

        // Step 2: Expand outward from valley
        int left = minAbsIdx;
        int right = minAbsIdx + 1;
        int write = 0;

        while (left >= 0 && right < n) {
            int leftSq = nums[left] * nums[left];
            int rightSq = nums[right] * nums[right];

            if (leftSq <= rightSq) {
                result[write] = leftSq;
                left--;
            } else {
                result[write] = rightSq;
                right++;
            }
            write++;
        }

        // Remaining elements on the left
        while (left >= 0) {
            result[write] = nums[left] * nums[left];
            left--;
            write++;
        }

        // Remaining elements on the right
        while (right < n) {
            result[write] = nums[right] * nums[right];
            right++;
            write++;
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the merge happen
    // ─────────────────────────────────────────────
    public static void sortedSquaresWithTrace(int[] nums) {

        System.out.println("Input: " + arrayToString(nums));
        System.out.println("─────────────────────────────────────");

        int n = nums.length;
        int[] result = new int[n];

        int left = 0;
        int right = n - 1;
        int write = n - 1;
        int step = 1;

        while (left <= right) {

            int leftSquare = nums[left] * nums[left];
            int rightSquare = nums[right] * nums[right];

            System.out.println("Step " + step + ":");
            System.out.println("  left=" + left
                + " (val=" + nums[left]
                + ", sq=" + leftSquare + ")"
                + "  right=" + right
                + " (val=" + nums[right]
                + ", sq=" + rightSquare + ")"
                + "  write=" + write);

            if (leftSquare >= rightSquare) {
                result[write] = leftSquare;
                System.out.println("  → Left wins (" + leftSquare
                    + " >= " + rightSquare + ")"
                    + " → result[" + write + "] = " + leftSquare
                    + " → left++");
                left++;
            } else {
                result[write] = rightSquare;
                System.out.println("  → Right wins (" + rightSquare
                    + " > " + leftSquare + ")"
                    + " → result[" + write + "] = " + rightSquare
                    + " → right--");
                right--;
            }

            write--;
            System.out.println("  → Result so far: " + arrayToString(result));
            step++;
        }

        System.out.println("─────────────────────────────────────");
        System.out.println("Final result: " + arrayToString(result));
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
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 4: Squares of a Sorted Array             ║");
        System.out.println("║  Pattern: Two Pointers — Merge from Both Ends     ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Mixed negatives and positives ──
        System.out.println("═══ TEST 1: Mixed Negatives and Positives ═══");
        sortedSquaresWithTrace(new int[]{-4, -1, 0, 3, 10});

        // ── TEST 2: Another mixed example ──
        System.out.println("═══ TEST 2: Another Mixed Example ═══");
        sortedSquaresWithTrace(new int[]{-7, -3, 2, 3, 11});

        // ── TEST 3: All negatives ──
        System.out.println("═══ TEST 3: All Negatives ═══");
        sortedSquaresWithTrace(new int[]{-5, -4, -3, -2, -1});

        // ── TEST 4: All positives ──
        System.out.println("═══ TEST 4: All Positives ═══");
        sortedSquaresWithTrace(new int[]{1, 2, 3, 4, 5});

        // ── TEST 5: Single element ──
        System.out.println("═══ TEST 5: Single Element ═══");
        sortedSquaresWithTrace(new int[]{-3});

        // ── TEST 6: Two elements ──
        System.out.println("═══ TEST 6: Two Elements ═══");
        sortedSquaresWithTrace(new int[]{-3, 2});

        // ── TEST 7: Zeros ──
        System.out.println("═══ TEST 7: With Zeros ═══");
        sortedSquaresWithTrace(new int[]{-2, 0, 0, 0, 3});

        // ── TEST 8: Equal absolute values ──
        System.out.println("═══ TEST 8: Equal Absolute Values ═══");
        sortedSquaresWithTrace(new int[]{-3, -2, 2, 3});

        // ── TEST 9: Correctness verification across all approaches ──
        System.out.println("═══ TEST 9: Correctness Verification ═══");
        int[][] testCases = {
            {-10, -5, -3, 0, 1, 4, 8},
            {-100, -50, 0, 50, 100},
            {-1},
            {0, 0, 0},
            {-6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6}
        };

        boolean allPassed = true;
        for (int[] test : testCases) {
            int[] r1 = sortedSquares(test.clone());
            int[] r2 = sortedSquaresBruteForce(test.clone());
            int[] r3 = sortedSquaresAbsVersion(test.clone());
            int[] r4 = sortedSquaresFromFront(test.clone());

            boolean match = java.util.Arrays.equals(r1, r2)
                         && java.util.Arrays.equals(r2, r3)
                         && java.util.Arrays.equals(r3, r4);

            System.out.println("  Input: " + arrayToString(test)
                + " → " + (match ? "✓ ALL MATCH" : "✗ MISMATCH"));

            if (!match) allPassed = false;
        }
        System.out.println("  Overall: " + (allPassed ? "ALL TESTS PASSED ✓" : "SOME TESTS FAILED ✗"));
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        int size = 5000000;
        int[] largeArr = new int[size];
        // Half negative, half positive, sorted
        for (int i = 0; i < size; i++) {
            largeArr[i] = i - size / 2;  // range: [-2.5M, +2.5M]
        }

        // Two pointers approach
        int[] copy1 = largeArr.clone();
        long startTime = System.nanoTime();
        int[] res1 = sortedSquares(copy1);
        long twoPointerTime = System.nanoTime() - startTime;

        // Brute force approach
        int[] copy2 = largeArr.clone();
        startTime = System.nanoTime();
        int[] res2 = sortedSquaresBruteForce(copy2);
        long bruteTime = System.nanoTime() - startTime;

        System.out.println("Array size: " + size);
        System.out.println("Two Pointers: " + (twoPointerTime / 1_000_000) + " ms");
        System.out.println("Brute Force:  " + (bruteTime / 1_000_000) + " ms");
        System.out.println("Results match: " + java.util.Arrays.equals(res1, res2));
        if (twoPointerTime > 0) {
            System.out.println("Speedup: ~" + (bruteTime / Math.max(twoPointerTime, 1)) + "x");
        }
        System.out.println();
    }
}