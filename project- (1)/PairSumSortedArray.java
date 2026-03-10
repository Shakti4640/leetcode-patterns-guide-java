public class PairSumSortedArray {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Two Pointers — Left/Right Convergence
    // ─────────────────────────────────────────────
    public static int[] twoSum(int[] numbers, int target) {
        
        // Step 1: Initialize two pointers at opposite ends
        int left = 0;
        int right = numbers.length - 1;
        
        // Step 2: Converge inward until pointers meet
        while (left < right) {
            
            // Step 3: Compute current sum (use long to avoid overflow)
            long currentSum = (long) numbers[left] + (long) numbers[right];
            
            // Step 4: Compare with target and decide direction
            if (currentSum == target) {
                // FOUND — return 1-based indices
                return new int[] { left + 1, right + 1 };
            } 
            else if (currentSum < target) {
                // Sum too small → need a BIGGER left value → move left forward
                left++;
            } 
            else {
                // Sum too big → need a SMALLER right value → move right backward
                right--;
            }
        }
        
        // Step 5: No pair found (problem guarantees one exists, but safety net)
        return new int[] { -1, -1 };
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE (for comparison — DO NOT use in interviews)
    // ─────────────────────────────────────────────
    public static int[] twoSumBruteForce(int[] numbers, int target) {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[] { i + 1, j + 1 };
                }
            }
        }
        return new int[] { -1, -1 };
    }
    
    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the convergence happen
    // ─────────────────────────────────────────────
    public static void twoSumWithTrace(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;
        int step = 1;
        
        System.out.println("Array: ");
        System.out.print("  [");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i]);
            if (i < numbers.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Target: " + target);
        System.out.println("─────────────────────────────");
        
        while (left < right) {
            long currentSum = (long) numbers[left] + (long) numbers[right];
            
            System.out.println("Step " + step + ":");
            System.out.println("  left=" + left 
                + " (val=" + numbers[left] + ")"
                + "  right=" + right 
                + " (val=" + numbers[right] + ")");
            System.out.println("  sum = " + numbers[left] 
                + " + " + numbers[right] 
                + " = " + currentSum);
            
            if (currentSum == target) {
                System.out.println("  → MATCH FOUND at indices [" 
                    + left + ", " + right + "]");
                System.out.println("  → 1-based: [" 
                    + (left + 1) + ", " + (right + 1) + "]");
                return;
            } 
            else if (currentSum < target) {
                System.out.println("  → " + currentSum + " < " + target 
                    + " → too small → move left++");
                left++;
            } 
            else {
                System.out.println("  → " + currentSum + " > " + target 
                    + " → too big → move right--");
                right--;
            }
            
            System.out.println();
            step++;
        }
        
        System.out.println("No pair found.");
    }
    
    // ─────────────────────────────────────────────
    // VARIANT: Return ALL pairs (not just one)
    // ─────────────────────────────────────────────
    public static java.util.List<int[]> twoSumAllPairs(int[] numbers, int target) {
        java.util.List<int[]> result = new java.util.ArrayList<>();
        int left = 0;
        int right = numbers.length - 1;
        
        while (left < right) {
            long currentSum = (long) numbers[left] + (long) numbers[right];
            
            if (currentSum == target) {
                result.add(new int[] { numbers[left], numbers[right] });
                
                // Skip duplicates from left
                int leftVal = numbers[left];
                while (left < right && numbers[left] == leftVal) {
                    left++;
                }
                // Skip duplicates from right
                int rightVal = numbers[right];
                while (left < right && numbers[right] == rightVal) {
                    right--;
                }
            } 
            else if (currentSum < target) {
                left++;
            } 
            else {
                right--;
            }
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  PROJECT 1: Pair Sum in Sorted Array     ║");
        System.out.println("║  Pattern: Two Pointers — L/R Convergence ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
        
        // ── TEST 1: Basic Example ──
        System.out.println("═══ TEST 1: Basic Example ═══");
        int[] arr1 = {1, 3, 5, 7, 11, 15};
        int target1 = 16;
        twoSumWithTrace(arr1, target1);
        System.out.println();
        
        // ── TEST 2: Answer in the middle ──
        System.out.println("═══ TEST 2: Answer in the Middle ═══");
        int[] arr2 = {2, 4, 6, 10, 14};
        int target2 = 16;
        twoSumWithTrace(arr2, target2);
        System.out.println();
        
        // ── TEST 3: Negative numbers ──
        System.out.println("═══ TEST 3: Negative Numbers ═══");
        int[] arr3 = {-5, -3, 0, 2, 8};
        int target3 = -1;
        twoSumWithTrace(arr3, target3);
        System.out.println();
        
        // ── TEST 4: First and last elements ──
        System.out.println("═══ TEST 4: First and Last Elements ═══");
        int[] arr4 = {1, 2, 3, 4, 5};
        int target4 = 6;
        twoSumWithTrace(arr4, target4);
        System.out.println();
        
        // ── TEST 5: All pairs variant ──
        System.out.println("═══ TEST 5: All Pairs Variant ═══");
        int[] arr5 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int target5 = 10;
        java.util.List<int[]> allPairs = twoSumAllPairs(arr5, target5);
        System.out.println("All pairs summing to " + target5 + ":");
        for (int[] pair : allPairs) {
            System.out.println("  [" + pair[0] + ", " + pair[1] + "]");
        }
        System.out.println();
        
        // ── TEST 6: Performance comparison ──
        System.out.println("═══ TEST 6: Performance Comparison ═══");
        int size = 100000;
        int[] largeArr = new int[size];
        for (int i = 0; i < size; i++) {
            largeArr[i] = i * 2;  // sorted even numbers: 0, 2, 4, ...
        }
        int largeTarget = largeArr[0] + largeArr[size - 1]; // first + last
        
        // Two Pointers
        long startTime = System.nanoTime();
        int[] result1 = twoSum(largeArr, largeTarget);
        long twoPointerTime = System.nanoTime() - startTime;
        System.out.println("Two Pointers: [" + result1[0] + ", " + result1[1] 
            + "] → " + (twoPointerTime / 1000) + " microseconds");
        
        // Brute Force
        startTime = System.nanoTime();
        int[] result2 = twoSumBruteForce(largeArr, largeTarget);
        long bruteForceTime = System.nanoTime() - startTime;
        System.out.println("Brute Force:  [" + result2[0] + ", " + result2[1] 
            + "] → " + (bruteForceTime / 1000) + " microseconds");
        
        System.out.println("Speedup: " + (bruteForceTime / Math.max(twoPointerTime, 1)) + "x faster");
    }
}