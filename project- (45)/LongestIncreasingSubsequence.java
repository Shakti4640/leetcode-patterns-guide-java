import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LongestIncreasingSubsequence {

    // ─────────────────────────────────────────────
    // SOLUTION 1: Classic DP — O(n²)
    // ─────────────────────────────────────────────
    public static int lisDP(int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // every element is LIS of length 1

        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 2: Binary Search on Tails — O(n log n)
    // ─────────────────────────────────────────────
    public static int lisBinarySearch(int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] tails = new int[n]; // tails array (won't exceed n)
        int size = 0;             // current length of tails

        for (int num : nums) {

            // Binary search for first element >= num in tails[0..size-1]
            int lo = 0, hi = size;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] < num) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }

            // lo = insertion point
            tails[lo] = num;

            // If lo == size → appended (new longest) → increase size
            if (lo == size) {
                size++;
            }
        }

        return size;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 3: O(n²) DP with LIS Recovery
    // ─────────────────────────────────────────────
    public static List<Integer> lisWithRecovery(int[] nums) {

        if (nums == null || nums.length == 0) return new ArrayList<>();

        int n = nums.length;
        int[] dp = new int[n];
        int[] parent = new int[n]; // track predecessors
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);

        int maxLength = 1;
        int maxIndex = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j; // record predecessor
                }
            }

            if (dp[i] > maxLength) {
                maxLength = dp[i];
                maxIndex = i;
            }
        }

        // Reconstruct LIS by following parent pointers
        List<Integer> lis = new ArrayList<>();
        int idx = maxIndex;

        while (idx != -1) {
            lis.add(0, nums[idx]); // prepend
            idx = parent[idx];
        }

        return lis;
    }

    // ─────────────────────────────────────────────
    // SOLUTION 4: O(n log n) with LIS Recovery
    // ─────────────────────────────────────────────
    public static List<Integer> lisBinarySearchWithRecovery(int[] nums) {

        if (nums == null || nums.length == 0) return new ArrayList<>();

        int n = nums.length;
        int[] tails = new int[n];
        int[] tailIndices = new int[n]; // original indices stored in tails
        int[] predecessors = new int[n]; // predecessor for each element
        Arrays.fill(predecessors, -1);
        int size = 0;

        for (int i = 0; i < n; i++) {
            int lo = 0, hi = size;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] < nums[i]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }

            tails[lo] = nums[i];
            tailIndices[lo] = i;

            // Predecessor: whoever is at position lo-1 in tails
            if (lo > 0) {
                predecessors[i] = tailIndices[lo - 1];
            }

            if (lo == size) {
                size++;
            }
        }

        // Reconstruct LIS
        List<Integer> lis = new ArrayList<>();
        int idx = tailIndices[size - 1]; // start from last tails position

        while (idx != -1) {
            lis.add(0, nums[idx]);
            idx = predecessors[idx];
        }

        return lis;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Longest Non-Decreasing Subsequence
    // (allows equal consecutive elements)
    // ─────────────────────────────────────────────
    public static int lnds(int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] tails = new int[n];
        int size = 0;

        for (int num : nums) {
            int lo = 0, hi = size;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] <= num) {  // <= instead of < → allows equal
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }

            tails[lo] = num;
            if (lo == size) size++;
        }

        return size;
    }

    // ─────────────────────────────────────────────
    // VARIANT: Longest Decreasing Subsequence
    // (reverse the array → LIS)
    // ─────────────────────────────────────────────
    public static int lds(int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        // Reverse the array
        int n = nums.length;
        int[] reversed = new int[n];
        for (int i = 0; i < n; i++) {
            reversed[i] = nums[n - 1 - i];
        }

        return lisBinarySearch(reversed);
    }

    // ─────────────────────────────────────────────
    // VARIANT: Minimum Deletions to Make Sorted
    // = n - LIS length
    // ─────────────────────────────────────────────
    public static int minDeletionsToSort(int[] nums) {
        return nums.length - lisBinarySearch(nums);
    }

    // ─────────────────────────────────────────────
    // VARIANT: Number of LIS (count all longest subsequences)
    // O(n²) approach
    // ─────────────────────────────────────────────
    public static int countLIS(int[] nums) {

        if (nums == null || nums.length == 0) return 0;

        int n = nums.length;
        int[] dp = new int[n];     // length of LIS ending at i
        int[] count = new int[n];  // count of LIS of that length ending at i
        Arrays.fill(dp, 1);
        Arrays.fill(count, 1);

        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        count[i] = count[j]; // new longest → inherit count
                    } else if (dp[j] + 1 == dp[i]) {
                        count[i] += count[j]; // same length → add counts
                    }
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        // Sum counts of all positions achieving maxLength
        int totalCount = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] == maxLength) {
                totalCount += count[i];
            }
        }

        return totalCount;
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch O(n²) DP build
    // ─────────────────────────────────────────────
    public static void lisDPWithTrace(int[] nums) {

        int n = nums.length;
        int[] dp = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("═══════════════════════════════════════════");
        System.out.println("O(n²) DP Trace:");

        for (int i = 0; i < n; i++) {
            String bestFrom = "none";

            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                    bestFrom = "j=" + j + "(val=" + nums[j] + ",dp=" + dp[j] + ")";
                }
            }

            System.out.println("  i=" + i + " val=" + nums[i]
                + " → dp[" + i + "]=" + dp[i]
                + " (best predecessor: " + bestFrom + ")");
        }

        int maxLen = 0, maxIdx = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        System.out.println("dp = " + Arrays.toString(dp));
        System.out.println("LIS length = " + maxLen);

        // Recover
        List<Integer> lis = new ArrayList<>();
        int idx = maxIdx;
        while (idx != -1) {
            lis.add(0, nums[idx]);
            idx = parent[idx];
        }
        System.out.println("Actual LIS: " + lis);
    }

    // ─────────────────────────────────────────────
    // TRACE — Watch tails array evolve
    // ─────────────────────────────────────────────
    public static void lisBinarySearchWithTrace(int[] nums) {

        int n = nums.length;
        int[] tails = new int[n];
        int size = 0;

        System.out.println("Array: " + Arrays.toString(nums));
        System.out.println("═══════════════════════════════════════════");
        System.out.println("O(n log n) Binary Search Trace:");

        for (int num : nums) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails[mid] < num) lo = mid + 1;
                else hi = mid;
            }

            int oldVal = (lo < size) ? tails[lo] : -1;
            tails[lo] = num;

            if (lo == size) {
                size++;
                System.out.println("  Process " + num
                    + " → APPEND (new longest) → tails = "
                    + tailsToString(tails, size));
            } else {
                System.out.println("  Process " + num
                    + " → REPLACE tails[" + lo + "]=" + oldVal
                    + " with " + num + " → tails = "
                    + tailsToString(tails, size));
            }
        }

        System.out.println("LIS length = " + size);
    }

    private static String tailsToString(int[] tails, int size) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(tails[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 45: Longest Increasing Subsequence               ║");
        System.out.println("║  Pattern: DP + Binary Search Hybrid → Patience Sorting    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: O(n²) DP Trace ──
        System.out.println("═══ TEST 1: O(n²) DP Trace ═══");
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        lisDPWithTrace(nums1);
        System.out.println();

        // ── TEST 2: O(n log n) Binary Search Trace ──
        System.out.println("═══ TEST 2: O(n log n) Binary Search Trace ═══");
        lisBinarySearchWithTrace(nums1);
        System.out.println();

        // ── TEST 3: LIS Recovery (Both Approaches) ──
        System.out.println("═══ TEST 3: LIS Recovery ═══");
        int[] nums3 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("Array: " + Arrays.toString(nums3));
        System.out.println("O(n²) recovered LIS:      " + lisWithRecovery(nums3));
        System.out.println("O(n log n) recovered LIS:  " + lisBinarySearchWithRecovery(nums3));
        System.out.println();

        // ── TEST 4: All Approaches Agree ──
        System.out.println("═══ TEST 4: Verify All Approaches Agree ═══");
        int[][] testCases = {
            {10, 9, 2, 5, 3, 7, 101, 18},
            {0, 1, 0, 3, 2, 3},
            {7, 7, 7, 7, 7},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5},
            {1},
            {1, 3, 2},
            {-5, -3, -8, -1, -7}
        };

        System.out.println("┌────────────────────────────────────┬──────┬──────┐");
        System.out.println("│ Array                              │ O(n²)│O(nlogn)│");
        System.out.println("├────────────────────────────────────┼──────┼──────┤");

        for (int[] tc : testCases) {
            int dpResult = lisDP(tc);
            int bsResult = lisBinarySearch(tc);
            String arr = Arrays.toString(tc);
            if (arr.length() > 36) arr = arr.substring(0, 33) + "...";

            System.out.printf("│ %-34s │ %4d │ %4d │%n", arr, dpResult, bsResult);
        }
        System.out.println("└────────────────────────────────────┴──────┴──────┘");
        System.out.println();

        // ── TEST 5: Strictly Increasing vs Non-Decreasing ──
        System.out.println("═══ TEST 5: Strictly Increasing vs Non-Decreasing ═══");
        int[] nums5 = {1, 3, 3, 5, 5, 7};
        System.out.println("Array: " + Arrays.toString(nums5));
        System.out.println("Strictly increasing (LIS):  " + lisBinarySearch(nums5));
        System.out.println("Non-decreasing (LNDS):      " + lnds(nums5));
        System.out.println();

        // ── TEST 6: Longest Decreasing Subsequence ──
        System.out.println("═══ TEST 6: Longest Decreasing Subsequence ═══");
        int[] nums6 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("Array: " + Arrays.toString(nums6));
        System.out.println("LIS: " + lisBinarySearch(nums6));
        System.out.println("LDS: " + lds(nums6));
        System.out.println();

        // ── TEST 7: Minimum Deletions to Make Sorted ──
        System.out.println("═══ TEST 7: Minimum Deletions to Sort ═══");
        int[] nums7 = {5, 1, 3, 2, 4};
        System.out.println("Array: " + Arrays.toString(nums7));
        System.out.println("LIS length: " + lisBinarySearch(nums7));
        System.out.println("Min deletions: " + minDeletionsToSort(nums7));
        System.out.println("(Remove " + minDeletionsToSort(nums7)
            + " elements → remaining " + lisBinarySearch(nums7)
            + " are sorted)");
        System.out.println();

        // ── TEST 8: Count of LIS ──
        System.out.println("═══ TEST 8: Count of Longest Increasing Subsequences ═══");
        int[] nums8a = {1, 3, 5, 4, 7};
        int[] nums8b = {2, 2, 2, 2, 2};
        int[] nums8c = {1, 2, 4, 3, 5, 4, 7, 2};
        System.out.println(Arrays.toString(nums8a) + " → LIS=" + lisDP(nums8a)
            + ", count=" + countLIS(nums8a));
        System.out.println(Arrays.toString(nums8b) + " → LIS=" + lisDP(nums8b)
            + ", count=" + countLIS(nums8b));
        System.out.println(Arrays.toString(nums8c) + " → LIS=" + lisDP(nums8c)
            + ", count=" + countLIS(nums8c));
        System.out.println();

        // ── TEST 9: Performance Comparison ──
        System.out.println("═══ TEST 9: Performance Comparison ═══");
        int[] sizes = {1000, 5000, 10000, 50000, 100000};
        java.util.Random rand = new java.util.Random(42);

        System.out.println("┌─────────┬───────────────┬─────────────────┬─────────┐");
        System.out.println("│   n     │ O(n²) time    │ O(n log n) time │ LIS len │");
        System.out.println("├─────────┼───────────────┼─────────────────┼─────────┤");

        for (int size : sizes) {
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = rand.nextInt(size * 10);
            }

            // O(n²) — only for smaller sizes
            long dpTime = -1;
            int dpResult = -1;
            if (size <= 10000) {
                long start = System.nanoTime();
                dpResult = lisDP(arr);
                dpTime = (System.nanoTime() - start) / 1_000_000;
            }

            // O(n log n)
            long start = System.nanoTime();
            int bsResult = lisBinarySearch(arr);
            long bsTime = (System.nanoTime() - start) / 1_000_000;

            String dpStr = dpTime >= 0 ? dpTime + " ms" : "SKIPPED";
            if (dpResult >= 0 && dpResult != bsResult) dpStr += " MISMATCH!";

            System.out.printf("│ %7d │ %13s │ %15s │ %7d │%n",
                size, dpStr, bsTime + " ms", bsResult);
        }
        System.out.println("└─────────┴───────────────┴─────────────────┴─────────┘");
        System.out.println();

        // ── TEST 10: Edge Cases ──
        System.out.println("═══ TEST 10: Edge Cases ═══");
        System.out.println("Empty: " + lisBinarySearch(new int[]{}));
        System.out.println("Single: " + lisBinarySearch(new int[]{42}));
        System.out.println("Two increasing: " + lisBinarySearch(new int[]{1, 2}));
        System.out.println("Two decreasing: " + lisBinarySearch(new int[]{2, 1}));
        System.out.println("Two equal: " + lisBinarySearch(new int[]{3, 3}));
        System.out.println("Already sorted: " + lisBinarySearch(new int[]{1, 2, 3, 4, 5}));
        System.out.println("Reverse sorted: " + lisBinarySearch(new int[]{5, 4, 3, 2, 1}));

        int[] zigzag = {1, 10, 2, 9, 3, 8, 4, 7, 5, 6};
        System.out.println("Zigzag " + Arrays.toString(zigzag) + ": "
            + lisBinarySearch(zigzag));
    }
}