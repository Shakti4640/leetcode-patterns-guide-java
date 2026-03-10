import java.util.PriorityQueue;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MedianFromDataStream {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Two Heaps — Strategy A (Flow-Based)
    // ─────────────────────────────────────────────
    static class MedianFinder {

        // Max-heap: stores the SMALLER half (left side of sorted order)
        // Peek gives the LARGEST of the small numbers
        private PriorityQueue<Integer> maxHeap;

        // Min-heap: stores the LARGER half (right side of sorted order)
        // Peek gives the SMALLEST of the large numbers
        private PriorityQueue<Integer> minHeap;

        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {

            // Step 1: Add to max-heap (always)
            maxHeap.add(num);

            // Step 2: Flow the max of max-heap to min-heap
            // This ensures: maxHeap.peek() ≤ minHeap.peek()
            minHeap.add(maxHeap.poll());

            // Step 3: Rebalance sizes
            // Convention: maxHeap.size() ≥ minHeap.size()
            if (minHeap.size() > maxHeap.size()) {
                maxHeap.add(minHeap.poll());
            }
        }

        public double findMedian() {

            if (maxHeap.isEmpty()) {
                throw new IllegalStateException("No numbers added yet");
            }

            if (maxHeap.size() > minHeap.size()) {
                // Odd count → max-heap has the extra middle element
                return maxHeap.peek();
            } else {
                // Even count → average of both tops
                // Use division-first to avoid integer overflow
                return maxHeap.peek() / 2.0 + minHeap.peek() / 2.0;
            }
        }

        public int size() {
            return maxHeap.size() + minHeap.size();
        }

        public String getState() {
            List<Integer> maxList = new ArrayList<>(maxHeap);
            List<Integer> minList = new ArrayList<>(minHeap);
            Collections.sort(maxList);
            Collections.sort(minList);
            return "maxHeap=" + maxList + " (top=" +
                    (maxHeap.isEmpty() ? "empty" : maxHeap.peek()) +
                    ")  minHeap=" + minList + " (top=" +
                    (minHeap.isEmpty() ? "empty" : minHeap.peek()) + ")";
        }
    }

    // ─────────────────────────────────────────────
    // ALTERNATIVE: Strategy B (Direct Placement)
    // ─────────────────────────────────────────────
    static class MedianFinderDirectPlacement {

        private PriorityQueue<Integer> maxHeap;
        private PriorityQueue<Integer> minHeap;

        public MedianFinderDirectPlacement() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {

            // Step 1: Direct placement into correct heap
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.add(num);
            } else {
                minHeap.add(num);
            }

            // Step 2: Rebalance sizes
            // Allow maxHeap to be at most 1 bigger
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.add(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.add(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            return maxHeap.peek() / 2.0 + minHeap.peek() / 2.0;
        }
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Sorted List (for verification)
    // ─────────────────────────────────────────────
    static class MedianFinderBrute {

        private List<Integer> sorted;

        public MedianFinderBrute() {
            sorted = new ArrayList<>();
        }

        public void addNum(int num) {
            // Binary search for insertion point
            int pos = Collections.binarySearch(sorted, num);
            if (pos < 0) pos = -(pos + 1);
            sorted.add(pos, num);
        }

        public double findMedian() {
            int n = sorted.size();
            if (n % 2 == 1) {
                return sorted.get(n / 2);
            }
            return sorted.get(n / 2 - 1) / 2.0 + sorted.get(n / 2) / 2.0;
        }
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION: Visualize heap operations
    // ─────────────────────────────────────────────
    public static void traceMedianFinder(int[] nums) {

        System.out.println("Stream: ");
        System.out.print("  [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("─────────────────────────────────────────────────");

        MedianFinder mf = new MedianFinder();
        List<Integer> allNums = new ArrayList<>();

        for (int num : nums) {
            allNums.add(num);
            mf.addNum(num);

            // Show sorted order for reference
            List<Integer> sortedCopy = new ArrayList<>(allNums);
            Collections.sort(sortedCopy);

            double median = mf.findMedian();

            System.out.println("addNum(" + num + "):");
            System.out.println("  " + mf.getState());
            System.out.print("  Sorted view: [");
            int mid = sortedCopy.size() / 2;
            for (int i = 0; i < sortedCopy.size(); i++) {
                if (i == mid && sortedCopy.size() % 2 == 0) {
                    System.out.print("| ");
                }
                if (i == mid && sortedCopy.size() % 2 == 1) {
                    System.out.print("(" + sortedCopy.get(i) + ")");
                } else {
                    System.out.print(sortedCopy.get(i));
                }
                if (i == mid - 1 && sortedCopy.size() % 2 == 0) {
                    System.out.print(" ");
                } else if (i < sortedCopy.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
            System.out.println("  Median = " + median);
            System.out.println();
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Weighted Median (each number has a weight)
    //   → Not directly heap-solvable but shows the concept
    //   → Here we just handle the standard case efficiently
    // ─────────────────────────────────────────────

    // ─────────────────────────────────────────────
    // VARIANT: Running Median of Last K Elements
    //   → Preview of Project 41 (Sliding Window Median)
    //   → Simple version: just rebuild heaps for last K
    // ─────────────────────────────────────────────
    public static double[] runningMedianLastK(int[] nums, int k) {

        double[] result = new double[nums.length - k + 1];

        for (int i = 0; i <= nums.length - k; i++) {
            MedianFinder mf = new MedianFinder();
            for (int j = i; j < i + k; j++) {
                mf.addNum(nums[j]);
            }
            result[i] = mf.findMedian();
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 35: Find Median from a Data Stream      ║");
        System.out.println("║  Pattern: Two Heaps — Max-Heap + Min-Heap        ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example (Traced) ═══");
        traceMedianFinder(new int[]{5, 2, 8, 1, 4, 7});
        System.out.println();

        // ── TEST 2: Problem statement example ──
        System.out.println("═══ TEST 2: Problem Statement Example ═══");
        traceMedianFinder(new int[]{1, 3, 5, 2, 4, 0});
        System.out.println();

        // ── TEST 3: Descending order ──
        System.out.println("═══ TEST 3: Descending Order Input ═══");
        traceMedianFinder(new int[]{10, 8, 6, 4, 2});
        System.out.println();

        // ── TEST 4: All same values ──
        System.out.println("═══ TEST 4: All Same Values ═══");
        traceMedianFinder(new int[]{5, 5, 5, 5, 5});
        System.out.println();

        // ── TEST 5: Single element ──
        System.out.println("═══ TEST 5: Single Element ═══");
        traceMedianFinder(new int[]{42});
        System.out.println();

        // ── TEST 6: Negative numbers ──
        System.out.println("═══ TEST 6: Negative Numbers ═══");
        traceMedianFinder(new int[]{-5, -2, -8, -1, -4});
        System.out.println();

        // ── TEST 7: Correctness verification ──
        System.out.println("═══ TEST 7: Correctness Verification ═══");
        java.util.Random rand = new java.util.Random(42);
        int testSize = 1000;
        int[] testNums = new int[testSize];
        for (int i = 0; i < testSize; i++) {
            testNums[i] = rand.nextInt(2001) - 1000; // [-1000, 1000]
        }

        MedianFinder mfOpt = new MedianFinder();
        MedianFinderDirectPlacement mfDirect = new MedianFinderDirectPlacement();
        MedianFinderBrute mfBrute = new MedianFinderBrute();

        boolean allCorrect = true;
        for (int i = 0; i < testSize; i++) {
            mfOpt.addNum(testNums[i]);
            mfDirect.addNum(testNums[i]);
            mfBrute.addNum(testNums[i]);

            double medOpt = mfOpt.findMedian();
            double medDirect = mfDirect.findMedian();
            double medBrute = mfBrute.findMedian();

            if (Math.abs(medOpt - medBrute) > 1e-9
                    || Math.abs(medDirect - medBrute) > 1e-9) {
                System.out.println("  MISMATCH at i=" + i
                        + ": opt=" + medOpt
                        + " direct=" + medDirect
                        + " brute=" + medBrute);
                allCorrect = false;
            }
        }

        System.out.println("  Tested " + testSize + " insertions");
        System.out.println("  Strategy A (flow): " + (allCorrect ? "✓ ALL CORRECT" : "✗ FAILURES"));
        System.out.println("  Strategy B (direct): " + (allCorrect ? "✓ ALL CORRECT" : "✗ FAILURES"));
        System.out.println();

        // ── TEST 8: Performance comparison ──
        System.out.println("═══ TEST 8: Performance Comparison ═══");
        int perfSize = 100000;
        int[] perfNums = new int[perfSize];
        for (int i = 0; i < perfSize; i++) {
            perfNums[i] = rand.nextInt(1000000);
        }

        // Two Heaps (Strategy A)
        long start = System.nanoTime();
        MedianFinder mfPerf = new MedianFinder();
        double lastMedian = 0;
        for (int num : perfNums) {
            mfPerf.addNum(num);
            lastMedian = mfPerf.findMedian();
        }
        long heapTime = System.nanoTime() - start;

        // Brute Force (Sorted List)
        start = System.nanoTime();
        MedianFinderBrute mfBrutePerf = new MedianFinderBrute();
        double lastMedianBrute = 0;
        for (int num : perfNums) {
            mfBrutePerf.addNum(num);
            lastMedianBrute = mfBrutePerf.findMedian();
        }
        long bruteTime = System.nanoTime() - start;

        System.out.println("  Elements: " + String.format("%,d", perfSize));
        System.out.println("  Two Heaps:    "
                + String.format("%,d", heapTime / 1_000_000) + " ms"
                + "  (last median=" + String.format("%.1f", lastMedian) + ")");
        System.out.println("  Sorted List:  "
                + String.format("%,d", bruteTime / 1_000_000) + " ms"
                + "  (last median=" + String.format("%.1f", lastMedianBrute) + ")");
        System.out.println("  Speedup: "
                + String.format("%.1f", (double) bruteTime / Math.max(heapTime, 1)) + "x");
        System.out.println("  Results match: "
                + (Math.abs(lastMedian - lastMedianBrute) < 1e-9 ? "✓" : "✗"));
        System.out.println();

        // ── TEST 9: Integer overflow handling ──
        System.out.println("═══ TEST 9: Integer Overflow Handling ═══");
        MedianFinder mfOverflow = new MedianFinder();
        mfOverflow.addNum(Integer.MAX_VALUE);
        mfOverflow.addNum(Integer.MAX_VALUE);
        double overflowMedian = mfOverflow.findMedian();
        System.out.println("  Added: " + Integer.MAX_VALUE + " twice");
        System.out.println("  Median: " + overflowMedian);
        System.out.println("  Expected: " + (double) Integer.MAX_VALUE);
        System.out.println("  Correct: "
                + (Math.abs(overflowMedian - Integer.MAX_VALUE) < 1.0 ? "✓" : "✗"));
        System.out.println();

        // ── TEST 10: Running median of last K (preview of Project 41) ──
        System.out.println("═══ TEST 10: Running Median of Last K Elements ═══");
        int[] windowNums = {1, 3, 5, 2, 8, 6, 4, 7};
        int windowK = 3;
        double[] runningMeds = runningMedianLastK(windowNums, windowK);

        System.out.print("  Array: [");
        for (int i = 0; i < windowNums.length; i++) {
            System.out.print(windowNums[i]);
            if (i < windowNums.length - 1) System.out.print(", ");
        }
        System.out.println("]  k=" + windowK);

        for (int i = 0; i < runningMeds.length; i++) {
            System.out.print("  Window [");
            for (int j = i; j < i + windowK; j++) {
                System.out.print(windowNums[j]);
                if (j < i + windowK - 1) System.out.print(",");
            }
            System.out.println("] → median=" + runningMeds[i]);
        }
        System.out.println();
        System.out.println("  NOTE: This naive approach rebuilds heaps for each window → O(nk log k)");
        System.out.println("  Project 41 will show the EFFICIENT version → O(n log k)");

        // ── TEST 11: Heap size tracking ──
        System.out.println();
        System.out.println("═══ TEST 11: Heap Sizes Over Time ═══");
        MedianFinder mfSizes = new MedianFinder();
        int[] sizeNums = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        System.out.println("  ┌───────┬────────┬──────────┬──────────┬────────────┐");
        System.out.println("  │ Added │ Total  │ MaxHeap  │ MinHeap  │   Median   │");
        System.out.println("  ├───────┼────────┼──────────┼──────────┼────────────┤");

        for (int num : sizeNums) {
            mfSizes.addNum(num);
            int total = mfSizes.size();
            String maxSize = String.valueOf(
                    (total + 1) / 2); // maxHeap size
            String minSize = String.valueOf(
                    total / 2); // minHeap size
            double med = mfSizes.findMedian();

            System.out.printf("  │  %3d  │   %2d   │    %2s    │    %2s    │   %6.1f   │%n",
                    num, total, maxSize, minSize, med);
        }

        System.out.println("  └───────┴────────┴──────────┴──────────┴────────────┘");
        System.out.println();
        System.out.println("  Observation: maxHeap is always ≥ minHeap in size");
        System.out.println("  Observation: difference is always 0 or 1");
    }
}