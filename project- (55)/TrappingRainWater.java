import java.util.ArrayDeque;
import java.util.Deque;

public class TrappingRainWater {

    // ─────────────────────────────────────────────────────────────
    // APPROACH A: BRUTE FORCE — O(n²) time, O(1) space
    // For each position, scan left and right to find max heights
    // ─────────────────────────────────────────────────────────────
    public static int trapBruteForce(int[] height) {
        int n = height.length;
        if (n <= 2) return 0;

        int totalWater = 0;

        // Skip first and last — they cannot hold water (no wall on one side)
        for (int i = 1; i < n - 1; i++) {

            // Scan LEFT to find tallest bar
            int maxLeft = 0;
            for (int j = 0; j <= i; j++) {
                maxLeft = Math.max(maxLeft, height[j]);
            }

            // Scan RIGHT to find tallest bar
            int maxRight = 0;
            for (int j = i; j < n; j++) {
                maxRight = Math.max(maxRight, height[j]);
            }

            // Water at this position
            int waterHere = Math.min(maxLeft, maxRight) - height[i];
            totalWater += waterHere;
        }

        return totalWater;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH B: PREFIX MAX ARRAYS — O(n) time, O(n) space
    // Precompute maxLeft[] and maxRight[], then compute water
    // ─────────────────────────────────────────────────────────────
    public static int trapPrefixArrays(int[] height) {
        int n = height.length;
        if (n <= 2) return 0;

        // Step 1: Build maxLeft array
        // maxLeft[i] = maximum height from index 0 to i (inclusive)
        int[] maxLeft = new int[n];
        maxLeft[0] = height[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], height[i]);
        }

