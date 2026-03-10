import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;

public class SlidingWindowMedian {

    // ─────────────────────────────────────────────
    // SOLUTION 1: Two Heaps + Lazy Deletion (OPTIMAL)
    // ─────────────────────────────────────────────

    // Class-level heaps and tracking for clean helper access
    private PriorityQueue<Integer> maxHeap; // lower half (max at root)
    private PriorityQueue<Integer> minHeap; // upper half (min at root)
    private HashMap<Integer, Integer> delayedMap;
    private int maxHeapSize;
    private int minHeapSize;

    public double[] medianSlidingWindow(int[] nums, int k) {

        // Initialize data structures
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
        delayedMap = new HashMap<>();
        maxHeapSize = 0;
        minHeapSize = 0;

        int n = nums.length;
        double[] result = new double[n - k + 1];

        // ── STEP 1: Initialize first window ──
        // Sort first K elements to split correctly
        int[] firstWindow = Arrays.copyOfRange(nums, 0, k);
        int[] sorted = firstWindow.clone();
        Arrays.sort(sorted);

        // Lower half → maxHeap
        for (int i = 0; i < (k + 1) / 2; i++) {
            maxHeap.offer(sorted[i]);
            maxHeapSize++;
        }
        // Upper half → minHeap
        for (int i = (k + 1) / 2; i < k; i++) {
            minHeap.offer(sorted[i]);
            minHeapSize++;
        }

        // First median
        result[0] = getMedian(k);

        // ── STEP 2: Slide window ──
        for (int i = k; i < n; i++) {

            int outgoing = nums[i - k]; // leaving the window
            int incoming = nums[i];     // entering the window

            // ── A: Lazy delete outgoing ──
            delayedMap.merge(outgoing, 1, Integer::sum);

            // Determine which heap outgoing belongs to
            if (!maxHeap.isEmpty() && outgoing <= maxHeap.peek()) {
                maxHeapSize--;
            } else {
                minHeapSize--;
            }

            // ── B: Add incoming ──
            if (!maxHeap.isEmpty() && incoming <= maxHeap.peek()) {
                maxHeap.offer(incoming);
                maxHeapSize++;
            } else {
                minHeap.offer(incoming);
                minHeapSize++;
            }

            // ── C: Rebalance ──
            rebalance();

            // ── D: Prune ghost roots ──
            pruneHeap(maxHeap);
            pruneHeap(minHeap);

            // ── E: Record median ──
            result[i - k + 1] = getMedian(k);
        }

        return result;
    }

    private double getMedian(int k) {
        if (k % 2 == 1) {
            return (double) maxHeap.peek();
        } else {
            // Use double to avoid integer overflow
            return maxHeap.peek() / 2.0 + minHeap.peek() / 2.0;
        }
    }

    private void rebalance() {

        // maxHeap has too many → transfer to minHeap
        while (maxHeapSize > minHeapSize + 1) {
            pruneHeap(maxHeap); // ensure root is live before transfer
            int transferred = maxHeap.poll();
            minHeap.offer(transferred);
            maxHeapSize--;
            minHeapSize++;
        }

        // minHeap has too many → transfer to maxHeap
        while (minHeapSize > maxHeapSize) {
            pruneHeap(minHeap); // ensure root is live before transfer
            int transferred = minHeap.poll();
            maxHeap.offer(transferred);
            minHeapSize--;
            maxHeapSize++;
        }
    }

