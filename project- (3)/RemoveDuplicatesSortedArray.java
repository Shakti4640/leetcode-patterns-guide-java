public class RemoveDuplicatesSortedArray {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Read/Write Pointer Technique
    // ─────────────────────────────────────────────
    public static int removeDuplicates(int[] nums) {

        // Edge case: empty array
        if (nums.length == 0) return 0;

        // Write pointer: marks the last position of a unique value
        // First element is always unique → write starts at 0
        int write = 0;

        // Read pointer: scans every element starting from index 1
        for (int read = 1; read < nums.length; read++) {

            // Compare current element with last unique element
            if (nums[read] != nums[write]) {

                // NEW unique value found
                // Step 1: Advance write to the next empty slot
                write++;

                // Step 2: Place the new unique value there
                nums[write] = nums[read];
            }
            // If nums[read] == nums[write] → duplicate → skip (do nothing)
        }

        // write is 0-based index → count = write + 1
        return write + 1;
    }

    // ─────────────────────────────────────────────
    // BRUTE FORCE: Using extra ArrayList
    // ─────────────────────────────────────────────
    public static int removeDuplicatesBruteForce(int[] nums) {

        if (nums.length == 0) return 0;

        java.util.ArrayList<Integer> unique = new java.util.ArrayList<>();
        unique.add(nums[0]);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i - 1]) {
                unique.add(nums[i]);
            }
        }

        // Copy back to original array
        for (int i = 0; i < unique.size(); i++) {
            nums[i] = unique.get(i);
        }

        return unique.size();
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Remove duplicates allowing at most K occurrences
    // ─────────────────────────────────────────────
    public static int removeDuplicatesAllowK(int[] nums, int k) {

        // If array length is ≤ k → no duplicates can exceed limit
        if (nums.length <= k) return nums.length;

        // Write pointer starts at k (first k elements are always valid)
        int write = k;

        // Read starts at k
        for (int read = k; read < nums.length; read++) {

            // Compare with the element K positions BEFORE write
            // If different → this occurrence is within the allowed K count
            if (nums[read] != nums[write - k]) {
                nums[write] = nums[read];
                write++;
            }
        }

        return write;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Remove all instances of a specific value
    // ─────────────────────────────────────────────
    public static int removeElement(int[] nums, int val) {

        int write = 0;

        for (int read = 0; read < nums.length; read++) {
            // Keep everything that is NOT the target value
            if (nums[read] != val) {
                nums[write] = nums[read];
                write++;
            }
        }

        return write;
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Move all zeros to the end
    // ─────────────────────────────────────────────
    public static void moveZeros(int[] nums) {

        // Write pointer tracks where the next non-zero should go
        int write = 0;

        // Phase 1: Move all non-zero elements to the front
        for (int read = 0; read < nums.length; read++) {
            if (nums[read] != 0) {
                nums[write] = nums[read];
                write++;
            }
        }

        // Phase 2: Fill remaining positions with zeros
        while (write < nums.length) {
            nums[write] = 0;
            write++;
        }
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the zones evolve
    // ─────────────────────────────────────────────
    public static void removeDuplicatesWithTrace(int[] nums) {

        System.out.println("Input: " + arrayToString(nums));
        System.out.println("─────────────────────────────────────");

        if (nums.length == 0) {
            System.out.println("Empty array → return 0");
            System.out.println();
            return;
        }

        int write = 0;
        int step = 1;

        for (int read = 1; read < nums.length; read++) {

            System.out.println("Step " + step + ":");
            System.out.println("  write=" + write
                + " (val=" + nums[write] + ")"
                + "  read=" + read
                + " (val=" + nums[read] + ")");

            if (nums[read] != nums[write]) {
                write++;
                nums[write] = nums[read];
                System.out.println("  → DIFFERENT → write++, arr[" + write
                    + "] = " + nums[write]);
                System.out.println("  → Zone 1 (finalized): "
                    + zoneToString(nums, 0, write));
            } else {
                System.out.println("  → SAME → duplicate → skip");
            }

            step++;
        }

        int k = write + 1;
        System.out.println("─────────────────────────────────────");
        System.out.println("Result: k = " + k);
        System.out.println("Final array: " + arrayToString(nums));
        System.out.println("Unique portion: " + zoneToString(nums, 0, write));
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
    // HELPER: Zone to string (subset of array)
    // ─────────────────────────────────────────────
    private static String zoneToString(int[] arr, int from, int to) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = from; i <= to; i++) {
            sb.append(arr[i]);
            if (i < to) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 3: Remove Duplicates from Sorted Array   ║");
        System.out.println("║  Pattern: Two Pointers — Read/Write Technique     ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example ──
        System.out.println("═══ TEST 1: Basic Example ═══");
        removeDuplicatesWithTrace(new int[]{1, 1, 2});

        // ── TEST 2: Multiple duplicates ──
        System.out.println("═══ TEST 2: Multiple Duplicates ═══");
        removeDuplicatesWithTrace(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4});

        // ── TEST 3: All same ──
        System.out.println("═══ TEST 3: All Same Elements ═══");
        removeDuplicatesWithTrace(new int[]{1, 1, 1, 1, 1});

        // ── TEST 4: Already unique ──
        System.out.println("═══ TEST 4: Already Unique ═══");
        removeDuplicatesWithTrace(new int[]{1, 2, 3, 4, 5});

        // ── TEST 5: Single element ──
        System.out.println("═══ TEST 5: Single Element ═══");
        removeDuplicatesWithTrace(new int[]{42});

        // ── TEST 6: Empty array ──
        System.out.println("═══ TEST 6: Empty Array ═══");
        removeDuplicatesWithTrace(new int[]{});

        // ── TEST 7: Variant — Allow at most K=2 duplicates ──
        System.out.println("═══ TEST 7: Allow At Most K=2 Duplicates ═══");
        int[] arr7 = {1, 1, 1, 2, 2, 3};
        System.out.println("Input: " + arrayToString(arr7) + " with k=2");
        int k7 = removeDuplicatesAllowK(arr7, 2);
        System.out.println("Result: k = " + k7);
        System.out.print("Array: [");
        for (int i = 0; i < k7; i++) {
            System.out.print(arr7[i]);
            if (i < k7 - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        // ── TEST 8: Variant — Remove specific value ──
        System.out.println("═══ TEST 8: Remove All Instances of Value 3 ═══");
        int[] arr8 = {0, 1, 2, 3, 3, 4, 5, 3, 6};
        System.out.println("Input: " + arrayToString(arr8) + " remove val=3");
        int k8 = removeElement(arr8, 3);
        System.out.println("Result: k = " + k8);
        System.out.print("Array: [");
        for (int i = 0; i < k8; i++) {
            System.out.print(arr8[i]);
            if (i < k8 - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        // ── TEST 9: Variant — Move zeros to end ──
        System.out.println("═══ TEST 9: Move Zeros to End ═══");
        int[] arr9 = {0, 1, 0, 3, 12};
        System.out.println("Input: " + arrayToString(arr9));
        moveZeros(arr9);
        System.out.println("After: " + arrayToString(arr9));
        System.out.println();

        // ── TEST 10: Performance test ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        int size = 1000000;
        int[] largeArr1 = new int[size];
        int[] largeArr2 = new int[size];
        // Fill with many duplicates: 0,0,0,...,1,1,1,...,2,2,2,...
        for (int i = 0; i < size; i++) {
            largeArr1[i] = i / 1000;  // each value repeated 1000 times
            largeArr2[i] = i / 1000;
        }

        long startTime = System.nanoTime();
        int r1 = removeDuplicates(largeArr1);
        long readWriteTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int r2 = removeDuplicatesBruteForce(largeArr2);
        long bruteTime = System.nanoTime() - startTime;

        System.out.println("Array size: " + size);
        System.out.println("Read/Write: k=" + r1 + " → "
            + (readWriteTime / 1_000_000) + " ms");
        System.out.println("Brute Force: k=" + r2 + " → "
            + (bruteTime / 1_000_000) + " ms");
        System.out.println();
    }
}