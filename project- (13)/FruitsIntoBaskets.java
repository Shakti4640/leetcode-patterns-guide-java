import java.util.HashMap;
import java.util.Map;

public class FruitsIntoBaskets {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Sliding Window + HashMap (K=2)
    // Same template as Project 12 with int[] and K=2
    // ─────────────────────────────────────────────
    public static int totalFruit(int[] fruits) {
        
        // Edge case
        if (fruits == null || fruits.length == 0) return 0;
        
        // Frequency map: fruit type → count in current window
        HashMap<Integer, Integer> fruitFrequency = new HashMap<>();
        
        int windowStart = 0;
        int maxFruits = 0;
        
        for (int windowEnd = 0; windowEnd < fruits.length; windowEnd++) {
            
            // ── EXPAND: add fruit at windowEnd ──
            int rightFruit = fruits[windowEnd];
            fruitFrequency.put(rightFruit,
                fruitFrequency.getOrDefault(rightFruit, 0) + 1);
            
            // ── SHRINK: while more than 2 types → remove from left ──
            while (fruitFrequency.size() > 2) {
                int leftFruit = fruits[windowStart];
                fruitFrequency.put(leftFruit, fruitFrequency.get(leftFruit) - 1);
                
                if (fruitFrequency.get(leftFruit) == 0) {
                    fruitFrequency.remove(leftFruit);
                }
                
                windowStart++;
            }
            
            // ── UPDATE: record maximum valid window ──
            maxFruits = Math.max(maxFruits, windowEnd - windowStart + 1);
        }
        
        return maxFruits;
    }
    
    // ─────────────────────────────────────────────
    // GENERALIZED VERSION: At most K distinct types
    // Demonstrates that K=2 is just a special case
    // ─────────────────────────────────────────────
    public static int totalFruitGeneralized(int[] fruits, int k) {
        
        if (fruits == null || fruits.length == 0 || k == 0) return 0;
        
        HashMap<Integer, Integer> fruitFrequency = new HashMap<>();
        int windowStart = 0;
        int maxFruits = 0;
        
        for (int windowEnd = 0; windowEnd < fruits.length; windowEnd++) {
            
            int rightFruit = fruits[windowEnd];
            fruitFrequency.put(rightFruit,
                fruitFrequency.getOrDefault(rightFruit, 0) + 1);
            
            // Only difference from K=2: compare with parameter k
            while (fruitFrequency.size() > k) {
                int leftFruit = fruits[windowStart];
                fruitFrequency.put(leftFruit, fruitFrequency.get(leftFruit) - 1);
                if (fruitFrequency.get(leftFruit) == 0) {
                    fruitFrequency.remove(leftFruit);
                }
                windowStart++;
            }
            
            maxFruits = Math.max(maxFruits, windowEnd - windowStart + 1);
        }
        
        return maxFruits;
    }
    
