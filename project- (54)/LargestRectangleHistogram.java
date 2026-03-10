import java.util.ArrayDeque;
import java.util.Deque;

public class LargestRectangleHistogram {

    // ─────────────────────────────────────────────────────────────
    // APPROACH 1: TWO-PASS — Previous Smaller + Next Smaller
    // Clearest to understand — recommended for learning
    // ─────────────────────────────────────────────────────────────
    public static int largestRectangleTwoPass(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        if (n == 1) return heights[0];

        int[] leftBound  = new int[n];  // index of previous smaller element
        int[] rightBound = new int[n];  // index of next smaller element

        Deque<Integer> stack = new ArrayDeque<>();

        // ── PASS 1: Previous Smaller Element (scan left → right) ──
        // Stack maintains INCREASING order of heights
        // For each bar: pop everything ≥ current height
        // Whatever remains on top is the previous smaller
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            leftBound[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        // ── PASS 2: Next Smaller Element (scan right → left) ──
        // Same logic but reversed direction
        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            rightBound[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        // ── PASS 3: Compute maximum area ──
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = rightBound[i] - leftBound[i] - 1;
            int area  = heights[i] * width;
            maxArea   = Math.max(maxArea, area);
        }

        return maxArea;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH 2: SINGLE-PASS — Pop computes area on the fly
    // More elegant — recommended for interviews once understood
    // ─────────────────────────────────────────────────────────────
    public static int largestRectangleSinglePass(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        if (n == 1) return heights[0];

        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;

        for (int i = 0; i < n; i++) {
            // When current bar is shorter than stack top
            // → stack top has found its RIGHT boundary (current i)
            // → stack top's LEFT boundary is element below it
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                int poppedHeight = heights[stack.pop()];
                int leftBound    = stack.isEmpty() ? -1 : stack.peek();
                int rightBound   = i;
                int width        = rightBound - leftBound - 1;
                int area         = poppedHeight * width;
                maxArea          = Math.max(maxArea, area);
            }
            stack.push(i);
        }

        // Drain remaining elements
        // These bars never found a shorter bar to their right
        // → right boundary = n
        while (!stack.isEmpty()) {
            int poppedHeight = heights[stack.pop()];
            int leftBound    = stack.isEmpty() ? -1 : stack.peek();
            int rightBound   = n;
            int width        = rightBound - leftBound - 1;
            int area         = poppedHeight * width;
            maxArea          = Math.max(maxArea, area);
        }

        return maxArea;
    }

    // ─────────────────────────────────────────────────────────────
    // APPROACH 3: SINGLE-PASS WITH SENTINEL — Cleanest code
    // Append height 0 to force all pops — no separate drain
    // ─────────────────────────────────────────────────────────────
    public static int largestRectangleSentinel(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;

        // Iterate n+1 times: indices 0..n-1 use actual heights
        // Index n uses virtual height 0 (sentinel)
        for (int i = 0; i <= n; i++) {
            // Sentinel at position n has height 0
            // This guarantees all remaining stack elements get popped
            int currentHeight = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() && heights[stack.peek()] >= currentHeight) {
                int poppedHeight = heights[stack.pop()];
                int leftBound    = stack.isEmpty() ? -1 : stack.peek();
                int rightBound   = i;
                int width        = rightBound - leftBound - 1;
                int area         = poppedHeight * width;
                maxArea          = Math.max(maxArea, area);
            }
            stack.push(i);
        }

        return maxArea;
    }

    // ─────────────────────────────────────────────────────────────
    // BRUTE FORCE — O(n²) — For verification only
    // ─────────────────────────────────────────────────────────────
    public static int largestRectangleBrute(int[] heights) {
        int n = heights.length;
        int maxArea = 0;

        for (int i = 0; i < n; i++) {
            int minHeight = heights[i];
            for (int j = i; j < n; j++) {
                minHeight = Math.min(minHeight, heights[j]);
                int area  = minHeight * (j - i + 1);
                maxArea   = Math.max(maxArea, area);
            }
        }

        return maxArea;
    }