        // Step 2: Build maxRight array
        // maxRight[i] = maximum height from index i to n-1 (inclusive)
        int[] maxRight = new int[n];
        maxRight[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], height[i]);
        }

        // Step 3: Compute water at each position
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(maxLeft[i], maxRight[i]);
            totalWater += waterLevel - height[i];
        }

        return totalWater;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH C: MONOTONIC STACK — O(n) time, O(n) space
    // Decreasing stack → pop on taller bar → compute water layer
    // ─────────────────────────────────────────────────────────────
    public static int trapMonotonicStack(int[] height) {
        int n = height.length;
        if (n <= 2) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        int totalWater = 0;

        for (int i = 0; i < n; i++) {
            // While current bar is TALLER than stack top → valley found
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                // Pop the valley bottom
                int bottomIdx    = stack.pop();
                int bottomHeight = height[bottomIdx];

                // If stack is empty after pop → no left wall → no water
                if (stack.isEmpty()) break;

                // Left wall is the new stack top
                int leftIdx    = stack.peek();
                int leftHeight = height[leftIdx];

                // Right wall is current bar
                int rightHeight = height[i];

                // Water layer dimensions
                int waterHeight = Math.min(leftHeight, rightHeight) - bottomHeight;
                int width       = i - leftIdx - 1;

                // Add this layer of water
                totalWater += waterHeight * width;
            }

            // Push current index (maintains decreasing order)
            stack.push(i);
        }

        return totalWater;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH D: TWO POINTERS — O(n) time, O(1) space — OPTIMAL
    // Converge from both ends, process the side with smaller max
    // ─────────────────────────────────────────────────────────────
    public static int trapTwoPointers(int[] height) {
        int n = height.length;
        if (n <= 2) return 0;

        int left  = 0;
        int right = n - 1;
        int maxLeft  = 0;
        int maxRight = 0;
        int totalWater = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                // Right side has a bar ≥ height[right] > height[left]
                // So water at left is bounded by maxLeft
                maxLeft = Math.max(maxLeft, height[left]);
                totalWater += maxLeft - height[left];
                left++;
            } else {
                // Left side has a bar ≥ height[left] ≥ height[right]
                // So water at right is bounded by maxRight
                maxRight = Math.max(maxRight, height[right]);
                totalWater += maxRight - height[right];
                right--;
            }
        }

        return totalWater;
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE: PREFIX ARRAYS — Show the computation table
    // ─────────────────────────────────────────────────────────────
    public static void tracePrefixArrays(int[] height) {
        int n = height.length;

        int[] maxLeft = new int[n];
        maxLeft[0] = height[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], height[i]);
        }

        int[] maxRight = new int[n];
        maxRight[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], height[i]);
        }

        System.out.println("┌───────┬────────┬─────────┬──────────┬────────────┬───────┐");
        System.out.println("│ Index │ Height │ MaxLeft │ MaxRight │ WaterLevel │ Water │");
        System.out.println("├───────┼────────┼─────────┼──────────┼────────────┼───────┤");

        int total = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(maxLeft[i], maxRight[i]);
            int water      = waterLevel - height[i];
            total += water;
            System.out.printf("│  %3d  │  %4d  │  %4d   │   %4d   │    %4d    │  %3d  │%n",
                i, height[i], maxLeft[i], maxRight[i], waterLevel, water);
        }

        System.out.println("└───────┴────────┴─────────┴──────────┴────────────┴───────┘");
        System.out.println("Total Water: " + total);
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE: TWO POINTERS — Show each step
    // ─────────────────────────────────────────────────────────────
    public static void traceTwoPointers(int[] height) {
        int n = height.length;
        int left  = 0;
        int right = n - 1;
        int maxLeft  = 0;
        int maxRight = 0;
        int totalWater = 0;
        int step = 1;

        System.out.println("Heights:");
        System.out.print("  [");
        for (int i = 0; i < n; i++) {
            System.out.print(height[i]);
            if (i < n - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("────────────────────────────────────────────");

        while (left < right) {
            System.out.print("Step " + step + ": L=" + left
                + "(h=" + height[left] + ") R=" + right
                + "(h=" + height[right] + ")");

            if (height[left] < height[right]) {
                maxLeft = Math.max(maxLeft, height[left]);
                int water = maxLeft - height[left];
                totalWater += water;
                System.out.println(" → process LEFT"
                    + "  maxL=" + maxLeft
                    + "  water=" + water
                    + "  total=" + totalWater);
                left++;
            } else {
                maxRight = Math.max(maxRight, height[right]);
                int water = maxRight - height[right];
                totalWater += water;
                System.out.println(" → process RIGHT"
                    + "  maxR=" + maxRight
                    + "  water=" + water
                    + "  total=" + totalWater);
                right--;
            }
            step++;
        }

        System.out.println("────────────────────────────────────────────");
        System.out.println("Total Water: " + totalWater);
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE: MONOTONIC STACK — Show each pop event
    // ─────────────────────────────────────────────────────────────
    public static void traceMonotonicStack(int[] height) {
        int n = height.length;

        System.out.println("Heights:");
        System.out.print("  [");
        for (int i = 0; i < n; i++) {
            System.out.print(height[i]);
            if (i < n - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("────────────────────────────────────────────");

        Deque<Integer> stack = new ArrayDeque<>();
        int totalWater = 0;

        for (int i = 0; i < n; i++) {
            System.out.println("i=" + i + " h=" + height[i] + ":");

            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int bottomIdx    = stack.pop();
                int bottomHeight = height[bottomIdx];

                if (stack.isEmpty()) {
                    System.out.println("  POP idx=" + bottomIdx
                        + " h=" + bottomHeight
                        + " → stack empty → no left wall → skip");
                    break;
                }

                int leftIdx     = stack.peek();
                int leftHeight  = height[leftIdx];
                int rightHeight = height[i];
                int waterHeight = Math.min(leftHeight, rightHeight) - bottomHeight;
                int width       = i - leftIdx - 1;
                int water       = waterHeight * width;
                totalWater += water;

                System.out.println("  POP idx=" + bottomIdx
                    + " (bottom h=" + bottomHeight + ")"
                    + " leftWall=idx" + leftIdx + "(h=" + leftHeight + ")"
                    + " rightWall=idx" + i + "(h=" + rightHeight + ")"
                    + " waterH=" + waterHeight
                    + " w=" + width
                    + " water=" + water
                    + " total=" + totalWater);
            }

            stack.push(i);

            // Print current stack state
            System.out.print("  Stack: [");
            Integer[] arr = stack.toArray(new Integer[0]);
            for (int k = arr.length - 1; k >= 0; k--) {
                System.out.print(arr[k] + "(h=" + height[arr[k]] + ")");
                if (k > 0) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println();
        }

        System.out.println("Total Water: " + totalWater);
    }

    // ─────────────────────────────────────────────────────────────
    // VISUAL: ASCII histogram with water
    // ─────────────────────────────────────────────────────────────
    public static void visualize(int[] height) {
        int n = height.length;
        if (n == 0) return;

        // Compute water at each position
        int[] maxLeft = new int[n];
        maxLeft[0] = height[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], height[i]);
        }

        int[] maxRight = new int[n];
        maxRight[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], height[i]);
        }

        int[] water = new int[n];
        for (int i = 0; i < n; i++) {
            water[i] = Math.min(maxLeft[i], maxRight[i]) - height[i];
        }

        // Find max height for rendering
        int maxH = 0;
        for (int h : height) maxH = Math.max(maxH, h);

        // Render from top to bottom
        System.out.println("  Visual Histogram (█=bar, ░=water):");
        for (int level = maxH; level >= 1; level--) {
            System.out.print("  " + String.format("%2d", level) + " │");
            for (int i = 0; i < n; i++) {
                if (height[i] >= level) {
                    System.out.print(" █");
                } else if (height[i] + water[i] >= level) {
                    System.out.print(" ░");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        System.out.print("     └");
        for (int i = 0; i < n; i++) {
            System.out.print("──");
        }
        System.out.println();
        System.out.print("      ");
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("%2d", i));
        }
        System.out.println("  ← index");
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 55: Trapping Rain Water                     ║");
        System.out.println("║  Pattern: Monotonic Stack + Two Pointers             ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        int[] h1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        visualize(h1);
        System.out.println();
        tracePrefixArrays(h1);
        System.out.println();

        // ── TEST 2: Two Pointers Trace ──
        System.out.println("═══ TEST 2: Two Pointers Trace ═══");
        traceTwoPointers(h1);
        System.out.println();

        // ── TEST 3: Monotonic Stack Trace ──
        System.out.println("═══ TEST 3: Monotonic Stack Trace ═══");
        int[] h3 = {3, 0, 2, 0, 4};
        visualize(h3);
        System.out.println();
        traceMonotonicStack(h3);
        System.out.println();

        // ── TEST 4: No Water (Ascending) ──
        System.out.println("═══ TEST 4: No Water — Ascending ═══");
        int[] h4 = {1, 2, 3, 4, 5};
        visualize(h4);
        System.out.println("  Water: " + trapTwoPointers(h4));
        System.out.println();

        // ── TEST 5: No Water (Descending) ──
        System.out.println("═══ TEST 5: No Water — Descending ═══");
        int[] h5 = {5, 4, 3, 2, 1};
        visualize(h5);
        System.out.println("  Water: " + trapTwoPointers(h5));
        System.out.println();

        // ── TEST 6: Single Deep Valley ──
        System.out.println("═══ TEST 6: Single Deep Valley ═══");
        int[] h6 = {5, 0, 0, 0, 5};
        visualize(h6);
        System.out.println("  Water: " + trapTwoPointers(h6));
        System.out.println();

        // ── TEST 7: Multiple Valleys ──
        System.out.println("═══ TEST 7: Multiple Valleys ═══");
        int[] h7 = {4, 1, 3, 0, 2, 1, 5};
        visualize(h7);
        System.out.println();
        tracePrefixArrays(h7);
        System.out.println();

        // ── TEST 8: All Same Height ──
        System.out.println("═══ TEST 8: All Same Height ═══");
        int[] h8 = {3, 3, 3, 3, 3};
        System.out.println("  Water: " + trapTwoPointers(h8));
        System.out.println();

        // ── TEST 9: Edge Cases ──
        System.out.println("═══ TEST 9: Edge Cases ═══");
        System.out.println("  Empty array: " + trapTwoPointers(new int[] {}));
        System.out.println("  Single bar:  " + trapTwoPointers(new int[] {5}));
        System.out.println("  Two bars:    " + trapTwoPointers(new int[] {3, 7}));
        System.out.println();

        // ── TEST 10: Verify all approaches match ──
        System.out.println("═══ TEST 10: Verification — All Approaches ═══");
        int[][] testCases = {
            {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1},
            {4, 2, 0, 3, 2, 5},
            {3, 0, 2, 0, 4},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {5, 0, 0, 0, 5},
            {4, 1, 3, 0, 2, 1, 5},
            {3, 3, 3, 3, 3},
            {},
            {5},
            {3, 7},
            {0, 7, 1, 4, 6},
            {2, 0, 2},
            {6, 4, 2, 0, 3, 2, 0, 3, 1, 4, 5, 3, 2, 7, 5, 3, 0, 1, 2, 1, 3, 4, 6, 8, 1, 3}
        };

        boolean allPassed = true;
        for (int t = 0; t < testCases.length; t++) {
            int brute   = trapBruteForce(testCases[t]);
            int prefix  = trapPrefixArrays(testCases[t]);
            int stack   = trapMonotonicStack(testCases[t]);
            int twoPtr  = trapTwoPointers(testCases[t]);

            boolean match = (brute == prefix)
                         && (prefix == stack)
                         && (stack == twoPtr);

            System.out.printf("  Test %2d: Brute=%-3d Prefix=%-3d Stack=%-3d TwoPtr=%-3d %s%n",
                t + 1, brute, prefix, stack, twoPtr,
                match ? "✓ PASS" : "✗ FAIL");

            if (!match) allPassed = false;
        }
        System.out.println(allPassed
            ? "\n  ✅ ALL TESTS PASSED"
            : "\n  ❌ SOME TESTS FAILED");
        System.out.println();

        // ── TEST 11: Performance Comparison ──
        System.out.println("═══ TEST 11: Performance Comparison ═══");
        int size = 50000;
        int[] large = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            large[i] = rand.nextInt(10000);
        }

        long start, elapsed;

        start = System.nanoTime();
        int bruteResult = trapBruteForce(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  Brute O(n²):     result=" + bruteResult
            + "  time=" + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        int prefixResult = trapPrefixArrays(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  Prefix O(n):     result=" + prefixResult
            + "  time=" + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        int stackResult = trapMonotonicStack(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  Stack O(n):      result=" + stackResult
            + "  time=" + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        int twoPtrResult = trapTwoPointers(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  TwoPtr O(n)/O(1):result=" + twoPtrResult
            + "  time=" + (elapsed / 1_000_000) + " ms");
    }
}