public class FindSingleNumber {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: XOR Cancellation
    // ─────────────────────────────────────────────
    public static int singleNumber(int[] nums) {
        
        // XOR accumulator — starts at 0 (identity element)
        int result = 0;
        
        // XOR every element — pairs cancel to 0, single survives
        for (int num : nums) {
            result ^= num;
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // BRUTE FORCE: HashMap Frequency Count (for comparison)
    // Uses O(n) space — violates the constraint
    // ─────────────────────────────────────────────
    public static int singleNumberHashMap(int[] nums) {
        java.util.HashMap<Integer, Integer> freq = new java.util.HashMap<>();
        
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        for (java.util.Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        
        return -1; // should never reach here
    }
    
    // ─────────────────────────────────────────────
    // SORTING APPROACH (for comparison)
    // Uses O(n log n) time — violates the constraint
    // ─────────────────────────────────────────────
    public static int singleNumberSorting(int[] nums) {
        int[] sorted = nums.clone(); // don't modify original
        java.util.Arrays.sort(sorted);
        
        // Check pairs: [0,1], [2,3], [4,5], ...
        for (int i = 0; i < sorted.length - 1; i += 2) {
            if (sorted[i] != sorted[i + 1]) {
                // sorted[i] has no pair → it's the single number
                return sorted[i];
            }
        }
        
        // If no mismatch found, last element is the single one
        return sorted[sorted.length - 1];
    }
    
    // ─────────────────────────────────────────────
    // XOR VARIANT: Find missing number in [0..n]
    // Connection to Project 8 (Cyclic Sort alternative)
    // ─────────────────────────────────────────────
    public static int missingNumberXOR(int[] nums) {
        int n = nums.length;
        int result = n; // start with n (the index that has no element)
        
        for (int i = 0; i < n; i++) {
            result ^= i;        // XOR with index
            result ^= nums[i];  // XOR with value
            // matching index-value pairs cancel out
            // the unmatched index (missing number) survives
        }
        
        return result;
    }
    
    // ─────────────────────────────────────────────
    // TRACE: Bit-level visualization of XOR process
    // ─────────────────────────────────────────────
    public static void singleNumberWithTrace(int[] nums) {
        System.out.println("Array: ");
        System.out.print("  [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("─────────────────────────────────────────");
        
        int result = 0;
        System.out.println("  Initial result = 0");
        System.out.println("  Binary:          " + toBinaryPadded(0));
        System.out.println();
        
        for (int i = 0; i < nums.length; i++) {
            int prevResult = result;
            result ^= nums[i];
            
            System.out.println("  Step " + (i + 1) + ": result ^ " + nums[i]);
            System.out.println("    " + toBinaryPadded(prevResult) 
                + "  (" + prevResult + ")");
            System.out.println("  ^ " + toBinaryPadded(nums[i]) 
                + "  (" + nums[i] + ")");
            System.out.println("  = " + toBinaryPadded(result) 
                + "  (" + result + ")");
            System.out.println();
        }
        
        System.out.println("  ★ FINAL RESULT: " + result);
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // HELPER: Convert int to padded binary string
    // ─────────────────────────────────────────────
    private static String toBinaryPadded(int n) {
        // Show 8 bits for readability
        String binary = Integer.toBinaryString(n & 0xFF);
        while (binary.length() < 8) {
            binary = "0" + binary;
        }
        return binary;
    }
    
    // ─────────────────────────────────────────────
    // PROOF: Demonstrate XOR properties
    // ─────────────────────────────────────────────
    public static void demonstrateXORProperties() {
        System.out.println("═══ XOR PROPERTY DEMONSTRATIONS ═══");
        System.out.println();
        
        // Property 1: Self-cancellation
        System.out.println("PROPERTY 1: Self-Cancellation (a ^ a = 0)");
        int[] testValues = {5, 42, 127, 0, -3};
        for (int val : testValues) {
            System.out.println("  " + val + " ^ " + val + " = " + (val ^ val));
        }
        System.out.println();
        
        // Property 2: Identity with zero
        System.out.println("PROPERTY 2: Identity with Zero (a ^ 0 = a)");
        for (int val : testValues) {
            System.out.println("  " + val + " ^ 0 = " + (val ^ 0));
        }
        System.out.println();
        
        // Property 3: Commutativity
        System.out.println("PROPERTY 3: Commutativity (a ^ b = b ^ a)");
        System.out.println("  5 ^ 3 = " + (5 ^ 3));
        System.out.println("  3 ^ 5 = " + (3 ^ 5));
        System.out.println("  Equal? " + ((5 ^ 3) == (3 ^ 5)));
        System.out.println();
        
        // Property 4: Associativity
        System.out.println("PROPERTY 4: Associativity ((a^b)^c = a^(b^c))");
        System.out.println("  (5 ^ 3) ^ 7 = " + ((5 ^ 3) ^ 7));
        System.out.println("  5 ^ (3 ^ 7) = " + (5 ^ (3 ^ 7)));
        System.out.println("  Equal? " + (((5 ^ 3) ^ 7) == (5 ^ (3 ^ 7))));
        System.out.println();
        
        // Combined demonstration: pair cancellation
        System.out.println("COMBINED: Pair Cancellation in Action");
        System.out.println("  Array: [4, 1, 2, 1, 2]");
        System.out.println("  4 ^ 1 ^ 2 ^ 1 ^ 2");
        System.out.println("  = 4 ^ (1^1) ^ (2^2)    [regroup pairs]");
        System.out.println("  = 4 ^ 0 ^ 0             [self-cancel]");
        System.out.println("  = 4                      [identity]");
        System.out.println("  Computed: " + (4 ^ 1 ^ 2 ^ 1 ^ 2));
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // EDGE CASE TESTER
    // ─────────────────────────────────────────────
    public static void testEdgeCases() {
        System.out.println("═══ EDGE CASE TESTS ═══");
        System.out.println();
        
        // Edge 1: Single element array
        int[] single = {42};
        System.out.println("Single element [42]: " + singleNumber(single));
        
        // Edge 2: Three elements
        int[] three = {2, 2, 1};
        System.out.println("Three elements [2,2,1]: " + singleNumber(three));
        
        // Edge 3: Negative numbers
        int[] negatives = {-1, -1, -2, -3, -3};
        System.out.println("Negatives [-1,-1,-2,-3,-3]: " + singleNumber(negatives));
        
        // Edge 4: Mix of positive and negative
        int[] mixed = {-5, 3, -5, 3, 99};
        System.out.println("Mixed [-5,3,-5,3,99]: " + singleNumber(mixed));
        
        // Edge 5: Zero is the single number
        int[] withZero = {7, 0, 7};
        System.out.println("Zero is single [7,0,7]: " + singleNumber(withZero));
        
        // Edge 6: Large values
        int[] large = {Integer.MAX_VALUE, 100, Integer.MAX_VALUE};
        System.out.println("Large values [MAX,100,MAX]: " + singleNumber(large));
        
        // Edge 7: All same except one at the end
        int[] endSingle = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        System.out.println("Single at end [1,1,2,2,3,3,4,4,5]: " 
            + singleNumber(endSingle));
        
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // PERFORMANCE BENCHMARK
    // ─────────────────────────────────────────────
    public static void performanceBenchmark() {
        System.out.println("═══ PERFORMANCE BENCHMARK ═══");
        System.out.println();
        
        int size = 10_000_001; // odd size → one single element
        int[] arr = new int[size];
        
        // Fill with pairs: [0,0,1,1,2,2,...,4999999,4999999,5000000]
        int idx = 0;
        for (int i = 0; i < size / 2; i++) {
            arr[idx++] = i;
            arr[idx++] = i;
        }
        arr[idx] = size / 2; // the single number
        
        // Shuffle to make it realistic (pairs are not adjacent)
        java.util.Random rand = new java.util.Random(42);
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        
        int expected = size / 2;
        System.out.println("Array size: " + size + " elements");
        System.out.println("Expected single number: " + expected);
        System.out.println();
        
        // Benchmark XOR
        long start = System.nanoTime();
        int xorResult = singleNumber(arr);
        long xorTime = System.nanoTime() - start;
        System.out.println("XOR Approach:");
        System.out.println("  Result: " + xorResult);
        System.out.println("  Time:   " + (xorTime / 1_000_000) + " ms");
        System.out.println("  Space:  O(1) — 4 bytes");
        System.out.println();
        
        // Benchmark HashMap
        start = System.nanoTime();
        int hashResult = singleNumberHashMap(arr);
        long hashTime = System.nanoTime() - start;
        System.out.println("HashMap Approach:");
        System.out.println("  Result: " + hashResult);
        System.out.println("  Time:   " + (hashTime / 1_000_000) + " ms");
        System.out.println("  Space:  O(n) — ~" + (size * 32 / 1_000_000) + " MB");
        System.out.println();
        
        // Benchmark Sorting
        start = System.nanoTime();
        int sortResult = singleNumberSorting(arr);
        long sortTime = System.nanoTime() - start;
        System.out.println("Sorting Approach:");
        System.out.println("  Result: " + sortResult);
        System.out.println("  Time:   " + (sortTime / 1_000_000) + " ms");
        System.out.println("  Space:  O(n) — cloned array");
        System.out.println();
        
        System.out.println("SPEEDUP vs HashMap: " 
            + (hashTime / Math.max(xorTime, 1)) + "x");
        System.out.println("SPEEDUP vs Sorting: " 
            + (sortTime / Math.max(xorTime, 1)) + "x");
        System.out.println();
    }
    
    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 10: Find the Single Number           ║");
        System.out.println("║  Pattern: Bitwise XOR → Cancellation Property ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println();
        
        // ── Demonstrate XOR Properties ──
        demonstrateXORProperties();
        
        // ── TEST 1: Basic trace ──
        System.out.println("═══ TEST 1: Basic Trace ═══");
        singleNumberWithTrace(new int[] {4, 1, 2, 1, 2});
        
        // ── TEST 2: Larger example trace ──
        System.out.println("═══ TEST 2: Larger Example Trace ═══");
        singleNumberWithTrace(new int[] {7, 3, 5, 3, 5, 9, 7});
        
        // ── TEST 3: Negative numbers trace ──
        System.out.println("═══ TEST 3: Negative Numbers ═══");
        singleNumberWithTrace(new int[] {-1, -1, -2});
        
        // ── Edge Cases ──
        testEdgeCases();
        
        // ── Missing Number via XOR (Project 8 connection) ──
        System.out.println("═══ PROJECT 8 CONNECTION: Missing Number via XOR ═══");
        int[] missing1 = {3, 0, 1};
        System.out.println("Array [3,0,1] → missing: " + missingNumberXOR(missing1));
        int[] missing2 = {0, 1, 2, 4, 5};
        System.out.println("Array [0,1,2,4,5] → missing: " + missingNumberXOR(missing2));
        int[] missing3 = {1};
        System.out.println("Array [1] → missing: " + missingNumberXOR(missing3));
        System.out.println();
        
        // ── Performance ──
        performanceBenchmark();
    }
}