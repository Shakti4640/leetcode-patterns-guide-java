import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Sort + Anchor + Two Pointers
    // ─────────────────────────────────────────────
    public static List<List<Integer>> threeSum(int[] nums) {
        
        List<List<Integer>> result = new ArrayList<>();
        
        // Step 1: Sort the array — enables two pointers + duplicate detection
        Arrays.sort(nums);
        
        int n = nums.length;
        
        // Step 2: Iterate through each element as the anchor
        for (int i = 0; i < n - 2; i++) {
            
            // ── OPTIMIZATION 1: Early termination ──
            // If smallest remaining element is positive, no triplet can sum to 0
            if (nums[i] > 0) {
                break;
            }
            
            // ── DUPLICATE SKIP LEVEL 1: Skip duplicate anchors ──
            // Keep FIRST occurrence, skip subsequent identical values
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // Step 3: Two pointers for the remaining subarray
            int target = -nums[i]; // b + c must equal this
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                
                int sum = nums[left] + nums[right];
                
                if (sum == target) {
                    // ── MATCH FOUND ──
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // ── DUPLICATE SKIP LEVEL 2: Skip duplicate left values ──
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    
                    // ── DUPLICATE SKIP LEVEL 3: Skip duplicate right values ──
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    
                    // Move past the last duplicate on both sides
                    left++;
                    right--;
                    
                } else if (sum < target) {
                    // Sum too small → need bigger left value → move left forward
                    // (Project 1 logic: left++ increases sum in sorted array)
                    left++;
                    
                } else {
                    // Sum too big → need smaller right value → move right backward
                    // (Project 1 logic: right-- decreases sum in sorted array)
                    right--;
                }
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // OPTIMIZED SOLUTION: With additional pruning
    // ─────────────────────────────────────────────
    public static List<List<Integer>> threeSumOptimized(int[] nums) {
        
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;
        
        for (int i = 0; i < n - 2; i++) {
            
            // Pruning 1: If smallest element > 0, no solution possible
            if (nums[i] > 0) break;
            
            // Skip duplicate anchors
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            // Pruning 2: If minimum possible sum > 0, skip rest
            // Minimum sum with this anchor = nums[i] + nums[i+1] + nums[i+2]
            if (nums[i] + nums[i + 1] + nums[i + 2] > 0) break;
            
            // Pruning 3: If maximum possible sum < 0, skip this anchor
            // Maximum sum with this anchor = nums[i] + nums[n-2] + nums[n-1]
            if (nums[i] + nums[n - 2] + nums[n - 1] < 0) continue;
            
            int target = -nums[i];
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == target) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++;
                    right--;
                    
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE: O(n³) — for correctness comparison only
    // ─────────────────────────────────────────────
    public static List<List<Integer>> threeSumBruteForce(int[] nums) {
        
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // sort for consistent triplet ordering + dedup
        int n = nums.length;
        
        for (int i = 0; i < n - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            for (int j = i + 1; j < n - 1; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                
                for (int k = j + 1; k < n; k++) {
                    if (k > j + 1 && nums[k] == nums[k - 1]) continue;
                    
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    }
                }
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void threeSumWithTrace(int[] nums) {
        
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        
        System.out.print("  Original: [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        System.out.print("  Sorted:   [");
        for (int i = 0; i < sorted.length; i++) {
            System.out.print(sorted[i]);
            if (i < sorted.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("  ─────────────────────────────────────────");
        
        int n = sorted.length;
        List<List<Integer>> result = new ArrayList<>();
        
        for (int i = 0; i < n - 2; i++) {
            
            if (sorted[i] > 0) {
                System.out.println("  Anchor i=" + i + " val=" + sorted[i] 
                    + " → POSITIVE → BREAK (no more solutions)");
                break;
            }
            
            if (i > 0 && sorted[i] == sorted[i - 1]) {
                System.out.println("  Anchor i=" + i + " val=" + sorted[i] 
                    + " → DUPLICATE ANCHOR → SKIP");
                continue;
            }
            
            int target = -sorted[i];
            int left = i + 1;
            int right = n - 1;
            
            System.out.println("  Anchor i=" + i + " val=" + sorted[i] 
                + " → target for pair = " + target);
            
            while (left < right) {
                int sum = sorted[left] + sorted[right];
                
                System.out.println("    left=" + left + "(" + sorted[left] + ")"
                    + " right=" + right + "(" + sorted[right] + ")"
                    + " → sum=" + sum + " vs target=" + target);
                
                if (sum == target) {
                    List<Integer> triplet = Arrays.asList(
                        sorted[i], sorted[left], sorted[right]);
                    result.add(triplet);
                    System.out.println("    ★ MATCH: " + triplet);
                    
                    while (left < right && sorted[left] == sorted[left + 1]) {
                        System.out.println("    → skip dup left: " + sorted[left]);
                        left++;
                    }
                    while (left < right && sorted[right] == sorted[right - 1]) {
                        System.out.println("    → skip dup right: " + sorted[right]);
                        right--;
                    }
                    left++;
                    right--;
                    
                } else if (sum < target) {
                    System.out.println("    → too small → left++");
                    left++;
                } else {
                    System.out.println("    → too big → right--");
                    right--;
                }
            }
            System.out.println();
        }
        
        System.out.println("  FINAL RESULT: " + result);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: 3Sum Closest (find triplet closest to target)
    // ─────────────────────────────────────────────
    public static int threeSumClosest(int[] nums, int target) {
        
        Arrays.sort(nums);
        int n = nums.length;
        int closestSum = nums[0] + nums[1] + nums[2]; // initial guess
        
        for (int i = 0; i < n - 2; i++) {
            
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                // Update closest if this sum is nearer to target
                if (Math.abs(sum - target) < Math.abs(closestSum - target)) {
                    closestSum = sum;
                }
                
                if (sum == target) {
                    return sum; // can't get closer than exact match
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return closestSum;
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: 3Sum Smaller (count triplets with sum < target)
    // ─────────────────────────────────────────────
    public static int threeSumSmaller(int[] nums, int target) {
        
        Arrays.sort(nums);
        int n = nums.length;
        int count = 0;
        
        for (int i = 0; i < n - 2; i++) {
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum < target) {
                    // ALL pairs (left, right), (left, right-1), ..., (left, left+1)
                    // also have sum < target because reducing right reduces sum
                    count += (right - left);
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return count;
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 11: Three Sum — All Unique Triplets    ║");
        System.out.println("║  Pattern: Two Pointers → Multi-Pointer + Dedup  ║");
        System.out.println("╚═════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Classic example ──
        System.out.println("═══ TEST 1: Classic Example ═══");
        threeSumWithTrace(new int[] {-1, 0, 1, 2, -1, -4});
        
        // ── TEST 2: All zeros ──
        System.out.println("═══ TEST 2: All Zeros ═══");
        threeSumWithTrace(new int[] {0, 0, 0, 0});
        
        // ── TEST 3: No solution ──
        System.out.println("═══ TEST 3: No Solution ═══");
        threeSumWithTrace(new int[] {1, 2, -2, -1});
        
        // ── TEST 4: Multiple duplicates ──
        System.out.println("═══ TEST 4: Multiple Duplicates ═══");
        threeSumWithTrace(new int[] {-2, -2, -1, -1, 0, 0, 1, 1, 2, 2});
        
        // ── TEST 5: Negative heavy ──
        System.out.println("═══ TEST 5: Negative Heavy ═══");
        threeSumWithTrace(new int[] {-4, -3, -2, -1, 0, 1, 2, 3, 4});
        
        // ── TEST 6: Minimum array ──
        System.out.println("═══ TEST 6: Minimum Array (3 elements) ═══");
        System.out.println("  [0, 0, 0] → " + threeSum(new int[] {0, 0, 0}));
        System.out.println("  [1, -1, 0] → " + threeSum(new int[] {1, -1, 0}));
        System.out.println("  [1, 2, 3] → " + threeSum(new int[] {1, 2, 3}));
        System.out.println();
        
        // ── TEST 7: 3Sum Closest variant ──
        System.out.println("═══ TEST 7: 3Sum Closest Variant ═══");
        int[] closestArr = {-1, 2, 1, -4};
        int closestTarget = 1;
        System.out.println("  Array: [-1, 2, 1, -4], Target: " + closestTarget);
        System.out.println("  Closest sum: " 
            + threeSumClosest(closestArr, closestTarget));
        System.out.println("  (Expected: 2 → because -1+2+1=2 is closest to 1)");
        System.out.println();
        
        // ── TEST 8: 3Sum Smaller variant ──
        System.out.println("═══ TEST 8: 3Sum Smaller Variant ═══");
        int[] smallerArr = {-2, 0, 1, 3};
        int smallerTarget = 2;
        System.out.println("  Array: [-2, 0, 1, 3], Target: " + smallerTarget);
        System.out.println("  Count of triplets with sum < " + smallerTarget + ": " 
            + threeSumSmaller(smallerArr, smallerTarget));
        System.out.println("  (Triplets: [-2,0,1]=-1, [-2,0,3]=1 → count=2)");
        System.out.println();
        
        // ── TEST 9: Correctness verification ──
        System.out.println("═══ TEST 9: Correctness Verification ═══");
        int[][] testCases = {
            {-1, 0, 1, 2, -1, -4},
            {0, 0, 0, 0},
            {-2, -2, -1, -1, 0, 0, 1, 1, 2, 2},
            {-4, -3, -2, -1, 0, 1, 2, 3, 4},
            {3, -2, 1, 0},
        };
        
        boolean allPassed = true;
        for (int[] testCase : testCases) {
            List<List<Integer>> optimal = threeSum(testCase.clone());
            List<List<Integer>> brute = threeSumBruteForce(testCase.clone());
            boolean match = optimal.equals(brute);
            
            System.out.print("  ");
            System.out.print(Arrays.toString(testCase));
            System.out.println(" → " + (match ? "✓ PASS" : "✗ FAIL"));
            
            if (!match) {
                System.out.println("    Optimal: " + optimal);
                System.out.println("    Brute:   " + brute);
                allPassed = false;
            }
        }
        System.out.println("  All tests passed: " + allPassed);
        System.out.println();
        
        // ── TEST 10: Performance benchmark ──
        System.out.println("═══ TEST 10: Performance Benchmark ═══");
        int size = 3000;
        int[] largeArr = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(201) - 100; // range [-100, 100]
        }
        
        // Two-pointer approach
        long start = System.nanoTime();
        List<List<Integer>> tpResult = threeSum(largeArr.clone());
        long tpTime = System.nanoTime() - start;
        
        // Optimized approach
        start = System.nanoTime();
        List<List<Integer>> optResult = threeSumOptimized(largeArr.clone());
        long optTime = System.nanoTime() - start;
        
        System.out.println("  Array size: " + size);
        System.out.println("  Two-Pointer: " + tpResult.size() + " triplets in " 
            + (tpTime / 1_000_000) + " ms");
        System.out.println("  Optimized:   " + optResult.size() + " triplets in " 
            + (optTime / 1_000_000) + " ms");
        System.out.println("  Results match: " + (tpResult.size() == optResult.size()));
        System.out.println();
    }
}