import java.util.HashMap;
import java.util.Arrays;

public class ClimbingStairs {

    // ─────────────────────────────────────────────
    // APPROACH 1: Plain Recursion (EXPONENTIAL — for comparison only)
    // ─────────────────────────────────────────────
    static int callCount = 0;

    public static int climbStairsRecursive(int n) {
        callCount++;

        if (n <= 1) return 1;

        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }

    // ─────────────────────────────────────────────
    // APPROACH 2: Memoization (Top-Down DP)
    // ─────────────────────────────────────────────
    public static int climbStairsMemo(int n) {
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return memoHelper(n, memo);
    }

    private static int memoHelper(int n, int[] memo) {
        // Base cases
        if (n <= 1) return 1;

        // Check cache FIRST
        if (memo[n] != -1) return memo[n];

        // Compute and cache
        memo[n] = memoHelper(n - 1, memo) + memoHelper(n - 2, memo);
        return memo[n];
    }

    // ─────────────────────────────────────────────
    // APPROACH 3: Tabulation (Bottom-Up DP — Array)
    // ─────────────────────────────────────────────
    public static int climbStairsTabulation(int n) {

        if (n <= 1) return 1;

        int[] dp = new int[n + 1];

        // Base cases
        dp[0] = 1;
        dp[1] = 1;

        // Fill table bottom-up
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[n];
    }

