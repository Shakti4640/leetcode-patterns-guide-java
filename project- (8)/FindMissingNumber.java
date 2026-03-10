public class FindMissingNumber {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Cyclic Sort
    // ─────────────────────────────────────────────
    public static int missingNumber(int[] nums) {

        int n = nums.length;

        // Phase 1: CYCLIC SORT — place each number at its home index
        int i = 0;
        while (i < n) {

            int correctIndex = nums[i];  // where this value should live

            // If value is within range AND destination doesn't have correct value
            if (correctIndex < n && nums[correctIndex] != nums[i]) {
                // SWAP: send this value to its home
                int temp = nums[i];
                nums[i] = nums[correctIndex];
                nums[correctIndex] = temp;
                // DON'T increment i — check the swapped-in value
            } else {
                // Value is at home, OR value is N (no home), OR destination occupied
                i++;
            }
        }

        // Phase 2: SCAN — find the index where arr[i] != i
        for (i = 0; i < n; i++) {
            if (nums[i] != i) {
                return i;   // this index's rightful occupant is missing
            }
        }

        // All indices matched → N itself is missing
        return n;
    }

    // ─────────────────────────────────────────────
    // APPROACH 2: Math Formula (Sum difference)
    // ─────────────────────────────────────────────
    public static int missingNumberMath(int[] nums) {

        int n = nums.length;

        // Expected sum of 0 to N
        long expectedSum = (long) n * (n + 1) / 2;

        // Actual sum of array elements
        long actualSum = 0;
        for (int num : nums) {
            actualSum += num;
        }

        return (int) (expectedSum - actualSum);
    }

    // ─────────────────────────────────────────────
    // APPROACH 3: XOR (Preview of Project 10)
    // ─────────────────────────────────────────────
    public static int missingNumberXOR(int[] nums) {

        int n = nums.length;
        int xorResult = 0;

        // XOR all indices 0 to N
        for (int i = 0; i <= n; i++) {
            xorResult ^= i;
        }

        // XOR all array values
        for (int num : nums) {
            xorResult ^= num;
        }

        // Paired numbers cancel out → only the missing number remains
        return xorResult;
    }

    // ─────────────────────────────────────────────
    // APPROACH 4: HashSet (Brute Force)
    // ─────────────────────────────────────────────
    public static int missingNumberHashSet(int[] nums) {

        java.util.HashSet<Integer> set = new java.util.HashSet<>();
        for (int num : nums) {
            set.add(num);
        }

        for (int i = 0; i <= nums.length; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }

        return -1;  // should never reach here
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Find missing number in range [1, N]
    // (Array has N-1 elements from [1, N])
    // ─────────────────────────────────────────────
    public static int missingNumberOneToN(int[] nums) {

        int n = nums.length + 1;  // range is [1, N], array has N-1 elements
        int i = 0;

        while (i < nums.length) {
            // Value v belongs at index v-1 (shifted mapping)
            int correctIndex = nums[i] - 1;

            if (correctIndex >= 0 && correctIndex < nums.length
                && nums[correctIndex] != nums[i]) {
                int temp = nums[i];
                nums[i] = nums[correctIndex];
                nums[correctIndex] = temp;
            } else {
                i++;
            }
        }

        for (i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Find first missing POSITIVE integer
    // (Array can have negatives, zeros, and large numbers)
    // ─────────────────────────────────────────────
    public static int firstMissingPositive(int[] nums) {

        int n = nums.length;
        int i = 0;

        while (i < n) {
            // Value v belongs at index v-1 (value 1 → index 0, value 2 → index 1)
            int correctIndex = nums[i] - 1;

            // Only process values in range [1, N]
            if (correctIndex >= 0 && correctIndex < n
                && nums[correctIndex] != nums[i]) {
                int temp = nums[i];
                nums[i] = nums[correctIndex];
                nums[correctIndex] = temp;
            } else {
                i++;
            }
        }

        // Find first position where value != index+1
        for (i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n + 1;
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Find the corrupt pair
    // (One number missing, one number duplicated)
    // ─────────────────────────────────────────────
    public static int[] findCorruptPair(int[] nums) {

        int n = nums.length;
        int i = 0;

        // Cyclic sort: values [1, N] → index [0, N-1]
        while (i < n) {
            int correctIndex = nums[i] - 1;

            if (correctIndex >= 0 && correctIndex < n
                && nums[correctIndex] != nums[i]) {
                int temp = nums[i];
                nums[i] = nums[correctIndex];
                nums[correctIndex] = temp;
            } else {
                i++;
            }
        }

        // Find the mismatch: arr[i] != i+1
        for (i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                // nums[i] is the DUPLICATE (it's at wrong position)
                // i+1 is the MISSING number (this position is unoccupied)
                return new int[]{nums[i], i + 1};
            }
        }

        return new int[]{-1, -1};
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the cyclic sort happen
    // ─────────────────────────────────────────────
    public static void missingNumberWithTrace(int[] nums) {

        System.out.println("Input: " + arrayToString(nums)
            + "  N = " + nums.length);
        System.out.println("Range: [0, " + nums.length + "]");
        System.out.println("─────────────────────────────────────────────");

        int n = nums.length;
        int i = 0;
        int step = 1;

        System.out.println("Phase 1: CYCLIC SORT");

        while (i < n) {

            int correctIndex = nums[i];

            if (correctIndex < n && nums[correctIndex] != nums[i]) {

                System.out.println("  Step " + step + ": i=" + i
                    + " arr[" + i + "]=" + nums[i]
                    + " → belongs at index " + correctIndex
                    + " (currently " + nums[correctIndex] + ")"
                    + " → SWAP");

                int temp = nums[i];
                nums[i] = nums[correctIndex];
                nums[correctIndex] = temp;

                System.out.println("           Array: " + arrayToString(nums));
                step++;
            } else {
                String reason;
                if (correctIndex >= n) {
                    reason = "value " + correctIndex + " >= N → no home → skip";
                } else {
                    reason = "arr[" + correctIndex + "]=" + nums[correctIndex]
                        + " already correct → skip";
                }
                System.out.println("  Step " + step + ": i=" + i
                    + " arr[" + i + "]=" + nums[i]
                    + " → " + reason + " → i++");
                i++;
                step++;
            }
        }

        System.out.println();
        System.out.println("After sort: " + arrayToString(nums));
        System.out.println();

        System.out.println("Phase 2: SCAN for mismatch");
        for (i = 0; i < n; i++) {
            if (nums[i] != i) {
                System.out.println("  arr[" + i + "]=" + nums[i]
                    + " ≠ " + i + " → MISSING NUMBER = " + i);
                System.out.println();
                return;
            } else {
                System.out.println("  arr[" + i + "]=" + nums[i]
                    + " = " + i + " ✓");
            }
        }
        System.out.println("  All match → MISSING NUMBER = " + n);
        System.out.println();
    }

    // ─────────────────────────────────────────────
    // HELPER: Array to string
    // ─────────────────────────────────────────────
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all examples
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 8: Find the Missing Number (0 to N)        ║");
        System.out.println("║  Pattern: Cyclic Sort → Index-Value Mapping          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example ═══");
        missingNumberWithTrace(new int[]{3, 0, 1});

        // ── TEST 2: Missing at the end ──
        System.out.println("═══ TEST 2: Missing at the End ═══");
        missingNumberWithTrace(new int[]{0, 1});

        // ── TEST 3: Larger array ──
        System.out.println("═══ TEST 3: Larger Array ═══");
        missingNumberWithTrace(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1});

        // ── TEST 4: Missing 0 ──
        System.out.println("═══ TEST 4: Missing 0 ═══");
        missingNumberWithTrace(new int[]{1});

        // ── TEST 5: Missing N ──
        System.out.println("═══ TEST 5: Missing N (all 0 to N-1 present) ═══");
        missingNumberWithTrace(new int[]{0, 1, 2});

        // ── TEST 6: Single element ──
        System.out.println("═══ TEST 6: Single Element [0] → Missing 1 ═══");
        missingNumberWithTrace(new int[]{0});

        // ── TEST 7: Correctness — all approaches ──
        System.out.println("═══ TEST 7: Correctness — All Four Approaches ═══");
        int[][] testCases = {
            {3, 0, 1},
            {0, 1},
            {9, 6, 4, 2, 3, 5, 7, 0, 1},
            {1},
            {0},
            {0, 1, 2},
            {5, 3, 0, 1, 4},
            {2, 0, 4, 1, 5, 7, 3}
        };

        boolean allPassed = true;
        for (int[] test : testCases) {
            int r1 = missingNumber(test.clone());
            int r2 = missingNumberMath(test.clone());
            int r3 = missingNumberXOR(test.clone());
            int r4 = missingNumberHashSet(test.clone());

            boolean match = (r1 == r2) && (r2 == r3) && (r3 == r4);
            System.out.println("  " + arrayToString(test)
                + " → cyclic=" + r1
                + " math=" + r2
                + " xor=" + r3
                + " hash=" + r4
                + (match ? " ✓" : " ✗ MISMATCH"));
            if (!match) allPassed = false;
        }
        System.out.println("  Overall: "
            + (allPassed ? "ALL TESTS PASSED ✓" : "SOME FAILED ✗"));
        System.out.println();

        // ── TEST 8: First Missing Positive ──
        System.out.println("═══ TEST 8: Variant — First Missing Positive ═══");
        int[][] fmpTests = {
            {1, 2, 0},          // missing 3
            {3, 4, -1, 1},      // missing 2
            {7, 8, 9, 11, 12},  // missing 1
            {1, 2, 3, 4, 5},    // missing 6
            {2, 1},             // missing 3
            {1},                // missing 2
        };
        for (int[] test : fmpTests) {
            int result = firstMissingPositive(test.clone());
            System.out.println("  " + arrayToString(test) + " → " + result);
        }
        System.out.println();

        // ── TEST 9: Corrupt Pair ──
        System.out.println("═══ TEST 9: Variant — Corrupt Pair (duplicate + missing) ═══");
        int[][] corruptTests = {
            {3, 1, 2, 5, 2},    // duplicate=2, missing=4
            {1, 2, 3, 3},       // duplicate=3, missing=4
            {2, 2},             // duplicate=2, missing=1
            {1, 1},             // duplicate=1, missing=2
        };
        for (int[] test : corruptTests) {
            int[] result = findCorruptPair(test.clone());
            System.out.println("  " + arrayToString(test)
                + " → duplicate=" + result[0]
                + ", missing=" + result[1]);
        }
        System.out.println();

        // ── TEST 10: Performance comparison ──
        System.out.println("═══ TEST 10: Performance Comparison ═══");
        int size = 10000000;
        int[] largeArr = new int[size];
        // Fill with 0 to size, skip one random number
        int missingVal = size / 2;  // the number we'll skip
        int idx = 0;
        for (int v = 0; v <= size; v++) {
            if (v != missingVal) {
                largeArr[idx++] = v;
            }
        }
        // Shuffle to make it interesting
        java.util.Random rand = new java.util.Random(42);
        for (int j = size - 1; j > 0; j--) {
            int k = rand.nextInt(j + 1);
            int temp = largeArr[j];
            largeArr[j] = largeArr[k];
            largeArr[k] = temp;
        }

        // Cyclic Sort
        int[] copy1 = largeArr.clone();
        long startTime = System.nanoTime();
        int res1 = missingNumber(copy1);
        long cyclicTime = System.nanoTime() - startTime;

        // Math Formula
        int[] copy2 = largeArr.clone();
        startTime = System.nanoTime();
        int res2 = missingNumberMath(copy2);
        long mathTime = System.nanoTime() - startTime;

        // XOR
        int[] copy3 = largeArr.clone();
        startTime = System.nanoTime();
        int res3 = missingNumberXOR(copy3);
        long xorTime = System.nanoTime() - startTime;

        System.out.println("Array size: " + size
            + " | Missing: " + missingVal);
        System.out.println("Cyclic Sort: " + res1 + " → "
            + (cyclicTime / 1_000_000) + " ms");
        System.out.println("Math Formula: " + res2 + " → "
            + (mathTime / 1_000_000) + " ms");
        System.out.println("XOR:          " + res3 + " → "
            + (xorTime / 1_000_000) + " ms");
        System.out.println("All correct: "
            + (res1 == missingVal && res2 == missingVal && res3 == missingVal));
        System.out.println();
    }
}