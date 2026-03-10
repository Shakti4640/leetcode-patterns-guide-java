import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class KthLargestElement {

    // ─────────────────────────────────────────────
    // SOLUTION 1: Min-Heap of Size K (OPTIMAL for Top K pattern)
    // ─────────────────────────────────────────────
    public static int findKthLargest(int[] nums, int k) {

        // Min-heap → Java PriorityQueue default
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {

            if (minHeap.size() < k) {
                // Club not full → let everyone in
                minHeap.offer(num);
            } 
            else if (num > minHeap.peek()) {
                // New element beats the gatekeeper → swap
                minHeap.poll();
                minHeap.offer(num);
            }
            // else: num ≤ gatekeeper → rejected → skip
        }

        // Root of min-heap = smallest of top K = Kth largest
        return minHeap.peek();
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Simplified Version (add then trim)
    // ─────────────────────────────────────────────
    public static int findKthLargestSimplified(int[] nums, int k) {

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            minHeap.offer(num);

            // If heap exceeds size K → remove smallest
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        return minHeap.peek();
    }

    // ─────────────────────────────────────────────
    // SOLUTION 3: Sorting Approach (for comparison)
    // ─────────────────────────────────────────────
    public static int findKthLargestBySort(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // ─────────────────────────────────────────────
    // SOLUTION 4: Max-Heap Extract K Times (for comparison)
    // ─────────────────────────────────────────────
    public static int findKthLargestMaxHeap(int[] nums, int k) {

        // Max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        // Add ALL elements
        for (int num : nums) {
            maxHeap.offer(num);
        }

        // Extract K-1 times (discard them)
        for (int i = 0; i < k - 1; i++) {
            maxHeap.poll();
        }

        // Kth extraction = answer
        return maxHeap.peek();
    }

    // ─────────────────────────────────────────────
    // SOLUTION 5: QuickSelect (O(n) average) — for awareness
    // ─────────────────────────────────────────────
    public static int findKthLargestQuickSelect(int[] nums, int k) {

        // Kth largest = element at index (n - k) in sorted order
        int targetIndex = nums.length - k;
        return quickSelect(nums, 0, nums.length - 1, targetIndex);
    }

    private static int quickSelect(int[] nums, int lo, int hi, int targetIndex) {

        if (lo == hi) {
            return nums[lo];
        }

        // Partition around a pivot
        int pivotIndex = partition(nums, lo, hi);

        if (pivotIndex == targetIndex) {
            return nums[pivotIndex];
        } 
        else if (pivotIndex < targetIndex) {
            return quickSelect(nums, pivotIndex + 1, hi, targetIndex);
        } 
        else {
            return quickSelect(nums, lo, pivotIndex - 1, targetIndex);
        }
    }

    private static int partition(int[] nums, int lo, int hi) {

        // Random pivot to avoid O(n²) worst case
        Random rand = new Random();
        int randomIndex = lo + rand.nextInt(hi - lo + 1);
        swap(nums, randomIndex, hi);

        int pivot = nums[hi];
        int storeIndex = lo;

        for (int i = lo; i < hi; i++) {
            if (nums[i] < pivot) {
                swap(nums, i, storeIndex);
                storeIndex++;
            }
        }

        swap(nums, storeIndex, hi);
        return storeIndex;
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the min-heap evolve
    // ─────────────────────────────────────────────
    public static void findKthLargestWithTrace(int[] nums, int k) {

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("K = " + k);
        System.out.println("─────────────────────────────────");

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int step = 1;

        for (int num : nums) {

            System.out.println("Step " + step + ": Process element " + num);

            if (minHeap.size() < k) {
                minHeap.offer(num);
                System.out.println("  → Heap not full (size " + minHeap.size() 
                    + " ≤ " + k + ") → ADD");
            } 
            else if (num > minHeap.peek()) {
                int removed = minHeap.poll();
                minHeap.offer(num);
                System.out.println("  → " + num + " > gatekeeper(" + removed 
                    + ") → SWAP: remove " + removed + ", add " + num);
            } 
            else {
                System.out.println("  → " + num + " ≤ gatekeeper(" 
                    + minHeap.peek() + ") → REJECTED");
            }

            // Show current heap state
            Integer[] heapArr = minHeap.toArray(new Integer[0]);
            Arrays.sort(heapArr);
            System.out.println("  → Heap contents (sorted): " + Arrays.toString(heapArr));
            System.out.println("  → Gatekeeper (root): " + minHeap.peek());
            System.out.println();

            step++;
        }

        System.out.println("═══════════════════════════════════");
        System.out.println("ANSWER: " + k + "th largest = " + minHeap.peek());
        System.out.println("═══════════════════════════════════");
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find Top K Elements (return all K, not just Kth)
    // ─────────────────────────────────────────────
    public static int[] findTopKElements(int[] nums, int k) {

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Drain heap into result array
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll();
        }
        // result is now sorted descending
        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Kth Smallest (flip to max-heap of size K)
    // ─────────────────────────────────────────────
    public static int findKthSmallest(int[] nums, int k) {

        // Max-heap of size K for "K smallest"
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        for (int num : nums) {
            if (maxHeap.size() < k) {
                maxHeap.offer(num);
            } 
            else if (num < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.offer(num);
            }
        }

        return maxHeap.peek();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 38: Kth Largest Element in Array     ║");
        System.out.println("║  Pattern: Top K → Min-Heap of Size K          ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic Example with Trace ──
        System.out.println("═══ TEST 1: Basic Example with Trace ═══");
        int[] arr1 = {3, 2, 1, 5, 6, 4};
        findKthLargestWithTrace(arr1, 2);
        System.out.println();

        // ── TEST 2: Duplicates ──
        System.out.println("═══ TEST 2: Duplicates ═══");
        int[] arr2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        findKthLargestWithTrace(arr2, 4);
        System.out.println();

        // ── TEST 3: Negative Numbers ──
        System.out.println("═══ TEST 3: Negative Numbers ═══");
        int[] arr3 = {-5, -3, -8, -1, -7};
        findKthLargestWithTrace(arr3, 2);
        System.out.println();

        // ── TEST 4: All Solutions Agree ──
        System.out.println("═══ TEST 4: Verify All Solutions Agree ═══");
        int[] arr4 = {7, 10, 4, 3, 20, 15};
        int k4 = 3;
        System.out.println("Array: " + Arrays.toString(arr4) + ", K=" + k4);
        System.out.println("  Min-Heap (Gatekeeper): " + findKthLargest(arr4.clone(), k4));
        System.out.println("  Min-Heap (Simplified): " + findKthLargestSimplified(arr4.clone(), k4));
        System.out.println("  Sorting Approach:      " + findKthLargestBySort(arr4.clone(), k4));
        System.out.println("  Max-Heap (Extract K):  " + findKthLargestMaxHeap(arr4.clone(), k4));
        System.out.println("  QuickSelect:           " + findKthLargestQuickSelect(arr4.clone(), k4));
        System.out.println();

        // ── TEST 5: Top K Elements Variant ──
        System.out.println("═══ TEST 5: Top K Elements (Return All K) ═══");
        int[] arr5 = {3, 2, 1, 5, 6, 4};
        int k5 = 3;
        int[] topK = findTopKElements(arr5, k5);
        System.out.println("Array: " + Arrays.toString(arr5));
        System.out.println("Top " + k5 + " elements: " + Arrays.toString(topK));
        System.out.println();

        // ── TEST 6: Kth Smallest Variant ──
        System.out.println("═══ TEST 6: Kth Smallest (Max-Heap of Size K) ═══");
        int[] arr6 = {7, 10, 4, 3, 20, 15};
        int k6 = 3;
        System.out.println("Array: " + Arrays.toString(arr6));
        System.out.println(k6 + "rd smallest: " + findKthSmallest(arr6, k6));
        System.out.println("Verify: sorted = " + Arrays.toString(
            Arrays.stream(arr6.clone()).sorted().toArray()));
        System.out.println();

        // ── TEST 7: Performance Comparison ──
        System.out.println("═══ TEST 7: Performance Comparison ═══");
        int size = 5_000_000;
        int kPerf = 100;
        Random rand = new Random(42);
        int[] largeArr = new int[size];
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(10_000_000);
        }
        System.out.println("Array size: " + size + ", K: " + kPerf);

        // Min-Heap K
        int[] copy1 = largeArr.clone();
        long start = System.nanoTime();
        int res1 = findKthLargest(copy1, kPerf);
        long heapTime = System.nanoTime() - start;
        System.out.println("  Min-Heap (K):   " + res1 
            + " → " + (heapTime / 1_000_000) + " ms");

        // Sorting
        int[] copy2 = largeArr.clone();
        start = System.nanoTime();
        int res2 = findKthLargestBySort(copy2, kPerf);
        long sortTime = System.nanoTime() - start;
        System.out.println("  Sorting:        " + res2 
            + " → " + (sortTime / 1_000_000) + " ms");

        // QuickSelect
        int[] copy3 = largeArr.clone();
        start = System.nanoTime();
        int res3 = findKthLargestQuickSelect(copy3, kPerf);
        long qsTime = System.nanoTime() - start;
        System.out.println("  QuickSelect:    " + res3 
            + " → " + (qsTime / 1_000_000) + " ms");

        // Max-Heap full
        int[] copy4 = largeArr.clone();
        start = System.nanoTime();
        int res4 = findKthLargestMaxHeap(copy4, kPerf);
        long maxHeapTime = System.nanoTime() - start;
        System.out.println("  Max-Heap (all): " + res4 
            + " → " + (maxHeapTime / 1_000_000) + " ms");

        System.out.println();
        System.out.println("  Heap speedup vs Sort: " 
            + String.format("%.1f", (double) sortTime / heapTime) + "x");
        System.out.println("  Heap speedup vs Max-Heap: " 
            + String.format("%.1f", (double) maxHeapTime / heapTime) + "x");

        // ── TEST 8: Edge Cases ──
        System.out.println();
        System.out.println("═══ TEST 8: Edge Cases ═══");

        // Single element
        System.out.println("Single element [42], K=1: " 
            + findKthLargest(new int[]{42}, 1));

        // All same
        System.out.println("All same [7,7,7,7], K=3: " 
            + findKthLargest(new int[]{7, 7, 7, 7}, 3));

        // K = n (find minimum)
        System.out.println("K=n (find min) [5,3,8,1,7], K=5: " 
            + findKthLargest(new int[]{5, 3, 8, 1, 7}, 5));

        // K = 1 (find maximum)
        System.out.println("K=1 (find max) [5,3,8,1,7], K=1: " 
            + findKthLargest(new int[]{5, 3, 8, 1, 7}, 1));
    }
}