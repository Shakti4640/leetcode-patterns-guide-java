import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knapsack01 {

    // ─────────────────────────────────────────────
    // SOLUTION 1: 2D Tabulation (Full Table)
    // ─────────────────────────────────────────────
    public static int knapsack2D(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // Base cases: dp[0][w] = 0, dp[i][0] = 0 (Java arrays default to 0)

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {

                // Option 1: Exclude item i
                dp[i][w] = dp[i - 1][w];

                // Option 2: Include item i (if it fits)
                if (weights[i - 1] <= w) {
                    int includeValue = values[i - 1] + dp[i - 1][w - weights[i - 1]];
                    dp[i][w] = Math.max(dp[i][w], includeValue);
                }
            }
        }

        return dp[n][capacity];
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: 1D Space-Optimized (RIGHT TO LEFT)
    // ─────────────────────────────────────────────
    public static int knapsack1D(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[] dp = new int[capacity + 1];

        for (int i = 0; i < n; i++) {
            // RIGHT TO LEFT — ensures each item used at most once
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
            }
        }

        return dp[capacity];
    }

    // ─────────────────────────────────────────────
    // SOLUTION 3: Memoization (Top-Down)
    // ─────────────────────────────────────────────
    public static int knapsackMemo(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[][] memo = new int[n + 1][capacity + 1];
        for (int[] row : memo) Arrays.fill(row, -1);

        return memoHelper(weights, values, n, capacity, memo);
    }

    private static int memoHelper(int[] weights, int[] values,
                                   int i, int w, int[][] memo) {
        // Base case
        if (i == 0 || w == 0) return 0;

        // Check cache
        if (memo[i][w] != -1) return memo[i][w];

        // Exclude
        int exclude = memoHelper(weights, values, i - 1, w, memo);

        // Include (if fits)
        int include = 0;
        if (weights[i - 1] <= w) {
            include = values[i - 1]
                + memoHelper(weights, values, i - 1, w - weights[i - 1], memo);
        }

        memo[i][w] = Math.max(exclude, include);
        return memo[i][w];
    }

    // ─────────────────────────────────────────────
    // SOLUTION 4: Brute Force (enumerate all subsets)
    // ─────────────────────────────────────────────
    public static int knapsackBruteForce(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int maxValue = 0;

        // Enumerate all 2^n subsets using bitmask
        for (int mask = 0; mask < (1 << n); mask++) {
            int totalWeight = 0;
            int totalValue = 0;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalWeight += weights[i];
                    totalValue += values[i];
                }
            }

            if (totalWeight <= capacity) {
                maxValue = Math.max(maxValue, totalValue);
            }
        }

        return maxValue;
    }

    // ─────────────────────────────────────────────
    // ITEM RECOVERY — Which items were selected?
    // ─────────────────────────────────────────────
    public static List<Integer> recoverItems(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // Fill table (same as knapsack2D)
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w];
                if (weights[i - 1] <= w) {
                    int includeValue = values[i - 1] + dp[i - 1][w - weights[i - 1]];
                    dp[i][w] = Math.max(dp[i][w], includeValue);
                }
            }
        }

        // Backtrack to find which items were included
        List<Integer> items = new ArrayList<>();
        int w = capacity;

        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                // Item i was included (value changed)
                items.add(i - 1); // 0-based index
                w -= weights[i - 1];
            }
        }

        return items;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Unbounded Knapsack (each item unlimited)
    // ─────────────────────────────────────────────
    public static int knapsackUnbounded(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[] dp = new int[capacity + 1];

        for (int i = 0; i < n; i++) {
            // LEFT TO RIGHT — allows reuse of same item
            for (int w = weights[i]; w <= capacity; w++) {
                dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
            }
        }

        return dp[capacity];
    }

    // ─────────────────────────────────────────────
    // VARIANT: Subset Sum — Can we make exact weight W?
    // ─────────────────────────────────────────────
    public static boolean subsetSum(int[] nums, int target) {

        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // empty subset sums to 0

        for (int num : nums) {
            // RIGHT TO LEFT (each element used once)
            for (int w = target; w >= num; w--) {
                dp[w] = dp[w] || dp[w - num];
            }
        }

        return dp[target];
    }

    // ─────────────────────────────────────────────
    // VARIANT: Equal Partition — Split array into two equal-sum halves?
    // ─────────────────────────────────────────────
    public static boolean canPartition(int[] nums) {

        int totalSum = 0;
        for (int num : nums) totalSum += num;

        // If total is odd → can't split equally
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;

        // Subset sum: can we pick elements summing to target?
        return subsetSum(nums, target);
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch the 2D table being filled
    // ─────────────────────────────────────────────
    public static void knapsackWithTrace(int[] weights, int[] values, int capacity) {

        int n = weights.length;

        System.out.println("Items:");
        for (int i = 0; i < n; i++) {
            System.out.println("  Item " + i + ": weight=" + weights[i]
                + ", value=" + values[i]);
        }
        System.out.println("Capacity: " + capacity);
        System.out.println("═══════════════════════════════════════════");

        int[][] dp = new int[n + 1][capacity + 1];

        // Print header
        System.out.print("      ");
        for (int w = 0; w <= capacity; w++) {
            System.out.printf("w=%-3d ", w);
        }
        System.out.println();

        // Print row 0
        System.out.print("i=0   ");
        for (int w = 0; w <= capacity; w++) {
            System.out.printf("%-5d ", 0);
        }
        System.out.println(" (no items)");

        // Fill and print each row
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w]; // exclude

                if (weights[i - 1] <= w) {
                    int includeVal = values[i - 1] + dp[i - 1][w - weights[i - 1]];
                    dp[i][w] = Math.max(dp[i][w], includeVal);
                }
            }

            System.out.print("i=" + i + "   ");
            for (int w = 0; w <= capacity; w++) {
                System.out.printf("%-5d ", dp[i][w]);
            }
            System.out.println(" (+ item " + (i - 1) + ": w="
                + weights[i - 1] + ",v=" + values[i - 1] + ")");
        }

        System.out.println();
        System.out.println("OPTIMAL VALUE: " + dp[n][capacity]);

        // Recover items
        List<Integer> items = new ArrayList<>();
        int w = capacity;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                items.add(i - 1);
                w -= weights[i - 1];
            }
        }

        int totalWeight = 0;
        int totalValue = 0;
        System.out.print("ITEMS TAKEN: ");
        for (int idx : items) {
            System.out.print("item" + idx + "(w=" + weights[idx]
                + ",v=" + values[idx] + ") ");
            totalWeight += weights[idx];
            totalValue += values[idx];
        }
        System.out.println();
        System.out.println("TOTAL: weight=" + totalWeight + ", value=" + totalValue);
    }

    // ─────────────────────────────────────────────
    // 1D TRACE — Show the right-to-left processing
    // ─────────────────────────────────────────────
    public static void knapsack1DWithTrace(int[] weights, int[] values, int capacity) {

        int n = weights.length;
        int[] dp = new int[capacity + 1];

        System.out.println("1D SPACE-OPTIMIZED TRACE:");
        System.out.println("Initial: " + Arrays.toString(dp));

        for (int i = 0; i < n; i++) {
            System.out.println("───────────────────────────────────");
            System.out.println("Processing item " + i + " (w=" + weights[i]
                + ", v=" + values[i] + ") → RIGHT to LEFT:");

            for (int w = capacity; w >= weights[i]; w--) {
                int oldVal = dp[w];
                int includeVal = values[i] + dp[w - weights[i]];

                if (includeVal > oldVal) {
                    dp[w] = includeVal;
                    System.out.println("  dp[" + w + "]: max(" + oldVal + ", "
                        + values[i] + "+" + dp[w - weights[i]]
                        + "=" + includeVal + ") = " + dp[w] + " ← UPDATED");
                }
            }

            System.out.println("  dp = " + Arrays.toString(dp));
        }

        System.out.println("═══════════════════════════════════════════");
        System.out.println("ANSWER: " + dp[capacity]);
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 44: 0/1 Knapsack Problem                     ║");
        System.out.println("║  Pattern: DP → 2D State Table → Include/Exclude       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Classic Example with 2D Trace ──
        System.out.println("═══ TEST 1: Classic Example with 2D Table Trace ═══");
        int[] w1 = {1, 3, 4, 5};
        int[] v1 = {1, 4, 5, 7};
        knapsackWithTrace(w1, v1, 7);
        System.out.println();

        // ── TEST 2: 1D Space-Optimized Trace ──
        System.out.println("═══ TEST 2: 1D Space-Optimized Trace ═══");
        knapsack1DWithTrace(w1, v1, 7);
        System.out.println();

        // ── TEST 3: All Approaches Agree ──
        System.out.println("═══ TEST 3: Verify All Approaches Agree ═══");
        int[][] testWeights = {
            {1, 3, 4, 5},
            {2, 2, 3},
            {5, 10, 15},
            {2, 3, 4, 5},
            {1, 2, 3, 4, 5}
        };
        int[][] testValues = {
            {1, 4, 5, 7},
            {3, 3, 4},
            {10, 20, 30},
            {3, 4, 5, 6},
            {1, 6, 10, 16, 20}
        };
        int[] testCaps = {7, 5, 4, 8, 8};

        System.out.println("┌────────────────────────┬──────┬──────┬──────┬──────┐");
        System.out.println("│ Test                   │  2D  │  1D  │ Memo │Brute │");
        System.out.println("├────────────────────────┼──────┼──────┼──────┼──────┤");

        for (int t = 0; t < testWeights.length; t++) {
            int r2d = knapsack2D(testWeights[t], testValues[t], testCaps[t]);
            int r1d = knapsack1D(testWeights[t], testValues[t], testCaps[t]);
            int rMemo = knapsackMemo(testWeights[t], testValues[t], testCaps[t]);
            int rBrute = knapsackBruteForce(testWeights[t], testValues[t], testCaps[t]);

            System.out.printf("│ W=%-10s Cap=%-4d │ %4d │ %4d │ %4d │ %4d │%n",
                Arrays.toString(testWeights[t]), testCaps[t],
                r2d, r1d, rMemo, rBrute);
        }
        System.out.println("└────────────────────────┴──────┴──────┴──────┴──────┘");
        System.out.println();

        // ── TEST 4: Item Recovery ──
        System.out.println("═══ TEST 4: Item Recovery ═══");
        int[] w4 = {2, 3, 4, 5};
        int[] v4 = {3, 4, 5, 6};
        int cap4 = 8;
        int optValue = knapsack2D(w4, v4, cap4);
        List<Integer> items = recoverItems(w4, v4, cap4);

        System.out.println("Items: weights=" + Arrays.toString(w4)
            + " values=" + Arrays.toString(v4) + " cap=" + cap4);
        System.out.println("Optimal value: " + optValue);
        System.out.print("Items selected: ");
        int tw = 0, tv = 0;
        for (int idx : items) {
            System.out.print(idx + "(w=" + w4[idx] + ",v=" + v4[idx] + ") ");
            tw += w4[idx];
            tv += v4[idx];
        }
        System.out.println("\nTotal weight=" + tw + ", total value=" + tv);
        System.out.println();

        // ── TEST 5: 0/1 vs Unbounded Comparison ──
        System.out.println("═══ TEST 5: 0/1 vs Unbounded Knapsack ═══");
        int[] w5 = {3, 4, 5};
        int[] v5 = {5, 6, 8};
        int cap5 = 12;
        int res01 = knapsack1D(w5, v5, cap5);
        int resUB = knapsackUnbounded(w5, v5, cap5);
        System.out.println("Weights: " + Arrays.toString(w5));
        System.out.println("Values:  " + Arrays.toString(v5));
        System.out.println("Capacity: " + cap5);
        System.out.println("0/1 Knapsack (each once):      " + res01);
        System.out.println("Unbounded (unlimited reuse):    " + resUB);
        System.out.println("Difference: " + (resUB - res01)
            + " (unbounded can reuse best items)");
        System.out.println();

        // ── TEST 6: Subset Sum ──
        System.out.println("═══ TEST 6: Subset Sum Variant ═══");
        int[] nums6a = {3, 34, 4, 12, 5, 2};
        System.out.println("Array: " + Arrays.toString(nums6a));
        System.out.println("Can make sum 9?  " + subsetSum(nums6a, 9));
        System.out.println("Can make sum 30? " + subsetSum(nums6a, 30));
        System.out.println("Can make sum 11? " + subsetSum(nums6a, 11));
        System.out.println();

        // ── TEST 7: Equal Partition ──
        System.out.println("═══ TEST 7: Equal Partition Variant ═══");
        int[] nums7a = {1, 5, 11, 5};
        int[] nums7b = {1, 2, 3, 5};
        int[] nums7c = {1, 2, 3, 4, 5, 6, 7};
        System.out.println(Arrays.toString(nums7a) + " → can partition? "
            + canPartition(nums7a));
        System.out.println(Arrays.toString(nums7b) + " → can partition? "
            + canPartition(nums7b));
        System.out.println(Arrays.toString(nums7c) + " → can partition? "
            + canPartition(nums7c));
        System.out.println();

        // ── TEST 8: Performance Comparison ──
        System.out.println("═══ TEST 8: Performance Comparison ═══");
        int perfN = 500;
        int perfW = 5000;
        int[] perfWeights = new int[perfN];
        int[] perfValues = new int[perfN];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < perfN; i++) {
            perfWeights[i] = rand.nextInt(50) + 1;
            perfValues[i] = rand.nextInt(100) + 1;
        }
        System.out.println("N=" + perfN + " items, W=" + perfW);

        // 2D
        long start = System.nanoTime();
        int res2D = knapsack2D(perfWeights, perfValues, perfW);
        long time2D = (System.nanoTime() - start) / 1_000_000;
        System.out.println("  2D Tabulation: " + res2D + " in " + time2D + " ms");

        // 1D
        start = System.nanoTime();
        int res1D = knapsack1D(perfWeights, perfValues, perfW);
        long time1D = (System.nanoTime() - start) / 1_000_000;
        System.out.println("  1D Optimized:  " + res1D + " in " + time1D + " ms");

        // Memo
        start = System.nanoTime();
        int resMemo = knapsackMemo(perfWeights, perfValues, perfW);
        long timeMemo = (System.nanoTime() - start) / 1_000_000;
        System.out.println("  Memoization:   " + resMemo + " in " + timeMemo + " ms");

        System.out.println("  All agree: " + (res2D == res1D && res1D == resMemo ? "✓" : "✗"));
        System.out.println("  1D speedup vs 2D: "
            + String.format("%.1f", (double) time2D / Math.max(time1D, 1)) + "x");
        System.out.println();

        // ── TEST 9: Edge Cases ──
        System.out.println("═══ TEST 9: Edge Cases ═══");

        // No items
        System.out.println("No items: "
            + knapsack1D(new int[]{}, new int[]{}, 10));

        // Zero capacity
        System.out.println("Zero capacity: "
            + knapsack1D(new int[]{1, 2}, new int[]{10, 20}, 0));

        // All items too heavy
        System.out.println("All too heavy: "
            + knapsack1D(new int[]{5, 10}, new int[]{100, 200}, 4));

        // Single item fits
        System.out.println("Single item fits: "
            + knapsack1D(new int[]{3}, new int[]{50}, 5));

        // Single item doesn't fit
        System.out.println("Single item too heavy: "
            + knapsack1D(new int[]{10}, new int[]{50}, 5));

        // All items fit
        System.out.println("All items fit: "
            + knapsack1D(new int[]{1, 1, 1}, new int[]{10, 20, 30}, 10));
    }
}