    // ─────────────────────────────────────────────────────────────
    // TRACE FUNCTION — Visualize the single-pass algorithm
    // ─────────────────────────────────────────────────────────────
    public static void traceAlgorithm(int[] heights) {
        int n = heights.length;

        System.out.println("Heights: ");
        System.out.print("  [");
        for (int i = 0; i < n; i++) {
            System.out.print(heights[i]);
            if (i < n - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Indices:");
        System.out.print("   ");
        for (int i = 0; i < n; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        System.out.println("─────────────────────────────────────");

        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;

        for (int i = 0; i <= n; i++) {
            int currentHeight = (i == n) ? 0 : heights[i];
            String label = (i == n) ? "SENTINEL(h=0)" : "i=" + i + " h=" + currentHeight;
            System.out.println("Processing " + label + ":");

            while (!stack.isEmpty() && heights[stack.peek()] >= currentHeight) {
                int topIdx       = stack.pop();
                int poppedHeight = heights[topIdx];
                int leftBound    = stack.isEmpty() ? -1 : stack.peek();
                int rightBound   = i;
                int width        = rightBound - leftBound - 1;
                int area         = poppedHeight * width;
                maxArea          = Math.max(maxArea, area);

                System.out.println("  → POP index " + topIdx 
                    + " (height=" + poppedHeight + ")");
                System.out.println("    leftBound=" + leftBound 
                    + "  rightBound=" + rightBound
                    + "  width=" + width 
                    + "  area=" + area
                    + (area == maxArea ? " ← NEW MAX" : ""));
            }

            if (i < n) {
                stack.push(i);
                System.out.print("  Stack after: [");
                // Print stack bottom to top
                Integer[] arr = stack.toArray(new Integer[0]);
                for (int k = arr.length - 1; k >= 0; k--) {
                    System.out.print(arr[k] + "(h=" + heights[arr[k]] + ")");
                    if (k > 0) System.out.print(", ");
                }
                System.out.println("]");
            }
            System.out.println();
        }

        System.out.println("═══ MAXIMUM AREA = " + maxArea + " ═══");
    }

    // ─────────────────────────────────────────────────────────────
    // TWO-PASS TRACE — Show boundaries for each bar
    // ─────────────────────────────────────────────────────────────
    public static void traceTwoPass(int[] heights) {
        int n = heights.length;
        int[] leftBound  = new int[n];
        int[] rightBound = new int[n];

        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            leftBound[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            rightBound[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        System.out.println("┌───────┬────────┬───────────┬────────────┬───────┬──────┐");
        System.out.println("│ Index │ Height │ LeftBound │ RightBound │ Width │ Area │");
        System.out.println("├───────┼────────┼───────────┼────────────┼───────┼──────┤");

        int maxArea = 0;
        int maxIdx  = 0;
        for (int i = 0; i < n; i++) {
            int width = rightBound[i] - leftBound[i] - 1;
            int area  = heights[i] * width;
            if (area > maxArea) {
                maxArea = area;
                maxIdx  = i;
            }
            System.out.printf("│  %3d  │  %4d  │    %3d    │     %3d    │  %3d  │ %4d │%s%n",
                i, heights[i], leftBound[i], rightBound[i], width, area,
                (i == maxIdx && area == maxArea) ? " ← MAX" : "");
        }

        System.out.println("└───────┴────────┴───────────┴────────────┴───────┴──────┘");
        System.out.println("Maximum Area: " + maxArea);
    }

    // ─────────────────────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 54: Largest Rectangle in Histogram          ║");
        System.out.println("║  Pattern: Monotonic Stack → Boundary → Area Max      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        int[] h1 = {2, 1, 5, 6, 2, 3};
        traceTwoPass(h1);
        System.out.println();
        traceAlgorithm(h1);
        System.out.println();

        // ── TEST 2: All Same Height ──
        System.out.println("═══ TEST 2: All Same Height ═══");
        int[] h2 = {3, 3, 3, 3, 3};
        traceTwoPass(h2);
        System.out.println();

        // ── TEST 3: Ascending ──
        System.out.println("═══ TEST 3: Ascending Order ═══");
        int[] h3 = {1, 2, 3, 4, 5};
        traceTwoPass(h3);
        System.out.println();

        // ── TEST 4: Descending ──
        System.out.println("═══ TEST 4: Descending Order ═══");
        int[] h4 = {5, 4, 3, 2, 1};
        traceTwoPass(h4);
        System.out.println();

        // ── TEST 5: Single Bar ──
        System.out.println("═══ TEST 5: Single Bar ═══");
        int[] h5 = {7};
        System.out.println("Area: " + largestRectangleSentinel(h5));
        System.out.println();

        // ── TEST 6: Valley Shape ──
        System.out.println("═══ TEST 6: Valley Shape ═══");
        int[] h6 = {6, 2, 5, 4, 5, 1, 6};
        traceTwoPass(h6);
        System.out.println();

        // ── TEST 7: Verify all approaches match ──
        System.out.println("═══ TEST 7: Verification — All Approaches ═══");
        int[][] testCases = {
            {2, 1, 5, 6, 2, 3},
            {3, 3, 3, 3, 3},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {7},
            {6, 2, 5, 4, 5, 1, 6},
            {2, 4},
            {1},
            {0, 9},
            {4, 2, 0, 3, 2, 5}
        };

        boolean allPassed = true;
        for (int t = 0; t < testCases.length; t++) {
            int brute    = largestRectangleBrute(testCases[t]);
            int twoPass  = largestRectangleTwoPass(testCases[t]);
            int single   = largestRectangleSinglePass(testCases[t]);
            int sentinel = largestRectangleSentinel(testCases[t]);

            boolean match = (brute == twoPass) 
                         && (twoPass == single) 
                         && (single == sentinel);

            System.out.print("  Test " + (t + 1) + ": ");
            System.out.print("Brute=" + brute);
            System.out.print("  TwoPass=" + twoPass);
            System.out.print("  SinglePass=" + single);
            System.out.print("  Sentinel=" + sentinel);
            System.out.println(match ? "  ✓ PASS" : "  ✗ FAIL");

            if (!match) allPassed = false;
        }
        System.out.println(allPassed 
            ? "\n  ✅ ALL TESTS PASSED" 
            : "\n  ❌ SOME TESTS FAILED");
        System.out.println();

        // ── TEST 8: Performance ──
        System.out.println("═══ TEST 8: Performance Comparison ═══");
        int size = 50000;
        int[] large = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            large[i] = rand.nextInt(10000) + 1;
        }

        long start, elapsed;

        start = System.nanoTime();
        int bruteResult = largestRectangleBrute(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  Brute Force O(n²):  result=" + bruteResult 
            + "  time=" + (elapsed / 1_000_000) + " ms");

        start = System.nanoTime();
        int stackResult = largestRectangleSentinel(large);
        elapsed = System.nanoTime() - start;
        System.out.println("  Stack O(n):         result=" + stackResult 
            + "  time=" + (elapsed / 1_000_000) + " ms");
    }
}