    private void pruneHeap(PriorityQueue<Integer> heap) {
        // Remove ghost elements from the root
        while (!heap.isEmpty() && delayedMap.containsKey(heap.peek())) {
            int ghost = heap.poll();
            int newCount = delayedMap.get(ghost) - 1;
            if (newCount == 0) {
                delayedMap.remove(ghost);
            } else {
                delayedMap.put(ghost, newCount);
            }
        }
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Brute Force — Sort Each Window
    // ─────────────────────────────────────────────
    public static double[] medianBruteForce(int[] nums, int k) {

        int n = nums.length;
        double[] result = new double[n - k + 1];

        for (int i = 0; i <= n - k; i++) {
            int[] window = Arrays.copyOfRange(nums, i, i + k);
            Arrays.sort(window);

            if (k % 2 == 1) {
                result[i] = window[k / 2];
            } else {
                result[i] = window[k / 2 - 1] / 2.0 + window[k / 2] / 2.0;
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the heaps and lazy deletion evolve
    // ─────────────────────────────────────────────
    public double[] medianWithTrace(int[] nums, int k) {

        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
        delayedMap = new HashMap<>();
        maxHeapSize = 0;
        minHeapSize = 0;

        int n = nums.length;
        double[] result = new double[n - k + 1];

        System.out.println("Array: " + Arrays.toString(nums) + "  K=" + k);
        System.out.println("═══════════════════════════════════════════");

        // Initialize first window
        int[] sorted = Arrays.copyOfRange(nums, 0, k);
        Arrays.sort(sorted);
        for (int i = 0; i < (k + 1) / 2; i++) {
            maxHeap.offer(sorted[i]);
            maxHeapSize++;
        }
        for (int i = (k + 1) / 2; i < k; i++) {
            minHeap.offer(sorted[i]);
            minHeapSize++;
        }

        result[0] = getMedian(k);

        System.out.println("Window 0: " + windowString(nums, 0, k));
        printState(result[0]);

        for (int i = k; i < n; i++) {
            int outgoing = nums[i - k];
            int incoming = nums[i];

            System.out.println("───────────────────────────────────────");
            System.out.println("Window " + (i - k + 1) + ": " + windowString(nums, i - k + 1, k));
            System.out.println("  Outgoing: " + outgoing + "  Incoming: " + incoming);

            // Lazy delete
            delayedMap.merge(outgoing, 1, Integer::sum);
            if (!maxHeap.isEmpty() && outgoing <= maxHeap.peek()) {
                maxHeapSize--;
                System.out.println("  → Mark " + outgoing 
                    + " for lazy deletion (from maxHeap) maxSize=" + maxHeapSize);
            } else {
                minHeapSize--;
                System.out.println("  → Mark " + outgoing 
                    + " for lazy deletion (from minHeap) minSize=" + minHeapSize);
            }

            // Add incoming
            if (!maxHeap.isEmpty() && incoming <= maxHeap.peek()) {
                maxHeap.offer(incoming);
                maxHeapSize++;
                System.out.println("  → Add " + incoming 
                    + " to maxHeap. maxSize=" + maxHeapSize);
            } else {
                minHeap.offer(incoming);
                minHeapSize++;
                System.out.println("  → Add " + incoming 
                    + " to minHeap. minSize=" + minHeapSize);
            }

            // Rebalance
            rebalance();

            // Prune
            pruneHeap(maxHeap);
            pruneHeap(minHeap);

            result[i - k + 1] = getMedian(k);
            printState(result[i - k + 1]);
        }

        System.out.println("═══════════════════════════════════════════");
        System.out.println("RESULT: " + Arrays.toString(result));
        return result;
    }

    private String windowString(int[] nums, int start, int k) {
        int[] window = Arrays.copyOfRange(nums, start, start + k);
        return Arrays.toString(window);
    }

    private void printState(double median) {
        System.out.println("  maxHeap root: " 
            + (maxHeap.isEmpty() ? "empty" : maxHeap.peek())
            + " (logicalSize=" + maxHeapSize + ")");
        System.out.println("  minHeap root: " 
            + (minHeap.isEmpty() ? "empty" : minHeap.peek())
            + " (logicalSize=" + minHeapSize + ")");
        System.out.println("  Delayed: " + delayedMap);
        System.out.println("  ★ Median = " + median);
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 41: Sliding Window Median                ║");
        System.out.println("║  Pattern: Two Heaps + Lazy Deletion               ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();

        SlidingWindowMedian solver = new SlidingWindowMedian();

        // ── TEST 1: Odd K with Trace ──
        System.out.println("═══ TEST 1: Odd K with Full Trace ═══");
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        solver.medianWithTrace(nums1, 3);
        System.out.println();

        // ── TEST 2: Even K with Trace ──
        System.out.println("═══ TEST 2: Even K with Full Trace ═══");
        int[] nums2 = {1, 3, -1, -3, 5, 3};
        solver = new SlidingWindowMedian();
        solver.medianWithTrace(nums2, 4);
        System.out.println();

        // ── TEST 3: Verify Against Brute Force ──
        System.out.println("═══ TEST 3: Verify Against Brute Force ═══");
        int[][] testCases = {
            {1, 3, -1, -3, 5, 3, 6, 7},
            {1, 2, 3, 4, 2, 3, 1, 4, 2},
            {5, 5, 5, 5, 5},
            {-1, -2, -3, -4, -5},
            {1, 4, 2, 3}
        };
        int[] kValues = {3, 3, 2, 3, 4};

        for (int t = 0; t < testCases.length; t++) {
            solver = new SlidingWindowMedian();
            double[] heapResult = solver.medianSlidingWindow(testCases[t], kValues[t]);
            double[] bruteResult = medianBruteForce(testCases[t], kValues[t]);

            boolean match = true;
            for (int i = 0; i < heapResult.length; i++) {
                if (Math.abs(heapResult[i] - bruteResult[i]) > 1e-9) {
                    match = false;
                    break;
                }
            }

            System.out.println("  Test " + (t + 1) + ": " 
                + Arrays.toString(testCases[t]) + " K=" + kValues[t]);
            System.out.println("    Heap:  " + Arrays.toString(heapResult));
            System.out.println("    Brute: " + Arrays.toString(bruteResult));
            System.out.println("    Match: " + (match ? "✓" : "✗"));
        }
        System.out.println();

        // ── TEST 4: All Same Values ──
        System.out.println("═══ TEST 4: All Same Values ═══");
        int[] nums4 = {7, 7, 7, 7, 7, 7};
        solver = new SlidingWindowMedian();
        double[] res4 = solver.medianSlidingWindow(nums4, 3);
        System.out.println("Array: " + Arrays.toString(nums4) + " K=3");
        System.out.println("Result: " + Arrays.toString(res4));
        System.out.println();

        // ── TEST 5: K = 1 (each element is its own median) ──
        System.out.println("═══ TEST 5: K=1 (Each Element is Median) ═══");
        int[] nums5 = {5, 3, 8, 1, 7};
        solver = new SlidingWindowMedian();
        double[] res5 = solver.medianSlidingWindow(nums5, 1);
        System.out.println("Array: " + Arrays.toString(nums5) + " K=1");
        System.out.println("Result: " + Arrays.toString(res5));
        System.out.println();

        // ── TEST 6: K = array length (single median) ──
        System.out.println("═══ TEST 6: K=n (Single Median) ═══");
        int[] nums6 = {3, 1, 4, 1, 5};
        solver = new SlidingWindowMedian();
        double[] res6 = solver.medianSlidingWindow(nums6, 5);
        System.out.println("Array: " + Arrays.toString(nums6) + " K=5");
        System.out.println("Result: " + Arrays.toString(res6));
        System.out.println();

        // ── TEST 7: Large Negative Values (overflow test) ──
        System.out.println("═══ TEST 7: Large Values (Overflow Test) ═══");
        int[] nums7 = {
            Integer.MAX_VALUE, Integer.MAX_VALUE, 
            Integer.MAX_VALUE, Integer.MAX_VALUE
        };
        solver = new SlidingWindowMedian();
        double[] res7 = solver.medianSlidingWindow(nums7, 2);
        System.out.println("Array: [MAX_INT, MAX_INT, MAX_INT, MAX_INT] K=2");
        System.out.println("Result: " + Arrays.toString(res7));
        System.out.println("Expected: all " + (double) Integer.MAX_VALUE);
        System.out.println();

        // ── TEST 8: Performance Comparison ──
        System.out.println("═══ TEST 8: Performance Comparison ═══");
        int size = 100_000;
        int kPerf = 1000;
        int[] largeArr = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(1_000_000) - 500_000;
        }
        System.out.println("Array size: " + size + ", K: " + kPerf);

        // Two heaps + lazy deletion
        solver = new SlidingWindowMedian();
        long start = System.nanoTime();
        double[] resHeap = solver.medianSlidingWindow(largeArr, kPerf);
        long heapTime = System.nanoTime() - start;
        System.out.println("  Two Heaps + Lazy: " + (heapTime / 1_000_000) 
            + " ms (medians=" + resHeap.length + ")");

        // Brute force
        start = System.nanoTime();
        double[] resBrute = medianBruteForce(largeArr, kPerf);
        long bruteTime = System.nanoTime() - start;
        System.out.println("  Brute Force:      " + (bruteTime / 1_000_000) 
            + " ms (medians=" + resBrute.length + ")");

        // Verify match
        boolean allMatch = true;
        for (int i = 0; i < resHeap.length; i++) {
            if (Math.abs(resHeap[i] - resBrute[i]) > 1e-9) {
                allMatch = false;
                System.out.println("  MISMATCH at index " + i + ": heap=" 
                    + resHeap[i] + " brute=" + resBrute[i]);
                break;
            }
        }
        System.out.println("  Results match: " + (allMatch ? "✓" : "✗"));
        System.out.println("  Speedup: " 
            + String.format("%.1f", (double) bruteTime / heapTime) + "x");
    }
}