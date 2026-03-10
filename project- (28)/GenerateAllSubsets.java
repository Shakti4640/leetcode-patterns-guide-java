import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateAllSubsets {

    // ═══════════════════════════════════════════════
    // APPROACH A: Backtracking — Collect-At-Every-Node (Interview Standard)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        backtrack(nums, 0, currentSubset, results);

        return results;
    }

    private static void backtrack(int[] nums,
                                  int startIndex,
                                  List<Integer> currentSubset,
                                  List<List<Integer>> results) {

        // ── Every node in the decision tree is a valid subset ──
        results.add(new ArrayList<>(currentSubset));   // deep copy!

        // ── Try adding each remaining element ──
        for (int i = startIndex; i < nums.length; i++) {

            // CHOOSE: add nums[i]
            currentSubset.add(nums[i]);

            // EXPLORE: recurse with next startIndex = i + 1
            backtrack(nums, i + 1, currentSubset, results);

            // UN-CHOOSE: remove nums[i] → BACKTRACK
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // ═══════════════════════════════════════════════
    // APPROACH B: Backtracking — Include/Exclude Binary Decision
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsetsIncludeExclude(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        includeExclude(nums, 0, currentSubset, results);

        return results;
    }

    private static void includeExclude(int[] nums,
                                       int index,
                                       List<Integer> currentSubset,
                                       List<List<Integer>> results) {

        // ── Base case: all elements decided ──
        if (index == nums.length) {
            results.add(new ArrayList<>(currentSubset));  // LEAF → save
            return;
        }

        // ── BRANCH 1: INCLUDE nums[index] ──
        currentSubset.add(nums[index]);
        includeExclude(nums, index + 1, currentSubset, results);
        currentSubset.remove(currentSubset.size() - 1);   // BACKTRACK

        // ── BRANCH 2: EXCLUDE nums[index] ──
        includeExclude(nums, index + 1, currentSubset, results);
    }

    // ═══════════════════════════════════════════════
    // APPROACH C: Cascading (Iterative)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsetsCascading(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();

        // Start with empty subset
        results.add(new ArrayList<>());

        // For each number, expand all existing subsets
        for (int num : nums) {

            // Snapshot current size — we'll iterate over existing subsets only
            int currentSize = results.size();

            for (int j = 0; j < currentSize; j++) {

                // Copy existing subset and add the new number
                List<Integer> newSubset = new ArrayList<>(results.get(j));
                newSubset.add(num);
                results.add(newSubset);
            }
        }

        return results;
    }

    // ═══════════════════════════════════════════════
    // APPROACH D: Bit Manipulation
    // Connects to Project 10 (Bitwise XOR)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsetsBitwise(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        int n = nums.length;
        int totalSubsets = 1 << n;   // 2^n using bit shift

        // Each number from 0 to 2^n - 1 represents a subset
        for (int mask = 0; mask < totalSubsets; mask++) {

            List<Integer> subset = new ArrayList<>();

            for (int bit = 0; bit < n; bit++) {
                // Check if bit position 'bit' is set in 'mask'
                if ((mask & (1 << bit)) != 0) {
                    subset.add(nums[bit]);
                }
            }

            results.add(subset);
        }

        return results;
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Subsets of specific size K
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsetsOfSizeK(int[] nums, int k) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        backtrackSizeK(nums, 0, k, currentSubset, results);

        return results;
    }

    private static void backtrackSizeK(int[] nums,
                                       int startIndex,
                                       int k,
                                       List<Integer> currentSubset,
                                       List<List<Integer>> results) {

        // ── Collect only when size matches ──
        if (currentSubset.size() == k) {
            results.add(new ArrayList<>(currentSubset));
            return;   // no need to add more elements
        }

        // ── Pruning: not enough elements left to reach size k ──
        int remaining = nums.length - startIndex;
        int needed = k - currentSubset.size();
        if (remaining < needed) return;   // impossible to reach k → prune

        for (int i = startIndex; i < nums.length; i++) {
            currentSubset.add(nums[i]);
            backtrackSizeK(nums, i + 1, k, currentSubset, results);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Subsets with duplicates in input
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> subsetsWithDuplicates(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        // CRITICAL: Sort first to bring duplicates together
        Arrays.sort(nums);

        backtrackDuplicates(nums, 0, currentSubset, results);

        return results;
    }

    private static void backtrackDuplicates(int[] nums,
                                            int startIndex,
                                            List<Integer> currentSubset,
                                            List<List<Integer>> results) {

        results.add(new ArrayList<>(currentSubset));

        for (int i = startIndex; i < nums.length; i++) {

            // ── SKIP DUPLICATE at same level ──
            // If nums[i] == nums[i-1] AND i > startIndex
            // → we already explored this VALUE at this decision level
            // → skip to avoid duplicate subsets
            if (i > startIndex && nums[i] == nums[i - 1]) {
                continue;
            }

            currentSubset.add(nums[i]);
            backtrackDuplicates(nums, i + 1, currentSubset, results);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // TRACE VERSION: Watch backtracking happen
    // ─────────────────────────────────────────────
    public static List<List<Integer>> subsetsWithTrace(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        System.out.println("Input: " + Arrays.toString(nums));
        System.out.println("─────────────────────────────────────");

        backtrackTrace(nums, 0, currentSubset, results, 0);

        return results;
    }

    private static void backtrackTrace(int[] nums,
                                       int startIndex,
                                       List<Integer> currentSubset,
                                       List<List<Integer>> results,
                                       int depth) {

        String indent = "  ".repeat(depth);

        // Save current subset
        results.add(new ArrayList<>(currentSubset));
        System.out.println(indent + "📦 SAVE: " + currentSubset);

        for (int i = startIndex; i < nums.length; i++) {

            System.out.println(indent + "  ➕ ADD " + nums[i]
                    + " → " + currentSubset + " + " + nums[i]);

            currentSubset.add(nums[i]);
            backtrackTrace(nums, i + 1, currentSubset, results, depth + 1);

            currentSubset.remove(currentSubset.size() - 1);

            System.out.println(indent + "  ➖ REMOVE " + nums[i]
                    + " → back to " + currentSubset);
        }
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 28: Generate All Subsets of a Set       ║");
        System.out.println("║  Pattern: Backtracking → Inclusion/Exclusion     ║");
        System.out.println("║           Branching → Power Set Generation       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Core solution ──
        System.out.println("═══ TEST 1: All Subsets of [1,2,3] ═══");
        int[] nums1 = {1, 2, 3};
        List<List<Integer>> result1 = subsets(nums1);
        System.out.println("Subsets (" + result1.size() + " total):");
        for (List<Integer> s : result1) {
            System.out.println("  " + s);
        }
        System.out.println();

        // ── TEST 2: Traced execution ──
        System.out.println("═══ TEST 2: Traced Backtracking [1,2,3] ═══");
        List<List<Integer>> traced = subsetsWithTrace(nums1);
        System.out.println("\nAll subsets: " + traced);
        System.out.println();

        // ── TEST 3: Compare all four approaches ──
        System.out.println("═══ TEST 3: All Four Approaches Compared ═══");
        int[] nums3 = {5, 10, 15};

        List<List<Integer>> approachA = subsets(nums3);
        List<List<Integer>> approachB = subsetsIncludeExclude(nums3);
        List<List<Integer>> approachC = subsetsCascading(nums3);
        List<List<Integer>> approachD = subsetsBitwise(nums3);

        System.out.println("Approach A (Collect-at-every-node): " + approachA.size() + " subsets");
        System.out.println("  " + approachA);
        System.out.println("Approach B (Include/Exclude):       " + approachB.size() + " subsets");
        System.out.println("  " + approachB);
        System.out.println("Approach C (Cascading/Iterative):   " + approachC.size() + " subsets");
        System.out.println("  " + approachC);
        System.out.println("Approach D (Bit Manipulation):      " + approachD.size() + " subsets");
        System.out.println("  " + approachD);
        System.out.println("All produce " + (1 << nums3.length) + " subsets? "
                + (approachA.size() == (1 << nums3.length)
                && approachB.size() == (1 << nums3.length)
                && approachC.size() == (1 << nums3.length)
                && approachD.size() == (1 << nums3.length)));
        System.out.println();

        // ── TEST 4: Empty input ──
        System.out.println("═══ TEST 4: Empty Input ═══");
        int[] empty = {};
        List<List<Integer>> emptyResult = subsets(empty);
        System.out.println("Subsets of []: " + emptyResult);
        System.out.println("Count: " + emptyResult.size() + " (expected 1 — just the empty set)");
        System.out.println();

        // ── TEST 5: Single element ──
        System.out.println("═══ TEST 5: Single Element ═══");
        int[] single = {7};
        List<List<Integer>> singleResult = subsets(single);
        System.out.println("Subsets of [7]: " + singleResult);
        System.out.println();

        // ── TEST 6: Subsets of size K ──
        System.out.println("═══ TEST 6: Subsets of Size K ═══");
        int[] nums6 = {1, 2, 3, 4};
        System.out.println("Input: " + Arrays.toString(nums6));
        for (int k = 0; k <= nums6.length; k++) {
            List<List<Integer>> sizeK = subsetsOfSizeK(nums6, k);
            System.out.println("  Size " + k + ": " + sizeK + " (count=" + sizeK.size() + ")");
        }
        System.out.println();

        // ── TEST 7: Subsets with duplicates ──
        System.out.println("═══ TEST 7: Subsets With Duplicate Elements ═══");
        int[] withDups = {1, 2, 2};
        System.out.println("Input: " + Arrays.toString(withDups));

        List<List<Integer>> noDupHandling = subsets(withDups);
        System.out.println("Without duplicate handling: " + noDupHandling);
        System.out.println("  Count: " + noDupHandling.size()
                + " (has duplicates if elements repeat!)");

        List<List<Integer>> withDupHandling = subsetsWithDuplicates(withDups);
        System.out.println("With duplicate handling:    " + withDupHandling);
        System.out.println("  Count: " + withDupHandling.size() + " (unique subsets only)");
        System.out.println();

        // ── TEST 8: Verify 2^n count ──
        System.out.println("═══ TEST 8: Verify 2^n Subset Count ═══");
        for (int n = 0; n <= 6; n++) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = i + 1;
            int count = subsets(arr).size();
            int expected = 1 << n;
            System.out.println("  n=" + n + " → subsets=" + count
                    + " → 2^" + n + "=" + expected
                    + " → match=" + (count == expected));
        }
        System.out.println();

        // ── TEST 9: Performance comparison ──
        System.out.println("═══ TEST 9: Performance Comparison ═══");
        int[] perfArr = new int[20];
        for (int i = 0; i < 20; i++) perfArr[i] = i + 1;

        long start, end;

        start = System.currentTimeMillis();
        List<List<Integer>> perfA = subsets(perfArr);
        end = System.currentTimeMillis();
        System.out.println("Backtracking:  " + perfA.size()
                + " subsets in " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<List<Integer>> perfC = subsetsCascading(perfArr);
        end = System.currentTimeMillis();
        System.out.println("Cascading:     " + perfC.size()
                + " subsets in " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<List<Integer>> perfD = subsetsBitwise(perfArr);
        end = System.currentTimeMillis();
        System.out.println("Bitwise:       " + perfD.size()
                + " subsets in " + (end - start) + "ms");

        System.out.println("All counts match: "
                + (perfA.size() == perfC.size() && perfC.size() == perfD.size()));
        System.out.println();

        // ── TEST 10: Negative numbers ──
        System.out.println("═══ TEST 10: Negative Numbers ═══");
        int[] negArr = {-3, 0, 5};
        List<List<Integer>> negResult = subsets(negArr);
        System.out.println("Subsets of " + Arrays.toString(negArr) + ":");
        for (List<Integer> s : negResult) {
            int sum = s.stream().mapToInt(Integer::intValue).sum();
            System.out.println("  " + s + " → sum=" + sum);
        }
    }
}