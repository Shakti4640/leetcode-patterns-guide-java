import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class JumpGame {

    // ═══════════════════════════════════════════════════════
    // APPROACH A: Greedy — Farthest Reachable (OPTIMAL)
    // ═══════════════════════════════════════════════════════
    
    public static boolean canJump(int[] nums) {
        
        int n = nums.length;
        int farthest = 0;
        
        for (int i = 0; i < n; i++) {
            
            // If current index is beyond farthest reachable → stranded
            if (i > farthest) {
                return false;
            }
            
            // Update farthest reachable from this position
            farthest = Math.max(farthest, i + nums[i]);
            
            // Early exit: if farthest covers the last index → success
            if (farthest >= n - 1) {
                return true;
            }
        }
        
        // Loop completed → farthest >= n-1 guaranteed
        // (otherwise would have returned false when i > farthest)
        return true;
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH B: DP — Bottom-Up (for comparison)
    // Builds on Project 43 (Climbing Stairs) foundation
    // ═══════════════════════════════════════════════════════
    
    public static boolean canJumpDP(int[] nums) {
        
        int n = nums.length;
        
        // dp[i] = can we reach index i from index 0?
        boolean[] dp = new boolean[n];
        dp[0] = true; // starting point
        
        for (int i = 1; i < n; i++) {
            // Check all previous indices: can any reach index i?
            for (int j = i - 1; j >= 0; j--) {
                if (dp[j] && j + nums[j] >= i) {
                    dp[i] = true;
                    break; // found one → no need to check more
                }
            }
        }
        
        return dp[n - 1];
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH C: DP — Backward (Good/Bad index marking)
    // ═══════════════════════════════════════════════════════
    
    public static boolean canJumpDPBackward(int[] nums) {
        
        int n = nums.length;
        
        // State: GOOD = can reach end from here, BAD = cannot, UNKNOWN
        // 0 = unknown, 1 = good, -1 = bad
        int[] memo = new int[n];
        memo[n - 1] = 1; // last index is trivially GOOD
        
        // Work backward
        for (int i = n - 2; i >= 0; i--) {
            // Check all positions reachable from i
            int furthestJump = Math.min(i + nums[i], n - 1);
            for (int j = i + 1; j <= furthestJump; j++) {
                if (memo[j] == 1) {
                    memo[i] = 1; // can reach a GOOD index → this is GOOD
                    break;
                }
            }
            if (memo[i] != 1) {
                memo[i] = -1; // no reachable GOOD index → BAD
            }
        }
        
        return memo[0] == 1;
    }
    
    // ═══════════════════════════════════════════════════════
    // APPROACH D: BFS on Implicit Graph
    // Connects to Project 17 (Tree BFS) and Project 47 (Kahn's BFS)
    // ═══════════════════════════════════════════════════════
    
    public static boolean canJumpBFS(int[] nums) {
        
        int n = nums.length;
        if (n <= 1) return true;
        
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(0);
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            // Try all jumps from current position
            for (int jump = 1; jump <= nums[current]; jump++) {
                int next = current + jump;
                
                if (next >= n - 1) return true; // reached or passed the end
                
                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
        
        return false;
    }
    
    // ═══════════════════════════════════════════════════════
    // VARIANT: Greedy with "Fuel Tank" Mental Model
    // Equivalent to farthest-reachable but different perspective
    // ═══════════════════════════════════════════════════════
    
    public static boolean canJumpFuel(int[] nums) {
        
        int n = nums.length;
        int fuel = 0;
        
        for (int i = 0; i < n - 1; i++) {
            // At each station, take the better fuel option
            fuel = Math.max(fuel, nums[i]);
            
            // If no fuel → stranded
            if (fuel == 0) return false;
            
            // Use one unit of fuel to move to next index
            fuel--;
        }
        
        // Reached the last index (or beyond)
        return true;
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the Greedy Scan
    // ═══════════════════════════════════════════════════════
    
    public static void canJumpWithTrace(int[] nums) {
        
        int n = nums.length;
        
        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("Goal:  Reach index " + (n - 1));
        System.out.println();
        
        // Visual representation
        System.out.print("Index:  ");
        for (int i = 0; i < n; i++) System.out.printf("%-4d", i);
        System.out.println();
        System.out.print("Value:  ");
        for (int i = 0; i < n; i++) System.out.printf("%-4d", nums[i]);
        System.out.println();
        System.out.print("Reach:  ");
        for (int i = 0; i < n; i++) {
            int reach = Math.min(i + nums[i], n - 1);
            System.out.printf("→%-3d", reach);
        }
        System.out.println();
        System.out.println();
        
        int farthest = 0;
        
        System.out.println("─── Greedy Scan ───");
        
        for (int i = 0; i < n; i++) {
            System.out.println("Step " + i + ":");
            System.out.println("  i=" + i + "  farthest=" + farthest 
                + "  nums[" + i + "]=" + nums[i]);
            
            if (i > farthest) {
                System.out.println("  → i(" + i + ") > farthest(" + farthest 
                    + ") → STRANDED!");
                System.out.println("\n═══ ANSWER: FALSE — Cannot reach the end ═══");
                return;
            }
            
            int oldFarthest = farthest;
            farthest = Math.max(farthest, i + nums[i]);
            
            if (farthest > oldFarthest) {
                System.out.println("  → Reachable! Extend farthest: " 
                    + oldFarthest + " → " + farthest);
            } else {
                System.out.println("  → Reachable! Farthest unchanged: " + farthest);
            }
            
            // Show reachable range
            System.out.print("  → Reachable range: [0");
            System.out.println("..." + Math.min(farthest, n - 1) + "]");
            
            if (farthest >= n - 1) {
                System.out.println("  → farthest(" + farthest + ") >= last(" 
                    + (n - 1) + ") → REACHED THE END!");
                System.out.println("\n═══ ANSWER: TRUE — Can reach the end ═══");
                return;
            }
            
            System.out.println();
        }
        
        System.out.println("\n═══ ANSWER: TRUE — Reached the end ═══");
    }
    
    // ═══════════════════════════════════════════════════════
    // TRACE: Visualize the Fuel Tank Model
    // ═══════════════════════════════════════════════════════
    
    public static void canJumpFuelWithTrace(int[] nums) {
        
        int n = nums.length;
        
        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("Fuel Tank Model: start with 0 fuel\n");
        
        int fuel = 0;
        
        for (int i = 0; i < n - 1; i++) {
            int oldFuel = fuel;
            fuel = Math.max(fuel, nums[i]);
            
            System.out.println("Index " + i + ": station offers " + nums[i] 
                + " fuel, had " + oldFuel + " → take max → fuel=" + fuel);
            
            if (fuel == 0) {
                System.out.println("  → OUT OF FUEL! Stranded at index " + i);
                System.out.println("  → ANSWER: FALSE");
                return;
            }
            
            fuel--;
            System.out.println("  → Move forward → fuel=" + fuel);
        }
        
        System.out.println("Arrived at index " + (n - 1) + "! ANSWER: TRUE");
    }
    
    // ═══════════════════════════════════════════════════════
    // BONUS: Jump Game II — Minimum Jumps to Reach End
    // Shows how the pattern extends (BFS-like greedy)
    // ═══════════════════════════════════════════════════════
    
    public static int minJumps(int[] nums) {
        
        int n = nums.length;
        if (n <= 1) return 0;
        
        int jumps = 0;        // number of jumps made
        int currentEnd = 0;   // farthest we can reach with 'jumps' jumps
        int farthest = 0;     // farthest we can reach with 'jumps+1' jumps
        
        for (int i = 0; i < n - 1; i++) {
            
            // Extend the farthest reachable with one more jump
            farthest = Math.max(farthest, i + nums[i]);
            
            // If we've reached the boundary of current jump level
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
                
                // If current level covers the end → done
                if (currentEnd >= n - 1) {
                    return jumps;
                }
            }
        }
        
        return jumps;
    }
    
    // ═══════════════════════════════════════════════════════
    // MAIN — Run all examples
    // ═══════════════════════════════════════════════════════
    
    public static void main(String[] args) {
        
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 49: Jump Game — Can You Reach the End?  ║");
        System.out.println("║  Pattern: Greedy → Farthest Reachable Tracking   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Reachable ──
        System.out.println("═══ TEST 1: Reachable ═══");
        canJumpWithTrace(new int[]{2, 3, 1, 1, 4});
        System.out.println();
        
        // ── TEST 2: Not reachable ──
        System.out.println("═══ TEST 2: Not Reachable (Stuck at zero) ═══");
        canJumpWithTrace(new int[]{3, 2, 1, 0, 4});
        System.out.println();
        
        // ── TEST 3: Single element ──
        System.out.println("═══ TEST 3: Single Element ═══");
        canJumpWithTrace(new int[]{0});
        System.out.println();
        
        // ── TEST 4: Direct jump ──
        System.out.println("═══ TEST 4: Direct Jump to End ═══");
        canJumpWithTrace(new int[]{5, 0, 0, 0, 0});
        System.out.println();
        
        // ── TEST 5: All ones ──
        System.out.println("═══ TEST 5: All Ones — Hopping ═══");
        canJumpWithTrace(new int[]{1, 1, 1, 1, 1});
        System.out.println();
        
        // ── TEST 6: Zero trap ──
        System.out.println("═══ TEST 6: Zero Trap in Middle ═══");
        canJumpWithTrace(new int[]{1, 0, 1, 0});
        System.out.println();
        
        // ── TEST 7: Jump over zero ──
        System.out.println("═══ TEST 7: Jump Over Zero ═══");
        canJumpWithTrace(new int[]{2, 0, 1, 0, 1});
        System.out.println();
        
        // ── TEST 8: Fuel Tank Model ──
        System.out.println("═══ TEST 8: Fuel Tank Model ═══");
        canJumpFuelWithTrace(new int[]{2, 3, 1, 1, 4});
        System.out.println();
        canJumpFuelWithTrace(new int[]{3, 2, 1, 0, 4});
        System.out.println();
        
        // ── TEST 9: Cross-verification ──
        System.out.println("═══ TEST 9: Cross-Verification (All Approaches) ═══");
        int[][] testCases = {
            {2, 3, 1, 1, 4},
            {3, 2, 1, 0, 4},
            {0},
            {5, 0, 0, 0, 0},
            {1, 1, 1, 1, 1},
            {1, 0, 1, 0},
            {2, 0, 0},
            {2, 0, 1, 0, 1},
            {1, 2, 3},
            {0, 1},
            {3, 0, 8, 2, 0, 0, 1},
            {2, 5, 0, 0},
        };
        
        for (int t = 0; t < testCases.length; t++) {
            int[] nums = testCases[t];
            boolean greedy = canJump(nums);
            boolean fuel = canJumpFuel(nums);
            boolean dpForward = canJumpDP(nums);
            boolean dpBackward = canJumpDPBackward(nums);
            boolean bfs = canJumpBFS(nums);
            
            boolean allMatch = (greedy == fuel) && (fuel == dpForward) 
                && (dpForward == dpBackward) && (dpBackward == bfs);
            String status = allMatch ? "✓" : "✗";
            
            System.out.println("  Test " + (t + 1) 
                + ": " + Arrays.toString(nums)
                + " → Greedy=" + greedy
                + "  Fuel=" + fuel
                + "  DP=" + dpForward
                + "  DPBack=" + dpBackward
                + "  BFS=" + bfs
                + "  " + status);
        }
        System.out.println();
        
        // ── TEST 10: Jump Game II — Minimum Jumps ──
        System.out.println("═══ TEST 10: Jump Game II — Minimum Jumps ═══");
        int[][] jumpTests = {
            {2, 3, 1, 1, 4},
            {2, 3, 0, 1, 4},
            {1, 1, 1, 1, 1},
            {5, 0, 0, 0, 0},
            {1, 2, 1, 1, 1},
        };
        
        for (int[] nums : jumpTests) {
            System.out.println("  " + Arrays.toString(nums) 
                + " → min jumps = " + minJumps(nums));
        }
        System.out.println();
        
        // ── TEST 11: Performance benchmark ──
        System.out.println("═══ TEST 11: Performance Benchmark ═══");
        int size = 10_000_000;
        int[] large = new int[size];
        // Reachable case: all 1s
        Arrays.fill(large, 1);
        
        long startTime = System.nanoTime();
        boolean result1 = canJump(large);
        long greedyTime = System.nanoTime() - startTime;
        
        System.out.println("  n = " + size + " (all 1s — reachable)");
        System.out.println("  Greedy: " + result1 + " → " 
            + (greedyTime / 1_000_000) + " ms");
        
        // Unreachable case: all 1s except a 0 in the middle
        large[size / 2] = 0;
        // Need to make sure we get stuck: set previous to 1
        large[size / 2 - 1] = 1;
        
        startTime = System.nanoTime();
        boolean result2 = canJump(large);
        long greedyTime2 = System.nanoTime() - startTime;
        
        System.out.println("  n = " + size + " (zero trap at " + (size / 2) + ")");
        System.out.println("  Greedy: " + result2 + " → " 
            + (greedyTime2 / 1_000_000) + " ms");
        System.out.println();
        
        // Compare greedy vs DP on smaller input
        int smallSize = 10_000;
        int[] small = new int[smallSize];
        Arrays.fill(small, 1);
        
        startTime = System.nanoTime();
        canJump(small);
        long greedySmall = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        canJumpDP(small);
        long dpSmall = System.nanoTime() - startTime;
        
        System.out.println("  n = " + smallSize + " comparison:");
        System.out.println("  Greedy: " + (greedySmall / 1_000) + " μs");
        System.out.println("  DP:     " + (dpSmall / 1_000) + " μs");
        System.out.println("  Speedup: ~" 
            + (dpSmall / Math.max(greedySmall, 1)) + "x");
    }
}