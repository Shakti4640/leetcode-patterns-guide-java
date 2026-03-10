import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class NextGreaterElement {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Left-to-Right, Decreasing Stack
    // ─────────────────────────────────────────────
    public static int[] nextGreaterElement(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // Default: no next greater

        // Stack stores INDICES of elements waiting for their next greater
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            // Pop all elements that found their next greater (current element)
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                int waitingIdx = stack.pop();
                result[waitingIdx] = nums[i]; // Current element IS the answer
            }

            // Current element starts waiting for ITS next greater
            stack.push(i);
        }

        // Elements remaining on stack → no next greater → already -1
        return result;
    }

    // ─────────────────────────────────────────────
    // ALTERNATIVE: Right-to-Left, Stack Holds Candidates
    // ─────────────────────────────────────────────
    public static int[] nextGreaterElementRTL(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];

        // Stack stores VALUES of potential "next greater" candidates
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {

            // Pop candidates that are too small to be the answer
            while (!stack.isEmpty() && stack.peek() <= nums[i]) {
                stack.pop();
            }

            // Stack top (if exists) is the next greater element
            result[i] = stack.isEmpty() ? -1 : stack.peek();

            // Current element becomes a candidate for elements to its left
            stack.push(nums[i]);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE (for verification)
    // ─────────────────────────────────────────────
    public static int[] nextGreaterBrute(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            result[i] = -1;
            for (int j = i + 1; j < n; j++) {
                if (nums[j] > nums[i]) {
                    result[i] = nums[j];
                    break;
                }
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE: Visualize the monotonic stack
    // ─────────────────────────────────────────────
    public static void nextGreaterWithTrace(int[] nums) {

        System.out.print("Array: [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("─────────────────────────────────────────────────");

        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            System.out.println("i=" + i + ", nums[" + i + "]=" + nums[i]);

            // Show pops
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                int waitingIdx = stack.pop();
                result[waitingIdx] = nums[i];
                System.out.println("  POP index " + waitingIdx
                        + " (val=" + nums[waitingIdx] + ")"
                        + " → next greater = " + nums[i]
                        + "  result[" + waitingIdx + "] = " + nums[i]);
            }

            stack.push(i);

            // Show stack state
            System.out.print("  PUSH index " + i + " → Stack (bottom→top): [");
            Integer[] stackArr = stack.toArray(new Integer[0]);
            for (int j = stackArr.length - 1; j >= 0; j--) {
                System.out.print(nums[stackArr[j]]);
                if (j > 0) System.out.print(", ");
            }
            System.out.println("] ← decreasing ✓");
            System.out.println();
        }

        // Show remaining
        if (!stack.isEmpty()) {
            System.out.print("Remaining on stack (no next greater): ");
            while (!stack.isEmpty()) {
                int idx = stack.pop();
                System.out.print("index " + idx + " (val=" + nums[idx] + ")  ");
            }
            System.out.println();
        }

        System.out.print("Result: [");
        for (int i = 0; i < n; i++) {
            System.out.print(result[i]);
            if (i < n - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // ─────────────────────────────────────────────
    // VARIANT: Next Greater Element in CIRCULAR Array
    //   → After the last element, wrap around to the beginning
    //   → Process 2n elements with modulo indexing
    // ─────────────────────────────────────────────
    public static int[] nextGreaterCircular(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        // Process twice to handle the circular wrap
        for (int i = 0; i < 2 * n; i++) {
            int idx = i % n;

            while (!stack.isEmpty() && nums[idx] > nums[stack.peek()]) {
                int waitingIdx = stack.pop();
                result[waitingIdx] = nums[idx];
            }

            // Only push during the first pass
            // Second pass is just for resolving remaining elements
            if (i < n) {
                stack.push(idx);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Next SMALLER Element
    //   → Use INCREASING stack instead of decreasing
    //   → Pop when current < stack top
    // ─────────────────────────────────────────────
    public static int[] nextSmallerElement(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        // INCREASING stack (bottom to top: smallest to largest)
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            // Pop elements that found their next SMALLER
            while (!stack.isEmpty() && nums[i] < nums[stack.peek()]) {
                int waitingIdx = stack.pop();
                result[waitingIdx] = nums[i];
            }

            stack.push(i);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Previous Greater Element
    //   → For each element, find nearest GREATER to its LEFT
    //   → Answer comes from STACK TOP (not current element)
    // ─────────────────────────────────────────────
    public static int[] previousGreaterElement(int[] nums) {

        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        // Decreasing stack (same as next greater)
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            // Pop elements smaller than or equal to current
            // They can't be "previous greater" for anyone after current
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                stack.pop();
            }

            // Stack top (if exists) is the previous greater for current element
            if (!stack.isEmpty()) {
                result[i] = nums[stack.peek()];
            }

            stack.push(i);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Daily Temperatures
    //   → Instead of the VALUE of next greater, return the DISTANCE
    //   → "How many days until a warmer temperature?"
    // ─────────────────────────────────────────────
    public static int[] dailyTemperatures(int[] temps) {

        int n = temps.length;
        int[] result = new int[n]; // Default 0 = no warmer day

        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            while (!stack.isEmpty() && temps[i] > temps[stack.peek()]) {
                int waitingIdx = stack.pop();
                result[waitingIdx] = i - waitingIdx; // DISTANCE, not value
            }

            stack.push(i);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Stock Span
    //   → For each day, how many consecutive days (including today)
    //     was the price ≤ today's price?
    //   → Uses "previous greater" logic
    // ─────────────────────────────────────────────
    public static int[] stockSpan(int[] prices) {

        int n = prices.length;
        int[] result = new int[n];

        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {

            // Pop days with price ≤ today
            while (!stack.isEmpty() && prices[stack.peek()] <= prices[i]) {
                stack.pop();
            }

            // Span = distance from previous greater (or start if none)
            result[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());

            stack.push(i);
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // UTILITY: Print array
    // ─────────────────────────────────────────────
    private static void printArray(String label, int[] arr) {
        System.out.print(label + "[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 36: Next Greater Element                ║");
        System.out.println("║  Pattern: Monotonic Stack — Decreasing Invariant ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example (Traced) ═══");
        nextGreaterWithTrace(new int[]{4, 5, 2, 10, 8});
        System.out.println();

        // ── TEST 2: Another example with trace ──
        System.out.println("═══ TEST 2: Another Example (Traced) ═══");
        nextGreaterWithTrace(new int[]{3, 1, 2, 4});
        System.out.println();

        // ── TEST 3: Decreasing array ──
        System.out.println("═══ TEST 3: Decreasing Array (All -1) ═══");
        nextGreaterWithTrace(new int[]{5, 4, 3, 2, 1});
        System.out.println();

        // ── TEST 4: Increasing array ──
        System.out.println("═══ TEST 4: Increasing Array ═══");
        nextGreaterWithTrace(new int[]{1, 2, 3, 4, 5});
        System.out.println();

        // ── TEST 5: Verify both approaches match ──
        System.out.println("═══ TEST 5: Left-to-Right vs Right-to-Left ═══");
        int[] test5 = {7, 3, 5, 1, 6, 4, 2, 8};
        printArray("  Input:   ", test5);
        printArray("  L→R:     ", nextGreaterElement(test5));
        printArray("  R→L:     ", nextGreaterElementRTL(test5));
        printArray("  Brute:   ", nextGreaterBrute(test5));
        System.out.println("  All match: "
                + (Arrays.equals(nextGreaterElement(test5), nextGreaterBrute(test5))
                && Arrays.equals(nextGreaterElementRTL(test5), nextGreaterBrute(test5))
                ? "✓" : "✗"));
        System.out.println();

        // ── TEST 6: Circular variant ──
        System.out.println("═══ TEST 6: Circular Next Greater ═══");
        int[] circ = {3, 1, 2, 4};
        printArray("  Input:    ", circ);
        printArray("  Linear:   ", nextGreaterElement(circ));
        printArray("  Circular: ", nextGreaterCircular(circ));
        System.out.println("  Note: In linear, 4→-1. In circular, 4 wraps to find nothing → still -1");
        System.out.println();

        int[] circ2 = {1, 2, 1};
        printArray("  Input:    ", circ2);
        printArray("  Linear:   ", nextGreaterElement(circ2));
        printArray("  Circular: ", nextGreaterCircular(circ2));
        System.out.println("  Note: In circular, last 1 wraps around to find 2");
        System.out.println();

        // ── TEST 7: Next Smaller variant ──
        System.out.println("═══ TEST 7: Next Smaller Element ═══");
        int[] small = {4, 5, 2, 10, 8};
        printArray("  Input:         ", small);
        printArray("  Next Greater:  ", nextGreaterElement(small));
        printArray("  Next Smaller:  ", nextSmallerElement(small));
        System.out.println();

        // ── TEST 8: Previous Greater variant ──
        System.out.println("═══ TEST 8: Previous Greater Element ═══");
        int[] prev = {4, 5, 2, 10, 8};
        printArray("  Input:            ", prev);
        printArray("  Next Greater:     ", nextGreaterElement(prev));
        printArray("  Previous Greater: ", previousGreaterElement(prev));
        System.out.println();

        // ── TEST 9: Daily Temperatures variant ──
        System.out.println("═══ TEST 9: Daily Temperatures ═══");
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
        printArray("  Temps:      ", temps);
        printArray("  Days until: ", dailyTemperatures(temps));
        System.out.println("  Meaning: temp[0]=73 → 1 day until 74");
        System.out.println("           temp[3]=71 → 2 days until 72");
        System.out.println("           temp[6]=76 → 0 (no warmer day)");
        System.out.println();

        // ── TEST 10: Stock Span variant ──
        System.out.println("═══ TEST 10: Stock Span ═══");
        int[] prices = {100, 80, 60, 70, 60, 75, 85};
        printArray("  Prices: ", prices);
        printArray("  Spans:  ", stockSpan(prices));
        System.out.println("  Meaning: price 85 → span 6 (85≥80, 85≥60, 85≥70, 85≥60, 85≥75)");
        System.out.println();

        // ── TEST 11: Stress Test ──
        System.out.println("═══ TEST 11: Correctness Stress Test ═══");
        java.util.Random rand = new java.util.Random(42);
        int testCount = 10000;
        int passed = 0;

        for (int t = 0; t < testCount; t++) {
            int size = rand.nextInt(50) + 1;
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = rand.nextInt(100);
            }

            int[] ltr = nextGreaterElement(arr);
            int[] rtl = nextGreaterElementRTL(arr);
            int[] brute = nextGreaterBrute(arr);

            if (Arrays.equals(ltr, brute) && Arrays.equals(rtl, brute)) {
                passed++;
            } else {
                System.out.println("  FAILED on: " + Arrays.toString(arr));
            }
        }

        System.out.println("  " + passed + "/" + testCount + " tests passed"
                + (passed == testCount ? " ✓ ALL CORRECT" : " ✗ SOME FAILED"));
        System.out.println();

        // ── TEST 12: Performance comparison ──
        System.out.println("═══ TEST 12: Performance Comparison ═══");
        int perfSize = 1_000_000;
        int[] perfArr = new int[perfSize];
        for (int i = 0; i < perfSize; i++) {
            perfArr[i] = rand.nextInt(1000000);
        }

        // Monotonic Stack
        long start = System.nanoTime();
        int[] stackResult = nextGreaterElement(perfArr);
        long stackTime = System.nanoTime() - start;

        System.out.println("  Array size: " + String.format("%,d", perfSize));
        System.out.println("  Monotonic Stack: "
                + String.format("%,d", stackTime / 1000) + " μs");

        // Brute force (on smaller array to avoid timeout)
        int smallPerf = 50000;
        int[] smallArr = Arrays.copyOf(perfArr, smallPerf);

        start = System.nanoTime();
        int[] bruteResult = nextGreaterBrute(smallArr);
        long bruteTime = System.nanoTime() - start;

        System.out.println("  Brute Force (n=" + String.format("%,d", smallPerf) + "): "
                + String.format("%,d", bruteTime / 1000) + " μs");
        System.out.println("  Estimated brute for n="
                + String.format("%,d", perfSize) + ": "
                + String.format("%,d", bruteTime * (perfSize / smallPerf) * (perfSize / smallPerf) / 1000)
                + " μs");
        System.out.println("  Estimated speedup: "
                + String.format("%,d", (bruteTime * (perfSize / smallPerf) * (perfSize / smallPerf))
                / Math.max(stackTime, 1)) + "x");
        System.out.println();

        // ── TEST 13: Stack size analysis ──
        System.out.println("═══ TEST 13: Max Stack Size Analysis ═══");
        int[][] patterns = {
                {1, 2, 3, 4, 5},          // Increasing → stack grows to n
                {5, 4, 3, 2, 1},          // Decreasing → stack grows to n
                {1, 3, 2, 5, 4},          // Zigzag → stack stays small
                {5, 1, 5, 1, 5},          // Alternating → stack stays small
        };
        String[] patternNames = {"Increasing", "Decreasing", "Zigzag", "Alternating"};

        for (int p = 0; p < patterns.length; p++) {
            Deque<Integer> testStack = new ArrayDeque<>();
            int maxStackSize = 0;

            for (int i = 0; i < patterns[p].length; i++) {
                while (!testStack.isEmpty() && patterns[p][i] > patterns[p][testStack.peek()]) {
                    testStack.pop();
                }
                testStack.push(i);
                maxStackSize = Math.max(maxStackSize, testStack.size());
            }

            System.out.print("  " + String.format("%-12s", patternNames[p]));
            printArray(": ", patterns[p]);
            System.out.println("    Max stack size: " + maxStackSize + "/" + patterns[p].length);
        }
    }
}