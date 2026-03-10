import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FindTwoSingleNumbers {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: XOR + Partition by Differentiating Bit
    // ─────────────────────────────────────────────
    public static int[] singleNumbers(int[] nums) {

        // STEP 1: XOR all numbers → singleA ^ singleB
        int xorAll = 0;
        for (int num : nums) {
            xorAll ^= num;
        }
        // xorAll is non-zero because singleA ≠ singleB

        // STEP 2: Find rightmost set bit (where singles differ)
        int diffBit = xorAll & (-xorAll);
        // diffBit is a power of 2 with exactly one bit set
        // This bit position is where singleA and singleB have different values

        // STEP 3: Partition all numbers and XOR each group
        int num1 = 0; // XOR accumulator for group where diffBit is 0
        int num2 = 0; // XOR accumulator for group where diffBit is 1

        for (int num : nums) {
            if ((num & diffBit) == 0) {
                num1 ^= num; // Group 0: this bit is 0
            } else {
                num2 ^= num; // Group 1: this bit is 1
            }
        }

        // num1 and num2 are the two single numbers
        return new int[]{num1, num2};
    }

    // ─────────────────────────────────────────────
    // ALTERNATIVE: Single XOR pass (combine Steps 1 and 3)
    //   → Actually we MUST do two passes because Step 2 needs xorAll first
    //   → But we can combine Step 1 with preprocessing
    // ─────────────────────────────────────────────

    // ─────────────────────────────────────────────
    // BRUTE FORCE: HashMap (for verification)
    // ─────────────────────────────────────────────
    public static int[] singleNumbersHashMap(int[] nums) {

        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        int[] result = new int[2];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            if (entry.getValue() == 1) {
                result[idx++] = entry.getKey();
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // TRACE: Visualize the XOR + Partition process
    // ─────────────────────────────────────────────
    public static void singleNumbersWithTrace(int[] nums) {

        System.out.print("Array: [");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]);
            if (i < nums.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("─────────────────────────────────────────────────");

        // Find max value for binary width
        int maxVal = 0;
        for (int num : nums) maxVal = Math.max(maxVal, Math.abs(num));
        int bits = Math.max(4, Integer.SIZE - Integer.numberOfLeadingZeros(maxVal));

        // STEP 1: XOR all
        System.out.println("STEP 1: XOR all numbers");
        int xorAll = 0;
        for (int num : nums) {
            System.out.println("  xorAll = " + toBinary(xorAll, bits)
                    + " ^ " + toBinary(num, bits)
                    + " (" + xorAll + " ^ " + num + ")"
                    + " = " + toBinary(xorAll ^ num, bits)
                    + " (" + (xorAll ^ num) + ")");
            xorAll ^= num;
        }
        System.out.println("  Result: xorAll = " + xorAll
                + " = " + toBinary(xorAll, bits));
        System.out.println("  This equals singleA ^ singleB");
        System.out.println();

        // STEP 2: Rightmost set bit
        int diffBit = xorAll & (-xorAll);
        System.out.println("STEP 2: Find rightmost set bit");
        System.out.println("  xorAll  = " + toBinary(xorAll, bits));
        System.out.println("  -xorAll = " + toBinary(-xorAll, bits));
        System.out.println("  AND     = " + toBinary(diffBit, bits)
                + " (= " + diffBit + ")");
        System.out.println("  Partition bit: position "
                + Integer.numberOfTrailingZeros(diffBit)
                + " (value " + diffBit + ")");
        System.out.println();

        // STEP 3: Partition and XOR
        System.out.println("STEP 3: Partition by bit " + Integer.numberOfTrailingZeros(diffBit));
        int num1 = 0, num2 = 0;
        for (int num : nums) {
            String group;
            if ((num & diffBit) == 0) {
                num1 ^= num;
                group = "Group 0 (bit=0)";
            } else {
                num2 ^= num;
                group = "Group 1 (bit=1)";
            }
            System.out.println("  " + String.format("%3d", num)
                    + " = " + toBinary(num, bits)
                    + " → " + group);
        }
        System.out.println();

        System.out.println("  Group 0 XOR = " + num1);
        System.out.println("  Group 1 XOR = " + num2);
        System.out.println();
        System.out.println("ANSWER: The two single numbers are " + num1 + " and " + num2);
    }

    private static String toBinary(int num, int bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = bits - 1; i >= 0; i--) {
            sb.append((num >> i) & 1);
        }
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find Two Missing Numbers from 1 to N
    //   → Array should contain 1..N but two are missing
    //   → XOR actual array with XOR of 1..N
    //   → Reduces to "two single numbers" problem
    // ─────────────────────────────────────────────
    public static int[] findTwoMissing(int[] nums, int n) {

        // XOR all numbers in array with all numbers 1..N
        int xorAll = 0;

        // XOR the actual array
        for (int num : nums) {
            xorAll ^= num;
        }

        // XOR with 1..N (the complete expected set)
        for (int i = 1; i <= n; i++) {
            xorAll ^= i;
        }

        // Now xorAll = missingA ^ missingB
        // Same algorithm as singleNumbers
        int diffBit = xorAll & (-xorAll);
        int miss1 = 0, miss2 = 0;

        // Partition BOTH arrays by diffBit
        for (int num : nums) {
            if ((num & diffBit) == 0) miss1 ^= num;
            else miss2 ^= num;
        }
        for (int i = 1; i <= n; i++) {
            if ((i & diffBit) == 0) miss1 ^= i;
            else miss2 ^= i;
        }

        return new int[]{miss1, miss2};
    }

    // ─────────────────────────────────────────────
    // VARIANT: Find the One Single Number (Project 10 recap)
    //   → For comparison and to show the progression
    // ─────────────────────────────────────────────
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    // ─────────────────────────────────────────────
    // UTILITY: Verify result contains both singles
    // ─────────────────────────────────────────────
    private static boolean verifyResult(int[] result, int[] expected) {
        Arrays.sort(result);
        Arrays.sort(expected);
        return Arrays.equals(result, expected);
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 37: Find Two Single Numbers             ║");
        System.out.println("║  Pattern: XOR + Partition by Differentiating Bit ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example (Traced) ═══");
        singleNumbersWithTrace(new int[]{1, 2, 1, 3, 2, 5});
        System.out.println();

        // ── TEST 2: Another example with trace ──
        System.out.println("═══ TEST 2: Larger Numbers (Traced) ═══");
        singleNumbersWithTrace(new int[]{10, 20, 10, 30, 20, 40, 50, 50});
        System.out.println();

        // ── TEST 3: Minimum case (two elements) ──
        System.out.println("═══ TEST 3: Minimum Case — Two Elements ═══");
        singleNumbersWithTrace(new int[]{2, 3});
        System.out.println();

        // ── TEST 4: Singles at edges ──
        System.out.println("═══ TEST 4: Singles at Array Edges ═══");
        int[] test4 = {7, 1, 1, 2, 2, 3, 3, 9};
        int[] result4 = singleNumbers(test4);
        System.out.print("Array: ");
        System.out.println(Arrays.toString(test4));
        System.out.println("Singles: " + result4[0] + " and " + result4[1]);
        System.out.println("Correct: " + verifyResult(result4, new int[]{7, 9}));
        System.out.println();

        // ── TEST 5: Negative numbers ──
        System.out.println("═══ TEST 5: Negative Numbers ═══");
        int[] test5 = {-1, 2, -1, -3, 2, 5};
        int[] result5 = singleNumbers(test5);
        System.out.println("Array: " + Arrays.toString(test5));
        System.out.println("Singles: " + result5[0] + " and " + result5[1]);
        System.out.println("Correct: " + verifyResult(result5, new int[]{-3, 5}));
        System.out.println();

        // ── TEST 6: Powers of 2 ──
        System.out.println("═══ TEST 6: Powers of 2 ═══");
        int[] test6 = {1, 2, 4, 8, 1, 2};
        int[] result6 = singleNumbers(test6);
        System.out.println("Array: " + Arrays.toString(test6));
        System.out.println("Singles: " + result6[0] + " and " + result6[1]);
        System.out.println("Correct: " + verifyResult(result6, new int[]{4, 8}));
        System.out.println();

        // ── TEST 7: Project 10 comparison ──
        System.out.println("═══ TEST 7: Single Number (Project 10) vs Two Singles ═══");
        int[] oneUnique = {4, 1, 2, 1, 2};
        int[] twoUnique = {4, 1, 2, 1, 2, 7};
        System.out.println("One unique: " + Arrays.toString(oneUnique)
                + " → " + singleNumber(oneUnique));
        int[] twoResult = singleNumbers(twoUnique);
        System.out.println("Two unique: " + Arrays.toString(twoUnique)
                + " → " + twoResult[0] + " and " + twoResult[1]);
        System.out.println();

        // ── TEST 8: Two Missing Numbers variant ──
        System.out.println("═══ TEST 8: Two Missing Numbers from 1 to N ═══");
        int[] missing = {1, 2, 4, 6, 7};  // Missing 3 and 5 from 1..7
        int n = 7;
        int[] missingResult = findTwoMissing(missing, n);
        System.out.println("Array: " + Arrays.toString(missing) + " (should be 1.." + n + ")");
        System.out.println("Missing: " + missingResult[0] + " and " + missingResult[1]);
        System.out.println("Correct: " + verifyResult(missingResult, new int[]{3, 5}));
        System.out.println();

        // ── TEST 9: Correctness stress test ──
        System.out.println("═══ TEST 9: Correctness Stress Test ═══");
        java.util.Random rand = new java.util.Random(42);
        int testCount = 10000;
        int passed = 0;

        for (int t = 0; t < testCount; t++) {
            // Generate random array with exactly two singles
            int pairCount = rand.nextInt(20) + 1;
            int singleA = rand.nextInt(1000) + 1;
            int singleB;
            do {
                singleB = rand.nextInt(1000) + 1;
            } while (singleB == singleA);

            int[] arr = new int[2 * pairCount + 2];
            int idx = 0;
            for (int p = 0; p < pairCount; p++) {
                int val;
                do {
                    val = rand.nextInt(1000) + 1;
                } while (val == singleA || val == singleB);
                arr[idx++] = val;
                arr[idx++] = val;
            }
            arr[idx++] = singleA;
            arr[idx] = singleB;

            // Shuffle
            for (int i = arr.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }

            // Test
            int[] xorResult = singleNumbers(arr);
            int[] hashResult = singleNumbersHashMap(arr);

            Arrays.sort(xorResult);
            Arrays.sort(hashResult);

            if (Arrays.equals(xorResult, hashResult)) {
                passed++;
            } else {
                System.out.println("  FAILED: expected " + Arrays.toString(hashResult)
                        + " got " + Arrays.toString(xorResult));
            }
        }

        System.out.println("  " + passed + "/" + testCount + " tests passed"
                + (passed == testCount ? " ✓ ALL CORRECT" : " ✗ SOME FAILED"));
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        int perfSize = 10_000_000;
        int[] perfArr = new int[perfSize];
        int perfIdx = 0;

        // Generate pairs
        for (int i = 0; i < (perfSize - 2) / 2; i++) {
            perfArr[perfIdx++] = i + 3;
            perfArr[perfIdx++] = i + 3;
        }
        // Add two singles
        perfArr[perfIdx++] = 1;
        perfArr[perfIdx++] = 2;

        // Shuffle
        for (int i = perfArr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = perfArr[i];
            perfArr[i] = perfArr[j];
            perfArr[j] = temp;
        }

        System.out.println("  Array size: " + String.format("%,d", perfSize));

        // XOR approach
        long start = System.nanoTime();
        int[] xorRes = singleNumbers(perfArr);
        long xorTime = System.nanoTime() - start;

        // HashMap approach
        start = System.nanoTime();
        int[] hashRes = singleNumbersHashMap(perfArr);
        long hashTime = System.nanoTime() - start;

        Arrays.sort(xorRes);
        Arrays.sort(hashRes);

        System.out.println("  XOR approach:     "
                + String.format("%,d", xorTime / 1000) + " μs"
                + "  result=" + Arrays.toString(xorRes));
        System.out.println("  HashMap approach: "
                + String.format("%,d", hashTime / 1000) + " μs"
                + "  result=" + Arrays.toString(hashRes));
        System.out.println("  Speedup: "
                + String.format("%.1f", (double) hashTime / Math.max(xorTime, 1)) + "x");
        System.out.println("  Results match: "
                + (Arrays.equals(xorRes, hashRes) ? "✓" : "✗"));
        System.out.println();

        // ── TEST 11: Bit analysis ──
        System.out.println("═══ TEST 11: Rightmost Set Bit Examples ═══");
        int[] bitExamples = {6, 12, 10, 1, 8, 7, 15, 16};
        for (int val : bitExamples) {
            int rsb = val & (-val);
            System.out.println("  " + String.format("%3d", val)
                    + " = " + toBinary(val, 8)
                    + "  rightmost set bit = " + rsb
                    + " at position " + Integer.numberOfTrailingZeros(val));
        }
    }
}