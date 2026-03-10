import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateAllPermutations {

    // ═══════════════════════════════════════════════
    // APPROACH A: Visited Array Method (Interview Standard)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPerm = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];

        backtrackVisited(nums, visited, currentPerm, results);

        return results;
    }

    private static void backtrackVisited(int[] nums,
                                         boolean[] visited,
                                         List<Integer> currentPerm,
                                         List<List<Integer>> results) {

        // ── Base case: all elements placed ──
        if (currentPerm.size() == nums.length) {
            results.add(new ArrayList<>(currentPerm));   // deep copy
            return;
        }

        // ── Try every element at the current position ──
        for (int i = 0; i < nums.length; i++) {

            // Skip if already used in this permutation
            if (visited[i]) continue;

            // ── CHOOSE ──
            visited[i] = true;
            currentPerm.add(nums[i]);

            // ── EXPLORE ──
            backtrackVisited(nums, visited, currentPerm, results);

            // ── UN-CHOOSE (backtrack) ──
            currentPerm.remove(currentPerm.size() - 1);
            visited[i] = false;
        }
    }

    // ═══════════════════════════════════════════════
    // APPROACH B: Swap Method (Space Efficient)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteSwap(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();

        backtrackSwap(nums, 0, results);

        return results;
    }

    private static void backtrackSwap(int[] nums,
                                      int fixedIndex,
                                      List<List<Integer>> results) {

        // ── Base case: all positions filled ──
        if (fixedIndex == nums.length) {
            // Convert current array state to a list (deep copy)
            List<Integer> perm = new ArrayList<>();
            for (int num : nums) {
                perm.add(num);
            }
            results.add(perm);
            return;
        }

        // ── Try each available element at position fixedIndex ──
        for (int i = fixedIndex; i < nums.length; i++) {

            // ── CHOOSE: place nums[i] at position fixedIndex ──
            swap(nums, fixedIndex, i);

            // ── EXPLORE: fix next position ──
            backtrackSwap(nums, fixedIndex + 1, results);

            // ── UN-CHOOSE: restore array ──
            swap(nums, fixedIndex, i);
        }
    }

    private static void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION: Visited method with step-by-step output
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteWithTrace(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPerm = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];

        System.out.println("Input: " + Arrays.toString(nums));
        System.out.println("─────────────────────────────────────");

        backtrackVisitedTrace(nums, visited, currentPerm, results, 0);

        return results;
    }

    private static void backtrackVisitedTrace(int[] nums,
                                              boolean[] visited,
                                              List<Integer> currentPerm,
                                              List<List<Integer>> results,
                                              int depth) {

        String indent = "  ".repeat(depth);

        if (currentPerm.size() == nums.length) {
            results.add(new ArrayList<>(currentPerm));
            System.out.println(indent + "✅ COMPLETE: " + currentPerm);
            return;
        }

        System.out.println(indent + "Position " + depth
                + " | current=" + currentPerm
                + " | visited=" + Arrays.toString(visited));

        for (int i = 0; i < nums.length; i++) {

            if (visited[i]) {
                System.out.println(indent + "  [" + nums[i] + " at index "
                        + i + "] → SKIP (already used)");
                continue;
            }

            System.out.println(indent + "  ➕ CHOOSE " + nums[i]
                    + " at index " + i);

            visited[i] = true;
            currentPerm.add(nums[i]);
            backtrackVisitedTrace(nums, visited, currentPerm, results, depth + 1);
            currentPerm.remove(currentPerm.size() - 1);
            visited[i] = false;

            System.out.println(indent + "  ➖ UNCHOOSE " + nums[i]
                    + " → back to " + currentPerm);
        }
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION: Swap method with step-by-step output
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteSwapWithTrace(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();

        System.out.println("Input: " + Arrays.toString(nums));
        System.out.println("─────────────────────────────────────");

        backtrackSwapTrace(nums, 0, results, 0);

        return results;
    }

    private static void backtrackSwapTrace(int[] nums,
                                           int fixedIndex,
                                           List<List<Integer>> results,
                                           int depth) {

        String indent = "  ".repeat(depth);

        if (fixedIndex == nums.length) {
            List<Integer> perm = new ArrayList<>();
            for (int num : nums) perm.add(num);
            results.add(perm);
            System.out.println(indent + "✅ COMPLETE: " + Arrays.toString(nums));
            return;
        }

        System.out.println(indent + "Fix position " + fixedIndex
                + " | array=" + Arrays.toString(nums)
                + " | fixed=[0.." + (fixedIndex - 1)
                + "] available=[" + fixedIndex + ".." + (nums.length - 1) + "]");

        for (int i = fixedIndex; i < nums.length; i++) {

            System.out.println(indent + "  🔄 SWAP pos " + fixedIndex
                    + " ↔ pos " + i
                    + " (place " + nums[i] + " at position " + fixedIndex + ")");

            swap(nums, fixedIndex, i);
            backtrackSwapTrace(nums, fixedIndex + 1, results, depth + 1);
            swap(nums, fixedIndex, i);

            System.out.println(indent + "  🔄 UNSWAP → restored "
                    + Arrays.toString(nums));
        }
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Permutations with duplicate elements
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteWithDuplicates(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPerm = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];

        // CRITICAL: Sort to bring duplicates together
        Arrays.sort(nums);

        backtrackDuplicates(nums, visited, currentPerm, results);

        return results;
    }

    private static void backtrackDuplicates(int[] nums,
                                            boolean[] visited,
                                            List<Integer> currentPerm,
                                            List<List<Integer>> results) {

        if (currentPerm.size() == nums.length) {
            results.add(new ArrayList<>(currentPerm));
            return;
        }

        for (int i = 0; i < nums.length; i++) {

            if (visited[i]) continue;

            // ── SKIP DUPLICATE at same decision level ──
            // If nums[i] == nums[i-1] AND nums[i-1] is NOT visited
            // → nums[i-1] was already tried AND backtracked at this level
            // → Trying nums[i] (same value) would produce duplicate permutations
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) {
                continue;
            }

            visited[i] = true;
            currentPerm.add(nums[i]);
            backtrackDuplicates(nums, visited, currentPerm, results);
            currentPerm.remove(currentPerm.size() - 1);
            visited[i] = false;
        }
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Permutations of size K (partial permutations)
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteOfSizeK(int[] nums, int k) {

        List<List<Integer>> results = new ArrayList<>();
        List<Integer> currentPerm = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];

        backtrackSizeK(nums, visited, currentPerm, results, k);

        return results;
    }

    private static void backtrackSizeK(int[] nums,
                                       boolean[] visited,
                                       List<Integer> currentPerm,
                                       List<List<Integer>> results,
                                       int k) {

        // ── Collect when size reaches k (not n) ──
        if (currentPerm.size() == k) {
            results.add(new ArrayList<>(currentPerm));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            currentPerm.add(nums[i]);
            backtrackSizeK(nums, visited, currentPerm, results, k);
            currentPerm.remove(currentPerm.size() - 1);
            visited[i] = false;
        }
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Swap method with duplicate handling
    // ═══════════════════════════════════════════════
    public static List<List<Integer>> permuteSwapWithDuplicates(int[] nums) {

        List<List<Integer>> results = new ArrayList<>();
        Arrays.sort(nums);

        backtrackSwapDup(nums, 0, results);

        return results;
    }

    private static void backtrackSwapDup(int[] nums,
                                         int fixedIndex,
                                         List<List<Integer>> results) {

        if (fixedIndex == nums.length) {
            List<Integer> perm = new ArrayList<>();
            for (int num : nums) perm.add(num);
            results.add(perm);
            return;
        }

        // Track which VALUES have been tried at this position
        Set<Integer> usedValues = new HashSet<>();

        for (int i = fixedIndex; i < nums.length; i++) {

            // Skip if this VALUE was already tried at position fixedIndex
            if (usedValues.contains(nums[i])) continue;
            usedValues.add(nums[i]);

            swap(nums, fixedIndex, i);
            backtrackSwapDup(nums, fixedIndex + 1, results);
            swap(nums, fixedIndex, i);
        }
    }

    // ─────────────────────────────────────────────
    // SIDE-BY-SIDE: Subsets vs Permutations
    // Shows the structural difference clearly
    // ─────────────────────────────────────────────
    public static void compareSubsetsVsPermutations(int[] nums) {

        System.out.println("Input: " + Arrays.toString(nums));
        System.out.println();

        // Subsets (from Project 28 approach)
        List<List<Integer>> subsets = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        subsetsHelper(nums, 0, current, subsets);

        System.out.println("SUBSETS (order irrelevant, variable length):");
        System.out.println("  Count: " + subsets.size() + " (2^" + nums.length + ")");
        for (List<Integer> s : subsets) {
            System.out.println("    " + s);
        }
        System.out.println();

        // Permutations
        List<List<Integer>> perms = permute(nums);

        System.out.println("PERMUTATIONS (order matters, fixed length = " + nums.length + "):");
        System.out.println("  Count: " + perms.size() + " (" + nums.length + "!)");
        for (List<Integer> p : perms) {
            System.out.println("    " + p);
        }
    }

    private static void subsetsHelper(int[] nums, int start,
                                      List<Integer> current,
                                      List<List<Integer>> results) {
        results.add(new ArrayList<>(current));
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            subsetsHelper(nums, i + 1, current, results);
            current.remove(current.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 29: Generate All Permutations of an Array   ║");
        System.out.println("║  Pattern: Backtracking → Visited / Swap Methods      ║");
        System.out.println("║           → Positional Placement                     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic visited method ──
        System.out.println("═══ TEST 1: Permutations of [1,2,3] — Visited Method ═══");
        int[] nums1 = {1, 2, 3};
        List<List<Integer>> result1 = permute(nums1);
        System.out.println("Count: " + result1.size() + " (expected 6)");
        for (List<Integer> p : result1) {
            System.out.println("  " + p);
        }
        System.out.println();

        // ── TEST 2: Swap method ──
        System.out.println("═══ TEST 2: Permutations of [1,2,3] — Swap Method ═══");
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = permuteSwap(nums2);
        System.out.println("Count: " + result2.size() + " (expected 6)");
        for (List<Integer> p : result2) {
            System.out.println("  " + p);
        }
        System.out.println();

        // ── TEST 3: Traced visited method ──
        System.out.println("═══ TEST 3: Traced Visited Method [1,2] ═══");
        int[] nums3 = {1, 2};
        List<List<Integer>> traced1 = permuteWithTrace(nums3);
        System.out.println("\nAll permutations: " + traced1);
        System.out.println();

        // ── TEST 4: Traced swap method ──
        System.out.println("═══ TEST 4: Traced Swap Method [1,2] ═══");
        int[] nums4 = {1, 2};
        List<List<Integer>> traced2 = permuteSwapWithTrace(nums4);
        System.out.println("\nAll permutations: " + traced2);
        System.out.println();

        // ── TEST 5: Side-by-side subsets vs permutations ──
        System.out.println("═══ TEST 5: Subsets vs Permutations [1,2,3] ═══");
        compareSubsetsVsPermutations(new int[]{1, 2, 3});
        System.out.println();

        // ── TEST 6: Single element ──
        System.out.println("═══ TEST 6: Single Element ═══");
        int[] single = {42};
        System.out.println("Visited: " + permute(single));
        System.out.println("Swap:    " + permuteSwap(single));
        System.out.println();

        // ── TEST 7: Two elements ──
        System.out.println("═══ TEST 7: Two Elements ═══");
        int[] two = {5, 10};
        System.out.println("Permutations: " + permute(two));
        System.out.println("Count: " + permute(two).size() + " (expected 2)");
        System.out.println();

        // ── TEST 8: Duplicate elements ──
        System.out.println("═══ TEST 8: Permutations with Duplicates ═══");
        int[] withDups = {1, 1, 2};
        System.out.println("Input: " + Arrays.toString(withDups));

        List<List<Integer>> naiveResult = permute(withDups);
        System.out.println("Without duplicate handling: " + naiveResult.size()
                + " permutations");
        System.out.println("  " + naiveResult);

        List<List<Integer>> dedupResult = permuteWithDuplicates(withDups);
        System.out.println("With duplicate handling:    " + dedupResult.size()
                + " permutations");
        System.out.println("  " + dedupResult);

        System.out.println();
        System.out.println("Swap method with duplicates:");
        int[] withDups2 = {1, 1, 2};
        List<List<Integer>> swapDedupResult = permuteSwapWithDuplicates(withDups2);
        System.out.println("  Count: " + swapDedupResult.size());
        System.out.println("  " + swapDedupResult);
        System.out.println();

        // ── TEST 9: Partial permutations (size K) ──
        System.out.println("═══ TEST 9: Permutations of Size K ═══");
        int[] nums9 = {1, 2, 3, 4};
        System.out.println("Input: " + Arrays.toString(nums9));
        for (int k = 1; k <= nums9.length; k++) {
            List<List<Integer>> sizeK = permuteOfSizeK(nums9, k);
            System.out.println("  Size " + k + ": " + sizeK.size()
                    + " permutations (P(" + nums9.length + "," + k + "))");
            if (k <= 2) {
                for (List<Integer> p : sizeK) {
                    System.out.println("    " + p);
                }
            }
        }
        System.out.println();

        // ── TEST 10: Verify n! count ──
        System.out.println("═══ TEST 10: Verify n! Count ═══");
        for (int n = 1; n <= 7; n++) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = i + 1;

            int visitedCount = permute(arr).size();
            int swapCount = permuteSwap(arr).size();
            int factorial = factorial(n);

            System.out.println("  n=" + n
                    + " | visited=" + visitedCount
                    + " | swap=" + swapCount
                    + " | n!=" + factorial
                    + " | match=" + (visitedCount == factorial && swapCount == factorial));
        }
        System.out.println();

        // ── TEST 11: Negative numbers ──
        System.out.println("═══ TEST 11: Negative Numbers ═══");
        int[] negArr = {-1, 0, 1};
        List<List<Integer>> negResult = permute(negArr);
        System.out.println("Permutations of " + Arrays.toString(negArr) + ":");
        for (List<Integer> p : negResult) {
            System.out.println("  " + p);
        }
        System.out.println();

        // ── TEST 12: Performance comparison ──
        System.out.println("═══ TEST 12: Performance Comparison ═══");
        int[] perfArr = new int[9];
        for (int i = 0; i < 9; i++) perfArr[i] = i + 1;
        int expectedCount = factorial(9);

        long start, end;

        start = System.currentTimeMillis();
        List<List<Integer>> visitedPerms = permute(perfArr);
        end = System.currentTimeMillis();
        System.out.println("Visited method: " + visitedPerms.size()
                + " permutations in " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<List<Integer>> swapPerms = permuteSwap(perfArr);
        end = System.currentTimeMillis();
        System.out.println("Swap method:    " + swapPerms.size()
                + " permutations in " + (end - start) + "ms");

        System.out.println("Expected: " + expectedCount + " (9!)");
        System.out.println("Both correct: "
                + (visitedPerms.size() == expectedCount
                && swapPerms.size() == expectedCount));
    }

    private static int factorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }
}