import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindAllDuplicates {

    // ═══════════════════════════════════════════════
    // SOLUTION 1: NEGATIVE MARKING
    // O(n) time, O(1) space — Cleanest approach
    // ═══════════════════════════════════════════════
    public static List<Integer> findDuplicatesNegativeMarking(int[] nums) {

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {

            // Step 1: Get the TRUE value (may have been negated)
            int val = Math.abs(nums[i]);

            // Step 2: Compute home index
            int homeIdx = val - 1;

            // Step 3: Check and mark
            if (nums[homeIdx] < 0) {
                // Already marked → val is a DUPLICATE
                result.add(val);
            } else {
                // First time seeing val → mark by negating
                nums[homeIdx] = -nums[homeIdx];
            }
        }

        // Optional: restore array to original state
        for (int i = 0; i < nums.length; i++) {
            nums[i] = Math.abs(nums[i]);
        }

        return result;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 2: CYCLIC SORT PLACEMENT
    // O(n) time, O(1) space — More intuitive
    // ═══════════════════════════════════════════════
    public static List<Integer> findDuplicatesCyclicSort(int[] nums) {

        List<Integer> result = new ArrayList<>();

        // Phase 1: Cyclic sort — place each value at its home
        for (int i = 0; i < nums.length; i++) {

            // Keep swapping until current value is at home
            // OR home already has the correct value (duplicate case)
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }

        // Phase 2: Scan for mismatches
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                // Value at index i is not its expected value
                // nums[i] is a duplicate (it's homeless)
                result.add(nums[i]);
            }
        }

        return result;
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // ═══════════════════════════════════════════════
    // SOLUTION 3: HASHSET — For comparison / verification
    // O(n) time, O(n) space
    // ═══════════════════════════════════════════════
    public static List<Integer> findDuplicatesHashSet(int[] nums) {

        List<Integer> result = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            if (seen.contains(num)) {
                result.add(num);
            } else {
                seen.add(num);
            }
        }

        return result;
    }

    // ═══════════════════════════════════════════════
    // TRACE: Negative Marking — Full visualization
    // ═══════════════════════════════════════════════
    public static void negativeMarkingWithTrace(int[] nums) {

        // Work on a copy to preserve original
        int[] arr = nums.clone();
        List<Integer> result = new ArrayList<>();

        System.out.println("  Initial array:");
        printArray("  ", arr);
        System.out.println();

        for (int i = 0; i < arr.length; i++) {

            int val = Math.abs(arr[i]);
            int homeIdx = val - 1;

            System.out.print("  i=" + i
                    + " | raw=" + arr[i]
                    + " | val=|" + arr[i] + "|=" + val
                    + " | homeIdx=" + homeIdx
                    + " | arr[" + homeIdx + "]=" + arr[homeIdx]);

            if (arr[homeIdx] < 0) {
                result.add(val);
                System.out.println(" → NEGATIVE → " + val + " is DUPLICATE ★");
            } else {
                arr[homeIdx] = -arr[homeIdx];
                System.out.println(" → positive → negate → arr[" + homeIdx
                        + "]=" + arr[homeIdx]);
            }

            printArray("    State: ", arr);
        }

        System.out.println();
        System.out.println("  Duplicates found: " + result);
    }

    // ═══════════════════════════════════════════════
    // TRACE: Cyclic Sort — Full visualization
    // ═══════════════════════════════════════════════
    public static void cyclicSortWithTrace(int[] nums) {

        int[] arr = nums.clone();
        List<Integer> result = new ArrayList<>();
        int totalSwaps = 0;

        System.out.println("  Initial array:");
        printArray("  ", arr);
        System.out.println();

        System.out.println("  ═══ Phase 1: Cyclic Sort ═══");

        for (int i = 0; i < arr.length; i++) {

            System.out.println("  i=" + i + " | arr[" + i + "]=" + arr[i]
                    + " | should be at index " + (arr[i] - 1));

            while (arr[i] != arr[arr[i] - 1]) {

                int targetIdx = arr[i] - 1;
                System.out.println("    Swap arr[" + i + "]=" + arr[i]
                        + " with arr[" + targetIdx + "]=" + arr[targetIdx]);
                swap(arr, i, targetIdx);
                totalSwaps++;
                printArray("    State: ", arr);
            }

            if (arr[i] == i + 1) {
                System.out.println("    → arr[" + i + "]=" + arr[i]
                        + " is at home ✓");
            } else {
                System.out.println("    → arr[" + i + "]=" + arr[i]
                        + " blocked (home occupied by same value) → skip");
            }
        }

        System.out.println("  Total swaps: " + totalSwaps);
        System.out.println();
        System.out.println("  ═══ Phase 2: Detect Duplicates ═══");
        printArray("  Sorted: ", arr);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != i + 1) {
                result.add(arr[i]);
                System.out.println("  Index " + i + ": expected " + (i + 1)
                        + " but found " + arr[i] + " → DUPLICATE ★");
            } else {
                System.out.println("  Index " + i + ": " + arr[i] + " ✓");
            }
        }

        System.out.println();
        System.out.println("  Duplicates found: " + result);
    }

    // ═══════════════════════════════════════════════
    // BONUS: Find BOTH duplicates AND missing numbers
    // Using negative marking — single pass
    // ═══════════════════════════════════════════════
    public static void findDuplicatesAndMissing(int[] nums) {

        int[] arr = nums.clone();
        List<Integer> duplicates = new ArrayList<>();
        List<Integer> missing = new ArrayList<>();

        // Pass 1: Mark and detect duplicates
        for (int i = 0; i < arr.length; i++) {
            int val = Math.abs(arr[i]);
            int homeIdx = val - 1;

            if (arr[homeIdx] < 0) {
                duplicates.add(val);
            } else {
                arr[homeIdx] = -arr[homeIdx];
            }
        }

        // Pass 2: Find missing numbers (indices still positive)
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                missing.add(i + 1);
            }
        }

        System.out.println("  Duplicates: " + duplicates);
        System.out.println("  Missing:    " + missing);
    }

    // ─────────────────────────────────────────────
    // UTILITY: Print array
    // ─────────────────────────────────────────────
    private static void printArray(String prefix, int[] arr) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(String.format("%3d", arr[i]));
            if (i < arr.length - 1) sb.append(",");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 21: Find All Duplicate Numbers in Array       ║");
        System.out.println("║  Pattern: Cyclic Sort → Negative Marking / Placement   ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Negative marking trace ──
        System.out.println("═══ TEST 1: Negative Marking Trace ═══");
        int[] test1 = {4, 3, 2, 7, 8, 2, 3, 1};
        negativeMarkingWithTrace(test1);
        System.out.println();

        // ── TEST 2: Cyclic sort trace ──
        System.out.println("═══ TEST 2: Cyclic Sort Trace ═══");
        int[] test2 = {4, 3, 2, 7, 8, 2, 3, 1};
        cyclicSortWithTrace(test2);
        System.out.println();

        // ── TEST 3: Small example ──
        System.out.println("═══ TEST 3: Small Example — [1, 1, 2] ═══");
        int[] test3 = {1, 1, 2};
        System.out.println("  Negative Marking: "
                + findDuplicatesNegativeMarking(test3.clone()));
        System.out.println("  Cyclic Sort:      "
                + findDuplicatesCyclicSort(test3.clone()));
        System.out.println("  HashSet:          "
                + findDuplicatesHashSet(test3.clone()));
        System.out.println();

        // ── TEST 4: No duplicates ──
        System.out.println("═══ TEST 4: No Duplicates — [1, 2, 3, 4, 5] ═══");
        int[] test4 = {1, 2, 3, 4, 5};
        System.out.println("  Negative Marking: "
                + findDuplicatesNegativeMarking(test4.clone()));
        System.out.println("  Cyclic Sort:      "
                + findDuplicatesCyclicSort(test4.clone()));
        System.out.println();

        // ── TEST 5: All duplicates ──
        System.out.println("═══ TEST 5: Maximum Duplicates — [1,1,2,2,3,3] ═══");
        int[] test5 = {1, 1, 2, 2, 3, 3};
        System.out.println("  Negative Marking: "
                + findDuplicatesNegativeMarking(test5.clone()));
        System.out.println("  Cyclic Sort:      "
                + findDuplicatesCyclicSort(test5.clone()));
        System.out.println();

        // ── TEST 6: Single element ──
        System.out.println("═══ TEST 6: Single Element — [1] ═══");
        int[] test6 = {1};
        System.out.println("  Negative Marking: "
                + findDuplicatesNegativeMarking(test6.clone()));
        System.out.println();

        // ── TEST 7: Two elements, both same ──
        System.out.println("═══ TEST 7: Two Same Elements — [1, 1] ═══");
        int[] test7 = {1, 1};
        System.out.println("  Negative Marking: "
                + findDuplicatesNegativeMarking(test7.clone()));
        System.out.println("  Cyclic Sort:      "
                + findDuplicatesCyclicSort(test7.clone()));
        System.out.println();

        // ── TEST 8: Duplicates AND Missing ──
        System.out.println("═══ TEST 8: BONUS — Find Both Duplicates AND Missing ═══");
        int[] test8 = {4, 3, 2, 7, 8, 2, 3, 1};
        System.out.println("  Input: [4, 3, 2, 7, 8, 2, 3, 1]");
        System.out.println("  Range: [1, 8]");
        findDuplicatesAndMissing(test8);
        System.out.println();

        // ── TEST 9: All three approaches agree ──
        System.out.println("═══ TEST 9: Verification — All Approaches Agree ═══");
        int[][] testCases = {
            {4, 3, 2, 7, 8, 2, 3, 1},
            {1, 1, 2},
            {1, 2, 3, 4, 5},
            {1, 1, 2, 2, 3, 3},
            {1},
            {2, 2},
            {3, 3, 1, 1, 2, 5, 5, 4},
            {10, 2, 5, 10, 9, 1, 1, 4, 3, 7}
        };

        boolean allMatch = true;
        for (int t = 0; t < testCases.length; t++) {

            List<Integer> r1 = findDuplicatesNegativeMarking(testCases[t].clone());
            List<Integer> r2 = findDuplicatesCyclicSort(testCases[t].clone());
            List<Integer> r3 = findDuplicatesHashSet(testCases[t].clone());

            // Sort all results for comparison
            Collections.sort(r1);
            Collections.sort(r2);
            Collections.sort(r3);

            boolean match = r1.equals(r2) && r2.equals(r3);
            System.out.println("  Test " + (t + 1)
                    + ": NegMark=" + r1
                    + " CycSort=" + r2
                    + " HashSet=" + r3
                    + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allMatch = false;
        }
        System.out.println("  All match: " + allMatch);
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance — 10,000,000 Elements ═══");
        int size = 10_000_000;
        int[] largeArr = new int[size];

        // Fill: values [1, size], make some duplicates
        for (int i = 0; i < size; i++) {
            largeArr[i] = (i % (size / 2)) + 1;  // creates duplicates
        }

        // Negative Marking
        long start = System.nanoTime();
        List<Integer> res1 = findDuplicatesNegativeMarking(largeArr.clone());
        long negTime = System.nanoTime() - start;

        // Cyclic Sort
        start = System.nanoTime();
        List<Integer> res2 = findDuplicatesCyclicSort(largeArr.clone());
        long cycTime = System.nanoTime() - start;

        // HashSet
        start = System.nanoTime();
        List<Integer> res3 = findDuplicatesHashSet(largeArr.clone());
        long hashTime = System.nanoTime() - start;

        System.out.println("  Array size: " + size);
        System.out.println("  Duplicates found: " + res1.size());
        System.out.println("  Negative Marking: " + (negTime / 1_000_000) + " ms (O(1) space)");
        System.out.println("  Cyclic Sort:      " + (cycTime / 1_000_000) + " ms (O(1) space)");
        System.out.println("  HashSet:          " + (hashTime / 1_000_000) + " ms (O(n) space)");
    }
}