    // ─────────────────────────────────────────────
    // OPTIMIZED K=2: Track last-seen positions
    // Specialized for exactly 2 types — faster but less general
    // ─────────────────────────────────────────────
    public static int totalFruitOptimizedK2(int[] fruits) {
        
        if (fruits == null || fruits.length == 0) return 0;
        
        // Track last occurrence index of each type in our 2 baskets
        // When third type appears, jump windowStart past the older type
        HashMap<Integer, Integer> lastSeen = new HashMap<>();
        int windowStart = 0;
        int maxFruits = 0;
        
        for (int windowEnd = 0; windowEnd < fruits.length; windowEnd++) {
            
            // Record last seen position of this fruit type
            lastSeen.put(fruits[windowEnd], windowEnd);
            
            // If we have more than 2 types
            if (lastSeen.size() > 2) {
                // Find the type with the SMALLEST last-seen index
                // That's the one we need to completely remove
                int minLastSeen = Integer.MAX_VALUE;
                int minType = -1;
                
                for (Map.Entry<Integer, Integer> entry : lastSeen.entrySet()) {
                    if (entry.getValue() < minLastSeen) {
                        minLastSeen = entry.getValue();
                        minType = entry.getKey();
                    }
                }
                
                // Jump windowStart past ALL occurrences of that type
                windowStart = minLastSeen + 1;
                lastSeen.remove(minType);
            }
            
            maxFruits = Math.max(maxFruits, windowEnd - windowStart + 1);
        }
        
        return maxFruits;
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE: O(n²) — check all subarrays
    // ─────────────────────────────────────────────
    public static int totalFruitBruteForce(int[] fruits) {
        
        if (fruits == null || fruits.length == 0) return 0;
        
        int maxFruits = 0;
        
        for (int i = 0; i < fruits.length; i++) {
            HashMap<Integer, Integer> seen = new HashMap<>();
            for (int j = i; j < fruits.length; j++) {
                seen.put(fruits[j], seen.getOrDefault(fruits[j], 0) + 1);
                if (seen.size() <= 2) {
                    maxFruits = Math.max(maxFruits, j - i + 1);
                } else {
                    break; // more than 2 types → no point extending
                }
            }
        }
        
        return maxFruits;
    }
    
    // ─────────────────────────────────────────────
    // RETURN ACTUAL SUBARRAY (not just length)
    // ─────────────────────────────────────────────
    public static int[] totalFruitSubarray(int[] fruits) {
        
        if (fruits == null || fruits.length == 0) return new int[0];
        
        HashMap<Integer, Integer> fruitFrequency = new HashMap<>();
        int windowStart = 0;
        int maxLength = 0;
        int maxStart = 0;
        
        for (int windowEnd = 0; windowEnd < fruits.length; windowEnd++) {
            
            int rightFruit = fruits[windowEnd];
            fruitFrequency.put(rightFruit,
                fruitFrequency.getOrDefault(rightFruit, 0) + 1);
            
            while (fruitFrequency.size() > 2) {
                int leftFruit = fruits[windowStart];
                fruitFrequency.put(leftFruit, fruitFrequency.get(leftFruit) - 1);
                if (fruitFrequency.get(leftFruit) == 0) {
                    fruitFrequency.remove(leftFruit);
                }
                windowStart++;
            }
            
            int currentLength = windowEnd - windowStart + 1;
            if (currentLength > maxLength) {
                maxLength = currentLength;
                maxStart = windowStart;
            }
        }
        
        // Extract the subarray
        int[] result = new int[maxLength];
        System.arraycopy(fruits, maxStart, result, 0, maxLength);
        return result;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Step-by-step visualization
    // ─────────────────────────────────────────────
    public static void traceExecution(int[] fruits) {
        
        System.out.print("  Fruits: [");
        for (int i = 0; i < fruits.length; i++) {
            System.out.print(fruits[i]);
            if (i < fruits.length - 1) System.out.print(", ");
        }
        System.out.println("]  (K=2 baskets)");
        System.out.println("  ─────────────────────────────────────────");
        
        HashMap<Integer, Integer> fruitFrequency = new HashMap<>();
        int windowStart = 0;
        int maxFruits = 0;
        
        for (int windowEnd = 0; windowEnd < fruits.length; windowEnd++) {
            
            int rightFruit = fruits[windowEnd];
            fruitFrequency.put(rightFruit,
                fruitFrequency.getOrDefault(rightFruit, 0) + 1);
            
            // Build window string
            StringBuilder windowStr = new StringBuilder("[");
            for (int w = windowStart; w <= windowEnd; w++) {
                windowStr.append(fruits[w]);
                if (w < windowEnd) windowStr.append(", ");
            }
            windowStr.append("]");
            
            System.out.println("  EXPAND → end=" + windowEnd 
                + " fruit=" + rightFruit
                + " window=" + windowStr
                + " map=" + fruitFrequency
                + " types=" + fruitFrequency.size());
            
            while (fruitFrequency.size() > 2) {
                int leftFruit = fruits[windowStart];
                fruitFrequency.put(leftFruit, fruitFrequency.get(leftFruit) - 1);
                if (fruitFrequency.get(leftFruit) == 0) {
                    fruitFrequency.remove(leftFruit);
                }
                windowStart++;
                
                // Rebuild window string after shrink
                windowStr = new StringBuilder("[");
                for (int w = windowStart; w <= windowEnd; w++) {
                    windowStr.append(fruits[w]);
                    if (w < windowEnd) windowStr.append(", ");
                }
                windowStr.append("]");
                
                System.out.println("  SHRINK → removed type " + leftFruit
                    + " start=" + windowStart
                    + " window=" + windowStr
                    + " map=" + fruitFrequency
                    + " types=" + fruitFrequency.size());
            }
            
            int currentLength = windowEnd - windowStart + 1;
            maxFruits = Math.max(maxFruits, currentLength);
            System.out.println("  → valid size=" + currentLength 
                + " maxFruits=" + maxFruits);
            System.out.println();
        }
        
        System.out.println("  ★ RESULT: " + maxFruits);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // DISGUISE DEMONSTRATOR: Show multiple problems are the same
    // ─────────────────────────────────────────────
    public static void demonstrateDisguises() {
        
        System.out.println("═══ DISGUISE DEMONSTRATION ═══");
        System.out.println();
        System.out.println("  All these are the SAME algorithm (at most K=2 distinct):");
        System.out.println();
        
        // Problem 1: Fruits into Baskets
        int[] fruits = {1, 2, 1, 2, 3};
        System.out.println("  DISGUISE 1: 'Fruits into Baskets'");
        System.out.println("  Input: fruits = [1, 2, 1, 2, 3]");
        System.out.println("  → Max fruits with 2 baskets: " + totalFruit(fruits));
        System.out.println();
        
        // Problem 2: Longest subarray with at most 2 distinct
        // (same array, same answer, different name)
        System.out.println("  DISGUISE 2: 'Longest subarray with ≤2 distinct'");
        System.out.println("  Input: arr = [1, 2, 1, 2, 3]");
        System.out.println("  → Longest subarray: " + totalFruitGeneralized(fruits, 2));
        System.out.println();
        
        // Problem 3: Maximum candies with 2 bags
        // (same array, same answer, different story)
        System.out.println("  DISGUISE 3: 'Max candies with 2 bags'");
        System.out.println("  Input: candies = [1, 2, 1, 2, 3]");
        System.out.println("  → Max candies: " + totalFruit(fruits));
        System.out.println();
        
        System.out.println("  → ALL THREE return 4 → same algorithm → different stories");
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 13: Fruits into Baskets                   ║");
        System.out.println("║  Pattern: Sliding Window → Disguised K=2 Distinct  ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ── Disguise demonstration ──
        demonstrateDisguises();
        
        // ── TEST 1: Basic trace ──
        System.out.println("═══ TEST 1: Basic Example (Traced) ═══");
        traceExecution(new int[] {1, 2, 1});
        
        // ── TEST 2: Third type forces shrink ──
        System.out.println("═══ TEST 2: Third Type Forces Shrink (Traced) ═══");
        traceExecution(new int[] {0, 1, 2, 2});
        
        // ── TEST 3: Longer example ──
        System.out.println("═══ TEST 3: Longer Example (Traced) ═══");
        traceExecution(new int[] {1, 2, 3, 2, 2});
        
        // ── TEST 4: Complex with many shrinks ──
        System.out.println("═══ TEST 4: Complex Example (Traced) ═══");
        traceExecution(new int[] {3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4});
        
        // ── TEST 5: Edge cases ──
        System.out.println("═══ TEST 5: Edge Cases ═══");
        System.out.println("  Single element [5]: " 
            + totalFruit(new int[] {5}));
        System.out.println("  Two same [3,3]: " 
            + totalFruit(new int[] {3, 3}));
        System.out.println("  Two different [1,2]: " 
            + totalFruit(new int[] {1, 2}));
        System.out.println("  All same [7,7,7,7,7]: " 
            + totalFruit(new int[] {7, 7, 7, 7, 7}));
        System.out.println("  Alternating [1,2,1,2,1,2]: " 
            + totalFruit(new int[] {1, 2, 1, 2, 1, 2}));
        System.out.println("  All different [1,2,3,4,5]: " 
            + totalFruit(new int[] {1, 2, 3, 4, 5}));
        System.out.println("  Large values [100000,200000,100000]: " 
            + totalFruit(new int[] {100000, 200000, 100000}));
        System.out.println();
        
        // ── TEST 6: Return actual subarray ──
        System.out.println("═══ TEST 6: Return Actual Subarray ═══");
        int[][] subarrayTests = {
            {1, 2, 1},
            {0, 1, 2, 2},
            {1, 2, 3, 2, 2},
            {3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4}
        };
        for (int[] test : subarrayTests) {
            int[] sub = totalFruitSubarray(test);
            System.out.print("  Input: [");
            for (int i = 0; i < test.length; i++) {
                System.out.print(test[i]);
                if (i < test.length - 1) System.out.print(",");
            }
            System.out.print("] → Subarray: [");
            for (int i = 0; i < sub.length; i++) {
                System.out.print(sub[i]);
                if (i < sub.length - 1) System.out.print(",");
            }
            System.out.println("] (length=" + sub.length + ")");
        }
        System.out.println();
        
        // ── TEST 7: Generalized K ──
        System.out.println("═══ TEST 7: Generalized K Values ═══");
        int[] genArr = {1, 2, 3, 4, 1, 2, 3, 1, 2, 1};
        System.out.print("  Array: [");
        for (int i = 0; i < genArr.length; i++) {
            System.out.print(genArr[i]);
            if (i < genArr.length - 1) System.out.print(",");
        }
        System.out.println("]");
        for (int k = 1; k <= 5; k++) {
            System.out.println("  K=" + k + " → max length: " 
                + totalFruitGeneralized(genArr, k));
        }
        System.out.println();
        
        // ── TEST 8: Correctness verification ──
        System.out.println("═══ TEST 8: Correctness Verification ═══");
        int[][] verifyTests = {
            {1, 2, 1},
            {0, 1, 2, 2},
            {1, 2, 3, 2, 2},
            {3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4},
            {1, 1, 1, 1},
            {1, 2, 3, 4, 5},
            {1, 2, 1, 2, 1, 2, 1},
            {5, 5, 1, 5, 1, 5, 5}
        };
        
        boolean allPassed = true;
        for (int[] test : verifyTests) {
            int optimal = totalFruit(test);
            int brute = totalFruitBruteForce(test);
            int optimized = totalFruitOptimizedK2(test);
            boolean pass = (optimal == brute) && (optimal == optimized);
            
            System.out.print("  [");
            for (int i = 0; i < test.length; i++) {
                System.out.print(test[i]);
                if (i < test.length - 1) System.out.print(",");
            }
            System.out.println("] → SW=" + optimal 
                + " Brute=" + brute 
                + " OptK2=" + optimized
                + " " + (pass ? "✓" : "✗"));
            if (!pass) allPassed = false;
        }
        System.out.println("  All passed: " + allPassed);
        System.out.println();
        
        // ── TEST 9: Performance benchmark ──
        System.out.println("═══ TEST 9: Performance Benchmark ═══");
        int size = 1_000_000;
        int[] largeArr = new int[size];
        java.util.Random rand = new java.util.Random(42);
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(100); // 100 distinct fruit types
        }
        
        long start = System.nanoTime();
        int swResult = totalFruit(largeArr);
        long swTime = System.nanoTime() - start;
        
        start = System.nanoTime();
        int optResult = totalFruitOptimizedK2(largeArr);
        long optTime = System.nanoTime() - start;
        
        start = System.nanoTime();
        int genResult = totalFruitGeneralized(largeArr, 2);
        long genTime = System.nanoTime() - start;
        
        System.out.println("  Array size: " + size + " (100 fruit types)");
        System.out.println("  HashMap (K=2):     result=" + swResult 
            + " time=" + (swTime / 1_000_000) + "ms");
        System.out.println("  Optimized K=2:     result=" + optResult 
            + " time=" + (optTime / 1_000_000) + "ms");
        System.out.println("  Generalized (K=2): result=" + genResult 
            + " time=" + (genTime / 1_000_000) + "ms");
        System.out.println("  All match: " 
            + (swResult == optResult && swResult == genResult));
        System.out.println();
    }
}