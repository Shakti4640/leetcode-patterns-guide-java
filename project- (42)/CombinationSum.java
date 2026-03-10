import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum {

    // ─────────────────────────────────────────────
    // SOLUTION 1: Backtracking with Sorting + Pruning (OPTIMAL)
    // ─────────────────────────────────────────────
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentCombination = new ArrayList<>();

        // Sort for early termination pruning
        Arrays.sort(candidates);

        backtrack(candidates, target, 0, currentCombination, result);

        return result;
    }

    private static void backtrack(
            int[] candidates,
            int remaining,
            int startIndex,
            List<Integer> currentCombination,
            List<List<Integer>> result) {

        // BASE CASE: found a valid combination
        if (remaining == 0) {
            result.add(new ArrayList<>(currentCombination)); // COPY!
            return;
        }

        // EXPLORE: try each candidate from startIndex onward
        for (int i = startIndex; i < candidates.length; i++) {

            // PRUNE: if current candidate exceeds remaining → all subsequent do too
            if (candidates[i] > remaining) {
                break; // not continue — sorted array guarantees all rest are bigger
            }

            // CHOOSE: add candidate to current combination
            currentCombination.add(candidates[i]);

            // EXPLORE: recurse with reduced target
            // startIndex = i (NOT i+1) → allows reuse of same candidate
            backtrack(candidates, remaining - candidates[i], i, currentCombination, result);

            // UN-CHOOSE: remove last element (backtrack)
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Without Sorting (correct but slower)
    // ─────────────────────────────────────────────
    public static List<List<Integer>> combinationSumNoSort(int[] candidates, int target) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        backtrackNoSort(candidates, target, 0, current, result);

        return result;
    }

    private static void backtrackNoSort(
            int[] candidates,
            int remaining,
            int startIndex,
            List<Integer> current,
            List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (remaining < 0) {
            return; // overshot — can't use break since not sorted
        }

        for (int i = startIndex; i < candidates.length; i++) {
            current.add(candidates[i]);
            backtrackNoSort(candidates, remaining - candidates[i], i, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Combination Sum II (each candidate used at most ONCE)
    // candidates may contain duplicates
    // ─────────────────────────────────────────────
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        Arrays.sort(candidates); // MUST sort for duplicate skipping

        backtrackCombSum2(candidates, target, 0, current, result);

        return result;
    }

    private static void backtrackCombSum2(
            int[] candidates,
            int remaining,
            int startIndex,
            List<Integer> current,
            List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = startIndex; i < candidates.length; i++) {

            // PRUNE: too large
            if (candidates[i] > remaining) break;

            // SKIP DUPLICATES at same recursion level
            // (only skip if not the first candidate at this level)
            if (i > startIndex && candidates[i] == candidates[i - 1]) continue;

            current.add(candidates[i]);

            // startIndex = i + 1 → each candidate used AT MOST ONCE
            backtrackCombSum2(candidates, remaining - candidates[i], i + 1, current, result);

            current.remove(current.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT: Combination Sum III
    // Find all combinations of K numbers (1-9) that sum to target
    // Each number used at most once
    // ─────────────────────────────────────────────
    public static List<List<Integer>> combinationSum3(int k, int target) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        backtrackCombSum3(k, target, 1, current, result);

        return result;
    }

    private static void backtrackCombSum3(
            int k,
            int remaining,
            int start,
            List<Integer> current,
            List<List<Integer>> result) {

        if (current.size() == k) {
            if (remaining == 0) {
                result.add(new ArrayList<>(current));
            }
            return; // exactly K numbers required — stop here
        }

        for (int i = start; i <= 9; i++) {

            if (i > remaining) break;

            current.add(i);
            backtrackCombSum3(k, remaining - i, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the backtracking tree
    // ─────────────────────────────────────────────
    public static List<List<Integer>> combinationSumWithTrace(int[] candidates, int target) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        Arrays.sort(candidates);

        System.out.println("Candidates: " + Arrays.toString(candidates));
        System.out.println("Target: " + target);
        System.out.println("═══════════════════════════════════════════");

        backtrackTrace(candidates, target, 0, current, result, 0);

        System.out.println("═══════════════════════════════════════════");
        System.out.println("TOTAL COMBINATIONS FOUND: " + result.size());
        return result;
    }

    private static int nodeCount = 0;

    private static void backtrackTrace(
            int[] candidates,
            int remaining,
            int startIndex,
            List<Integer> current,
            List<List<Integer>> result,
            int depth) {

        String indent = "  ".repeat(depth);
        nodeCount++;

        if (remaining == 0) {
            System.out.println(indent + "✅ FOUND: " + current 
                + " (sum=" + sumList(current) + ")");
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = startIndex; i < candidates.length; i++) {

            if (candidates[i] > remaining) {
                System.out.println(indent + "✂️ PRUNE: " + candidates[i] 
                    + " > remaining(" + remaining + ") → break (skip " 
                    + (candidates.length - i) + " candidates)");
                break;
            }

            System.out.println(indent + "→ Choose " + candidates[i] 
                + " | path=" + current + "+" + candidates[i] 
                + " | remaining=" + (remaining - candidates[i]));

            current.add(candidates[i]);
            backtrackTrace(candidates, remaining - candidates[i], i, current, result, depth + 1);
            current.remove(current.size() - 1);

            System.out.println(indent + "← Backtrack (removed " + candidates[i] + ")");
        }
    }

    private static int sumList(List<Integer> list) {
        int sum = 0;
        for (int x : list) sum += x;
        return sum;
    }

    // ─────────────────────────────────────────────
    // COUNTING VARIANT: How many combinations? (DP approach)
    // For comparison with backtracking
    // ─────────────────────────────────────────────
    public static int combinationSumCount(int[] candidates, int target) {

        // dp[i] = number of combinations that sum to i
        int[] dp = new int[target + 1];
        dp[0] = 1; // empty combination sums to 0

        // For each candidate, update all reachable sums
        for (int candidate : candidates) {
            for (int sum = candidate; sum <= target; sum++) {
                dp[sum] += dp[sum - candidate];
            }
        }

        return dp[target];
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 42: Combination Sum                          ║");
        System.out.println("║  Pattern: Backtracking → Pruning → Target Reduction   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example with Trace ──
        System.out.println("═══ TEST 1: Classic Example with Full Trace ═══");
        nodeCount = 0;
        int[] cand1 = {2, 3, 6, 7};
        List<List<Integer>> res1 = combinationSumWithTrace(cand1, 7);
        for (List<Integer> combo : res1) {
            System.out.println("  " + combo);
        }
        System.out.println("Nodes explored: " + nodeCount);
        System.out.println();

        // ── TEST 2: Multiple Combinations with Trace ──
        System.out.println("═══ TEST 2: Multiple Combinations with Trace ═══");
        nodeCount = 0;
        int[] cand2 = {2, 3, 5};
        List<List<Integer>> res2 = combinationSumWithTrace(cand2, 8);
        for (List<Integer> combo : res2) {
            System.out.println("  " + combo);
        }
        System.out.println("Nodes explored: " + nodeCount);
        System.out.println();

        // ── TEST 3: No Solution ──
        System.out.println("═══ TEST 3: No Solution ═══");
        int[] cand3 = {2};
        List<List<Integer>> res3 = combinationSum(cand3, 1);
        System.out.println("candidates=[2], target=1 → " + res3);
        System.out.println();

        // ── TEST 4: Single Candidate Reused ──
        System.out.println("═══ TEST 4: Single Candidate Reused ═══");
        int[] cand4 = {3};
        List<List<Integer>> res4 = combinationSum(cand4, 9);
        System.out.println("candidates=[3], target=9 → " + res4);
        System.out.println();

        // ── TEST 5: Sorted vs Unsorted Performance ──
        System.out.println("═══ TEST 5: Sorted vs Unsorted Performance ═══");
        int[] cand5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int target5 = 20;

        long start = System.nanoTime();
        List<List<Integer>> resSorted = combinationSum(cand5, target5);
        long sortedTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<List<Integer>> resUnsorted = combinationSumNoSort(cand5, target5);
        long unsortedTime = System.nanoTime() - start;

        System.out.println("candidates=[1..10], target=20");
        System.out.println("  With sorting+pruning: " + resSorted.size() 
            + " combos, " + (sortedTime / 1_000_000) + " ms");
        System.out.println("  Without sorting:      " + resUnsorted.size() 
            + " combos, " + (unsortedTime / 1_000_000) + " ms");
        System.out.println("  Speedup: " 
            + String.format("%.1f", (double) unsortedTime / Math.max(sortedTime, 1)) + "x");
        System.out.println();

        // ── TEST 6: Combination Sum II (each used once, with duplicates) ──
        System.out.println("═══ TEST 6: Combination Sum II (Use Once, Duplicates) ═══");
        int[] cand6 = {10, 1, 2, 7, 6, 1, 5};
        List<List<Integer>> res6 = combinationSum2(cand6, 8);
        System.out.println("candidates=" + Arrays.toString(cand6) + ", target=8");
        System.out.println("Combinations (each used at most once):");
        for (List<Integer> combo : res6) {
            System.out.println("  " + combo);
        }
        System.out.println();

        // ── TEST 7: Combination Sum III (K numbers from 1-9) ──
        System.out.println("═══ TEST 7: Combination Sum III (K Numbers, 1-9) ═══");
        List<List<Integer>> res7a = combinationSum3(3, 7);
        System.out.println("k=3, target=7 → " + res7a);

        List<List<Integer>> res7b = combinationSum3(3, 9);
        System.out.println("k=3, target=9 → " + res7b);

        List<List<Integer>> res7c = combinationSum3(4, 15);
        System.out.println("k=4, target=15 → " + res7c);
        System.out.println();

        // ── TEST 8: Backtracking vs DP for Count ──
        System.out.println("═══ TEST 8: Backtracking vs DP for Count Only ═══");
        int[] cand8 = {1, 2, 3, 4, 5};
        int[] targets = {10, 15, 20, 25, 30};

        System.out.println("candidates=[1,2,3,4,5]");
        System.out.println("┌─────────┬────────────────┬───────────┬──────────────┬───────────┐");
        System.out.println("│ Target  │ Backtrack Count │ BT Time   │ DP Count     │ DP Time   │");
        System.out.println("├─────────┼────────────────┼───────────┼──────────────┼───────────┤");

        for (int t : targets) {
            start = System.nanoTime();
            List<List<Integer>> btResult = combinationSum(cand8.clone(), t);
            long btTime = System.nanoTime() - start;

            start = System.nanoTime();
            int dpCount = combinationSumCount(cand8.clone(), t);
            long dpTime = System.nanoTime() - start;

            System.out.printf("│ %7d │ %14d │ %7d μs │ %12d │ %7d μs │%n",
                t, btResult.size(), btTime / 1000, dpCount, dpTime / 1000);
        }
        System.out.println("└─────────┴────────────────┴───────────┴──────────────┴───────────┘");
        System.out.println("Note: DP is MUCH faster for counting — doesn't enumerate combinations");
        System.out.println();

        // ── TEST 9: Edge Cases ──
        System.out.println("═══ TEST 9: Edge Cases ═══");

        // Target = 0
        System.out.println("target=0: " + combinationSum(new int[]{1, 2}, 0));

        // Single element = target
        System.out.println("candidates=[7], target=7: " 
            + combinationSum(new int[]{7}, 7));

        // All candidates too large
        System.out.println("candidates=[5,10], target=3: " 
            + combinationSum(new int[]{5, 10}, 3));

        // Large candidate set
        int[] largeCand = new int[20];
        for (int i = 0; i < 20; i++) largeCand[i] = i + 1;
        start = System.nanoTime();
        List<List<Integer>> resLarge = combinationSum(largeCand, 15);
        long largeTime = System.nanoTime() - start;
        System.out.println("candidates=[1..20], target=15: " 
            + resLarge.size() + " combinations in " + (largeTime / 1000) + " μs");
    }
}