    // ─────────────────────────────────────────────
    // APPROACH 4: Space-Optimized Tabulation (OPTIMAL)
    // ─────────────────────────────────────────────
    public static int climbStairsOptimal(int n) {

        if (n <= 1) return 1;

        int prev2 = 1; // dp[0]
        int prev1 = 1; // dp[1]

        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Climbing with 1, 2, or 3 steps
    // ─────────────────────────────────────────────
    public static int climbStairs3Steps(int n) {

        if (n <= 1) return 1;
        if (n == 2) return 2;

        int prev3 = 1; // dp[0]
        int prev2 = 1; // dp[1]
        int prev1 = 2; // dp[2]

        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2 + prev3;
            prev3 = prev2;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Climbing with arbitrary step sizes
    // Given array steps = [1, 3, 5] → can take 1, 3, or 5 steps
    // ─────────────────────────────────────────────
    public static int climbStairsCustom(int n, int[] steps) {

        int[] dp = new int[n + 1];
        dp[0] = 1; // base case: one way to stand at ground

        for (int i = 1; i <= n; i++) {
            for (int step : steps) {
                if (i - step >= 0) {
                    dp[i] += dp[i - step];
                }
            }
        }

        return dp[n];
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Minimum cost climbing stairs
    // cost[i] = cost to step on stair i
    // Can start from step 0 or step 1
    // ─────────────────────────────────────────────
    public static int minCostClimbingStairs(int[] cost) {

        int n = cost.length;

        // dp[i] = min cost to reach step i
        // Can come from i-1 (paid cost[i-1]) or i-2 (paid cost[i-2])
        int prev2 = 0; // cost to reach step 0 (start here free)
        int prev1 = 0; // cost to reach step 1 (start here free)

        for (int i = 2; i <= n; i++) {
            int current = Math.min(prev1 + cost[i - 1], prev2 + cost[i - 2]);
            prev2 = prev1;
            prev1 = current;
        }

        return prev1; // cost to reach step n (the top, beyond last step)
    }

    // ─────────────────────────────────────────────
    // VARIANT 4: Count with modulo (for large n)
    // ─────────────────────────────────────────────
    public static long climbStairsMod(int n, long mod) {

        if (n <= 1) return 1;

        long prev2 = 1;
        long prev1 = 1;

        for (int i = 2; i <= n; i++) {
            long current = (prev1 + prev2) % mod;
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch all approaches compute step by step
    // ─────────────────────────────────────────────
    public static void climbStairsWithTrace(int n) {

        System.out.println("Climbing " + n + " stairs");
        System.out.println("═══════════════════════════════════════════");

        // Show tabulation table building
        System.out.println("TABULATION (Bottom-Up):");
        int[] dp = new int[n + 1];
        dp[0] = 1;
        if (n >= 1) dp[1] = 1;

        System.out.println("  dp[0] = 1 (base)");
        if (n >= 1) System.out.println("  dp[1] = 1 (base)");

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
            System.out.println("  dp[" + i + "] = dp[" + (i - 1) + "](" + dp[i - 1]
                + ") + dp[" + (i - 2) + "](" + dp[i - 2] + ") = " + dp[i]);
        }

        System.out.println("  Table: " + Arrays.toString(dp));
        System.out.println("  Answer: " + dp[n]);
        System.out.println();

        // Show space-optimized rolling
        System.out.println("SPACE-OPTIMIZED (Rolling Variables):");
        int prev2 = 1, prev1 = 1;
        System.out.println("  Initial: prev2=1 (dp[0]), prev1=1 (dp[1])");

        for (int i = 2; i <= n; i++) {
            int current = prev1 + prev2;
            System.out.println("  i=" + i + ": current = " + prev1 + " + " + prev2
                + " = " + current
                + " → prev2=" + prev1 + ", prev1=" + current);
            prev2 = prev1;
            prev1 = current;
        }

        System.out.println("  Answer: " + prev1);
    }

    // ─────────────────────────────────────────────
    // MEMOIZATION TRACE — Show cache hits
    // ─────────────────────────────────────────────
    public static int climbStairsMemoTrace(int n) {

        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);

        System.out.println("MEMOIZATION (Top-Down) for n=" + n + ":");
        int result = memoTraceHelper(n, memo, 0);
        System.out.println("  Answer: " + result);
        return result;
    }

    private static int memoTraceHelper(int n, int[] memo, int depth) {

        String indent = "  " + "  ".repeat(depth);

        if (n <= 1) {
            System.out.println(indent + "ways(" + n + ") = 1 (base case)");
            return 1;
        }

        if (memo[n] != -1) {
            System.out.println(indent + "ways(" + n + ") = " + memo[n] + " (CACHED ✓)");
            return memo[n];
        }

        System.out.println(indent + "ways(" + n + ") = ways(" + (n - 1)
            + ") + ways(" + (n - 2) + ") → computing...");

        int left = memoTraceHelper(n - 1, memo, depth + 1);
        int right = memoTraceHelper(n - 2, memo, depth + 1);
        memo[n] = left + right;

        System.out.println(indent + "ways(" + n + ") = " + left + " + "
            + right + " = " + memo[n] + " → CACHED");

        return memo[n];
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 43: Climbing Stairs / Fibonacci                  ║");
        System.out.println("║  Pattern: Dynamic Programming → Memoization / Tabulation  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Tabulation Trace ──
        System.out.println("═══ TEST 1: Tabulation Trace (n=8) ═══");
        climbStairsWithTrace(8);
        System.out.println();

        // ── TEST 2: Memoization Trace ──
        System.out.println("═══ TEST 2: Memoization Trace (n=6) ═══");
        climbStairsMemoTrace(6);
        System.out.println();

        // ── TEST 3: All Approaches Agree ──
        System.out.println("═══ TEST 3: Verify All Approaches Agree ═══");
        System.out.println("┌───────┬───────────┬──────────┬────────────┬───────────┐");
        System.out.println("│   n   │ Recursive │ Memo     │ Tabulation │ Optimized │");
        System.out.println("├───────┼───────────┼──────────┼────────────┼───────────┤");

        for (int n = 0; n <= 15; n++) {
            int rec = (n <= 25) ? climbStairsRecursive(n) : -1;
            int mem = climbStairsMemo(n);
            int tab = climbStairsTabulation(n);
            int opt = climbStairsOptimal(n);

            System.out.printf("│ %5d │ %9d │ %8d │ %10d │ %9d │%n",
                n, rec, mem, tab, opt);
        }
        System.out.println("└───────┴───────────┴──────────┴────────────┴───────────┘");
        System.out.println();

        // ── TEST 4: Exponential vs Linear — Call Count ──
        System.out.println("═══ TEST 4: Exponential vs Linear — Call Counts ═══");
        System.out.println("┌───────┬──────────────────┬─────────────────┬─────────────┐");
        System.out.println("│   n   │ Recursive Calls  │ Memo Lookups    │ Tab Steps   │");
        System.out.println("├───────┼──────────────────┼─────────────────┼─────────────┤");

        for (int n : new int[]{5, 10, 15, 20, 25, 30}) {

            // Recursive call count
            callCount = 0;
            if (n <= 30) climbStairsRecursive(n);
            long recCalls = callCount;

            // Memo: exactly n+1 unique computations + cache hits
            // Tab: exactly n-1 iterations

            System.out.printf("│ %5d │ %16d │ %15d │ %11d │%n",
                n, recCalls, n + 1, Math.max(n - 1, 0));
        }
        System.out.println("└───────┴──────────────────┴─────────────────┴─────────────┘");
        System.out.println("Notice: Recursive calls DOUBLE with each +5 in n (exponential)");
        System.out.println("        Memo and Tab grow LINEARLY");
        System.out.println();

        // ── TEST 5: Performance Timing ──
        System.out.println("═══ TEST 5: Performance Timing ═══");
        int[] testSizes = {10, 20, 30, 40, 45};

        for (int n : testSizes) {
            // Recursive (only for small n)
            long recTime = -1;
            if (n <= 40) {
                long start = System.nanoTime();
                climbStairsRecursive(n);
                recTime = (System.nanoTime() - start) / 1000;
            }

            // Memo
            long start = System.nanoTime();
            int memoResult = climbStairsMemo(n);
            long memoTime = (System.nanoTime() - start) / 1000;

            // Tabulation
            start = System.nanoTime();
            int tabResult = climbStairsTabulation(n);
            long tabTime = (System.nanoTime() - start) / 1000;

            // Optimized
            start = System.nanoTime();
            int optResult = climbStairsOptimal(n);
            long optTime = (System.nanoTime() - start) / 1000;

            System.out.println("  n=" + n + " → answer=" + optResult);
            System.out.println("    Recursive: " + (recTime >= 0 ? recTime + " μs" : "SKIPPED (too slow)"));
            System.out.println("    Memo:      " + memoTime + " μs");
            System.out.println("    Tab:       " + tabTime + " μs");
            System.out.println("    Optimized: " + optTime + " μs");
        }
        System.out.println();

        // ── TEST 6: 3-Step Variant ──
        System.out.println("═══ TEST 6: Climbing with 1, 2, or 3 Steps ═══");
        for (int n = 0; n <= 10; n++) {
            System.out.println("  n=" + n + " → " + climbStairs3Steps(n) + " ways");
        }
        System.out.println();

        // ── TEST 7: Custom Step Sizes ──
        System.out.println("═══ TEST 7: Custom Step Sizes ═══");
        int[] steps1 = {1, 3, 5};
        System.out.println("Steps = " + Arrays.toString(steps1));
        for (int n = 0; n <= 10; n++) {
            System.out.println("  n=" + n + " → " + climbStairsCustom(n, steps1) + " ways");
        }
        System.out.println();

        // ── TEST 8: Minimum Cost Climbing ──
        System.out.println("═══ TEST 8: Minimum Cost Climbing Stairs ═══");
        int[] cost1 = {10, 15, 20};
        System.out.println("Costs: " + Arrays.toString(cost1));
        System.out.println("Min cost: " + minCostClimbingStairs(cost1));
        System.out.println("(Optimal: start at step 1 (cost 15), jump 2 to top = 15)");

        int[] cost2 = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
        System.out.println("\nCosts: " + Arrays.toString(cost2));
        System.out.println("Min cost: " + minCostClimbingStairs(cost2));
        System.out.println("(Optimal: take cheap steps, skip expensive ones = 6)");
        System.out.println();

        // ── TEST 9: Large n with Modulo ──
        System.out.println("═══ TEST 9: Large n with Modulo ═══");
        long MOD = 1_000_000_007L;
        int[] largeNs = {100, 1000, 10000, 100000, 1000000};
        for (int n : largeNs) {
            long start2 = System.nanoTime();
            long result = climbStairsMod(n, MOD);
            long time = (System.nanoTime() - start2) / 1000;
            System.out.println("  n=" + n + " → " + result + " (mod 10^9+7) in " + time + " μs");
        }
        System.out.println();

        // ── TEST 10: Edge Cases ──
        System.out.println("═══ TEST 10: Edge Cases ═══");
        System.out.println("n=0: " + climbStairsOptimal(0) + " (one way: do nothing)");
        System.out.println("n=1: " + climbStairsOptimal(1) + " (one way: take 1 step)");
        System.out.println("n=2: " + climbStairsOptimal(2) + " (1+1 or 2)");
        System.out.println("n=45: " + climbStairsOptimal(45) + " (max for int)");
    }
}