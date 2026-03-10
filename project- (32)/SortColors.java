import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class SortColors {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Dutch National Flag — Three Pointers
    // ─────────────────────────────────────────────
    public static void sortColors(int[] nums) {

        int low = 0;              // Next position for 0
        int mid = 0;              // Current element being examined
        int high = nums.length - 1; // Next position for 2

        while (mid <= high) {

            if (nums[mid] == 0) {
                // Belongs in 0-region → swap with low boundary
                swap(nums, low, mid);
                low++;   // 0-region expands
                mid++;   // Safe: element from low was already classified (0 or 1)
            }
            else if (nums[mid] == 1) {
                // Already in correct region → just advance
                mid++;
            }
            else {
                // nums[mid] == 2 → belongs in 2-region → swap with high boundary
                swap(nums, mid, high);
                high--;  // 2-region expands
                // mid stays! Element from high is UNKNOWN → must re-examine
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // ─────────────────────────────────────────────
    // COUNTING SORT APPROACH (Two-pass, for comparison)
    // ─────────────────────────────────────────────
    public static void sortColorsCountingSort(int[] nums) {

        // Pass 1: Count occurrences
        int count0 = 0, count1 = 0, count2 = 0;
        for (int num : nums) {
            if (num == 0) count0++;
            else if (num == 1) count1++;
            else count2++;
        }

        // Pass 2: Overwrite array
        int idx = 0;
        for (int i = 0; i < count0; i++) nums[idx++] = 0;
        for (int i = 0; i < count1; i++) nums[idx++] = 1;
        for (int i = 0; i < count2; i++) nums[idx++] = 2;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION: Visualize every step
    // ─────────────────────────────────────────────
    public static void sortColorsWithTrace(int[] nums) {

        System.out.print("Original: ");
        printArray(nums);
        System.out.println("─────────────────────────────────────────");

        int low = 0;
        int mid = 0;
        int high = nums.length - 1;
        int step = 1;

        printState(nums, low, mid, high, step, "INITIAL");

        while (mid <= high) {

            String action;
            if (nums[mid] == 0) {
                action = "nums[mid]=" + nums[mid]
                        + " → SWAP with low"
                        + " → swap(" + mid + "," + low + ")"
                        + " → low++, mid++";
                swap(nums, low, mid);
                low++;
                mid++;
            }
            else if (nums[mid] == 1) {
                action = "nums[mid]=" + nums[mid]
                        + " → KEEP in place"
                        + " → mid++";
                mid++;
            }
            else {
                action = "nums[mid]=" + nums[mid]
                        + " → SWAP with high"
                        + " → swap(" + mid + "," + high + ")"
                        + " → high-- (mid stays!)";
                swap(nums, mid, high);
                high--;
            }

            printState(nums, low, mid, high, step, action);
            step++;
        }

        System.out.print("Final:    ");
        printArray(nums);
        System.out.println();
    }

    private static void printState(int[] nums, int low, int mid, int high,
                                   int step, String action) {
        System.out.println("Step " + step + ": " + action);
        System.out.print("  Array: ");
        printArray(nums);
        System.out.println("  low=" + low + "  mid=" + mid + "  high=" + high);

        // Print regions
        System.out.print("  0s=[");
        for (int i = 0; i < low; i++) {
            System.out.print(nums[i]);
            if (i < low - 1) System.out.print(",");
        }
        System.out.print("]  1s=[");
        for (int i = low; i < mid; i++) {
            System.out.print(nums[i]);
            if (i < mid - 1) System.out.print(",");
        }
        System.out.print("]  ?=[");
        for (int i = mid; i <= high && i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < high) System.out.print(",");
        }
        System.out.print("]  2s=[");
        for (int i = high + 1; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(",");
        }
        System.out.println("]");
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // VARIANT: Three-Way Partition Around a Pivot
    //   → Elements < pivot go left
    //   → Elements == pivot stay middle
    //   → Elements > pivot go right
    // ─────────────────────────────────────────────
    public static void threeWayPartition(int[] nums, int pivot) {

        int low = 0;
        int mid = 0;
        int high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] < pivot) {
                swap(nums, low, mid);
                low++;
                mid++;
            }
            else if (nums[mid] == pivot) {
                mid++;
            }
            else {
                swap(nums, mid, high);
                high--;
            }
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Move Zeros to End (Two-value partition)
    //   → Simpler case: only 2 categories
    //   → Shows how Dutch National Flag simplifies for 2 values
    // ─────────────────────────────────────────────
    public static void moveZerosToEnd(int[] nums) {

        // This is like Dutch National Flag with only 0 and non-0
        // Only need TWO pointers (no high pointer needed)
        // Same as read/write from Project 3
        int writePos = 0;

        for (int readPos = 0; readPos < nums.length; readPos++) {
            if (nums[readPos] != 0) {
                swap(nums, writePos, readPos);
                writePos++;
            }
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Separate Negatives, Zeros, Positives
    // ─────────────────────────────────────────────
    public static void separateBySign(int[] nums) {

        int low = 0;              // Boundary for negatives
        int mid = 0;              // Scanner
        int high = nums.length - 1; // Boundary for positives

        while (mid <= high) {
            if (nums[mid] < 0) {
                // Negative → goes to left region
                swap(nums, low, mid);
                low++;
                mid++;
            }
            else if (nums[mid] == 0) {
                // Zero → stays in middle
                mid++;
            }
            else {
                // Positive → goes to right region
                swap(nums, mid, high);
                high--;
            }
        }
    }

    // ─────────────────────────────────────────────
    // VERIFICATION: Check if result is correctly sorted
    // ─────────────────────────────────────────────
    public static boolean isCorrectlySorted(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // ─────────────────────────────────────────────
    // UTILITY: Print array
    // ─────────────────────────────────────────────
    private static void printArray(int[] nums) {
        System.out.print("[");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 32: Sort Colors — Dutch National Flag   ║");
        System.out.println("║  Pattern: Three-Pointer Partitioning             ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Standard example with trace ──
        System.out.println("═══ TEST 1: Standard Example (Traced) ═══");
        sortColorsWithTrace(new int[]{2, 0, 2, 1, 1, 0});

        // ── TEST 2: Another example with trace ──
        System.out.println("═══ TEST 2: Mixed Array (Traced) ═══");
        sortColorsWithTrace(new int[]{1, 2, 0, 1, 2, 0, 0, 2, 1});

        // ── TEST 3: Already sorted ──
        System.out.println("═══ TEST 3: Already Sorted ═══");
        sortColorsWithTrace(new int[]{0, 0, 1, 1, 2, 2});

        // ── TEST 4: Reverse sorted ──
        System.out.println("═══ TEST 4: Reverse Sorted ═══");
        sortColorsWithTrace(new int[]{2, 2, 1, 1, 0, 0});

        // ── TEST 5: All same value ──
        System.out.println("═══ TEST 5: All Same Value ═══");
        sortColorsWithTrace(new int[]{1, 1, 1, 1});

        // ── TEST 6: Single element ──
        System.out.println("═══ TEST 6: Single Element ═══");
        sortColorsWithTrace(new int[]{2});

        // ── TEST 7: Two elements ──
        System.out.println("═══ TEST 7: Two Elements (Swap Needed) ═══");
        sortColorsWithTrace(new int[]{2, 0});

        // ── TEST 8: Only 0s and 2s ──
        System.out.println("═══ TEST 8: Only 0s and 2s ═══");
        sortColorsWithTrace(new int[]{2, 0, 2, 0, 2, 0});

        // ── TEST 9: Three-Way Partition Variant ──
        System.out.println("═══ TEST 9: Three-Way Partition Around Pivot=5 ═══");
        int[] partArr = {8, 3, 5, 1, 5, 9, 2, 5, 7};
        System.out.print("Before: ");
        printArray(partArr);
        threeWayPartition(partArr, 5);
        System.out.print("After:  ");
        printArray(partArr);
        System.out.println("  → [<5 region] [==5 region] [>5 region]");
        System.out.println();

        // ── TEST 10: Separate By Sign Variant ──
        System.out.println("═══ TEST 10: Separate Negatives/Zeros/Positives ═══");
        int[] signArr = {3, -2, 0, -5, 7, 0, -1, 4};
        System.out.print("Before: ");
        printArray(signArr);
        separateBySign(signArr);
        System.out.print("After:  ");
        printArray(signArr);
        System.out.println("  → [negatives] [zeros] [positives]");
        System.out.println();

        // ── TEST 11: Move Zeros Variant ──
        System.out.println("═══ TEST 11: Move Zeros to End ═══");
        int[] zeroArr = {0, 1, 0, 3, 12, 0, 5};
        System.out.print("Before: ");
        printArray(zeroArr);
        moveZerosToEnd(zeroArr);
        System.out.print("After:  ");
        printArray(zeroArr);
        System.out.println();

        // ── TEST 12: Stress Test — Correctness Verification ──
        System.out.println("═══ TEST 12: Stress Test (10,000 random arrays) ═══");
        java.util.Random rand = new java.util.Random(42);
        int testCount = 10000;
        int passed = 0;

        for (int t = 0; t < testCount; t++) {
            int size = rand.nextInt(100) + 1;
            int[] testArr = new int[size];
            for (int i = 0; i < size; i++) {
                testArr[i] = rand.nextInt(3); // 0, 1, or 2
            }

            // Make a copy for verification
            int[] expected = testArr.clone();
            Arrays.sort(expected);

            // Apply our algorithm
            sortColors(testArr);

            if (Arrays.equals(testArr, expected)) {
                passed++;
            } else {
                System.out.println("  FAILED on: " + Arrays.toString(testArr));
            }
        }

        System.out.println("  Result: " + passed + "/" + testCount + " tests passed"
                + (passed == testCount ? " ✓ ALL CORRECT" : " ✗ SOME FAILED"));
        System.out.println();

        // ── TEST 13: Performance Comparison ──
        System.out.println("═══ TEST 13: Performance Comparison ═══");
        int perfSize = 10_000_000;

        // Generate large array
        int[] perfArr1 = new int[perfSize];
        int[] perfArr2 = new int[perfSize];
        int[] perfArr3 = new int[perfSize];
        for (int i = 0; i < perfSize; i++) {
            int val = rand.nextInt(3);
            perfArr1[i] = val;
            perfArr2[i] = val;
            perfArr3[i] = val;
        }

        System.out.println("Array size: " + String.format("%,d", perfSize));

        // Dutch National Flag
        long start = System.nanoTime();
        sortColors(perfArr1);
        long dnfTime = System.nanoTime() - start;

        // Counting Sort (two-pass)
        start = System.nanoTime();
        sortColorsCountingSort(perfArr2);
        long countTime = System.nanoTime() - start;

        // Arrays.sort
        start = System.nanoTime();
        Arrays.sort(perfArr3);
        long sortTime = System.nanoTime() - start;

        System.out.println("Dutch National Flag: "
                + String.format("%,d", dnfTime / 1000) + " μs (single pass, O(n))");
        System.out.println("Counting Sort:       "
                + String.format("%,d", countTime / 1000) + " μs (two pass, O(n))");
        System.out.println("Arrays.sort:         "
                + String.format("%,d", sortTime / 1000) + " μs (O(n log n))");

        // Verify all produce same result
        System.out.println("All results match: "
                + (Arrays.equals(perfArr1, perfArr2)
                && Arrays.equals(perfArr2, perfArr3) ? "✓ YES" : "✗ NO"));
    }
}