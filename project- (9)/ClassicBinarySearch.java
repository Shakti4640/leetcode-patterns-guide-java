public class ClassicBinarySearch {

    // ─────────────────────────────────────────────
    // CORE SOLUTION: Standard Binary Search
    // ─────────────────────────────────────────────
    public static int binarySearch(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {

            // Safe mid calculation (no overflow)
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                // FOUND
                return mid;
            } else if (nums[mid] < target) {
                // Target is in the RIGHT half
                left = mid + 1;
            } else {
                // Target is in the LEFT half
                right = mid - 1;
            }
        }

        // Search space exhausted → target not found
        return -1;
    }

    // ─────────────────────────────────────────────
    // LINEAR SEARCH (for comparison)
    // ─────────────────────────────────────────────
    public static int linearSearch(int[] nums, int target) {

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) return i;
        }
        return -1;
    }

    // ─────────────────────────────────────────────
    // RECURSIVE VERSION (for understanding — prefer iterative)
    // ─────────────────────────────────────────────
    public static int binarySearchRecursive(int[] nums, int target) {
        return binarySearchHelper(nums, target, 0, nums.length - 1);
    }

    private static int binarySearchHelper(int[] nums, int target,
                                           int left, int right) {
        // Base case: search space empty
        if (left > right) return -1;

        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            return binarySearchHelper(nums, target, mid + 1, right);
        } else {
            return binarySearchHelper(nums, target, left, mid - 1);
        }
    }

    // ─────────────────────────────────────────────
    // VARIANT 1: Find FIRST occurrence (leftmost)
    // ─────────────────────────────────────────────
    public static int findFirstOccurrence(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                result = mid;       // record this as a candidate
                right = mid - 1;    // but keep searching LEFT for earlier occurrence
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 2: Find LAST occurrence (rightmost)
    // ─────────────────────────────────────────────
    public static int findLastOccurrence(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                result = mid;       // record this as a candidate
                left = mid + 1;     // but keep searching RIGHT for later occurrence
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 3: Find insertion position
    // (Where would target be inserted to keep sorted order?)
    // ─────────────────────────────────────────────
    public static int findInsertionPosition(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;         // exact match → insert here
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // left is now the insertion position
        // All elements before left are < target
        // All elements from left onward are > target
        return left;
    }

    // ─────────────────────────────────────────────
    // VARIANT 4: Find floor (largest element ≤ target)
    // ─────────────────────────────────────────────
    public static int findFloor(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] <= target) {
                result = mid;       // candidate for floor
                left = mid + 1;     // look for larger floor
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 5: Find ceiling (smallest element ≥ target)
    // ─────────────────────────────────────────────
    public static int findCeiling(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        int result = -1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            if (nums[mid] >= target) {
                result = mid;       // candidate for ceiling
                right = mid - 1;    // look for smaller ceiling
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // ─────────────────────────────────────────────
    // VARIANT 6: Count occurrences of target
    // (Using first + last occurrence)
    // ─────────────────────────────────────────────
    public static int countOccurrences(int[] nums, int target) {

        int first = findFirstOccurrence(nums, target);
        if (first == -1) return 0;

        int last = findLastOccurrence(nums, target);
        return last - first + 1;
    }

    // ─────────────────────────────────────────────
    // VARIANT 7: Find closest element to target
    // ─────────────────────────────────────────────
    public static int findClosest(int[] nums, int target) {

        if (nums.length == 0) return -1;

        int left = 0;
        int right = nums.length - 1;

        // Standard binary search until 2 elements remain
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            else if (nums[mid] < target) left = mid;
            else right = mid;
        }

        // Compare the two remaining candidates
        if (Math.abs(nums[left] - target) <= Math.abs(nums[right] - target)) {
            return left;
        }
        return right;
    }

    // ─────────────────────────────────────────────
    // TRACE FUNCTION — See the halving happen
    // ─────────────────────────────────────────────
    public static void binarySearchWithTrace(int[] nums, int target) {

        System.out.println("Array: " + arrayToString(nums)
            + "  Target: " + target);
        System.out.println("─────────────────────────────────────────────");

        int left = 0;
        int right = nums.length - 1;
        int step = 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;
            int searchSpaceSize = right - left + 1;

            System.out.println("Step " + step + ":");
            System.out.println("  Search space: [" + left + ".." + right
                + "] (" + searchSpaceSize + " elements)");
            System.out.println("  mid = " + mid + " → arr[" + mid + "] = " + nums[mid]);

            if (nums[mid] == target) {
                System.out.println("  → arr[" + mid + "] == " + target
                    + " → FOUND at index " + mid + " ✓");
                System.out.println();
                return;
            } else if (nums[mid] < target) {
                System.out.println("  → " + nums[mid] + " < " + target
                    + " → target in RIGHT half → left = " + (mid + 1));
                left = mid + 1;
            } else {
                System.out.println("  → " + nums[mid] + " > " + target
                    + " → target in LEFT half → right = " + (mid - 1));
                right = mid - 1;
            }

            step++;
        }

        System.out.println("Step " + step + ": left(" + left + ") > right("
            + right + ") → SEARCH SPACE EMPTY → NOT FOUND ✗");
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
        System.out.println("║  PROJECT 9: Classic Binary Search on Sorted Array    ║");
        System.out.println("║  Pattern: Modified Binary Search → Half Elimination  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic example with trace ──
        System.out.println("═══ TEST 1: Basic Example — Target Found ═══");
        binarySearchWithTrace(new int[]{-1, 0, 3, 5, 9, 12}, 9);

        // ── TEST 2: Target not found ──
        System.out.println("═══ TEST 2: Target Not Found ═══");
        binarySearchWithTrace(new int[]{-1, 0, 3, 5, 9, 12}, 2);

        // ── TEST 3: Larger array ──
        System.out.println("═══ TEST 3: Larger Array ═══");
        binarySearchWithTrace(
            new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19}, 13);

        // ── TEST 4: Single element found ──
        System.out.println("═══ TEST 4: Single Element — Found ═══");
        binarySearchWithTrace(new int[]{1}, 1);

        // ── TEST 5: Single element not found ──
        System.out.println("═══ TEST 5: Single Element — Not Found ═══");
        binarySearchWithTrace(new int[]{1}, 5);

        // ── TEST 6: Target at boundaries ──
        System.out.println("═══ TEST 6: Target at First Position ═══");
        binarySearchWithTrace(new int[]{1, 2, 3, 4, 5}, 1);

        System.out.println("═══ TEST 7: Target at Last Position ═══");
        binarySearchWithTrace(new int[]{1, 2, 3, 4, 5}, 5);

        // ── TEST 8: First and last occurrence ──
        System.out.println("═══ TEST 8: First and Last Occurrence (Duplicates) ═══");
        int[] arr8 = {1, 2, 2, 2, 2, 3, 4};
        int target8 = 2;
        int first = findFirstOccurrence(arr8, target8);
        int last = findLastOccurrence(arr8, target8);
        int count = countOccurrences(arr8, target8);
        System.out.println("Array: " + arrayToString(arr8) + "  Target: " + target8);
        System.out.println("First occurrence: index " + first);
        System.out.println("Last occurrence:  index " + last);
        System.out.println("Total occurrences: " + count);
        System.out.println();

        // ── TEST 9: Insertion position ──
        System.out.println("═══ TEST 9: Insertion Position ═══");
        int[] arr9 = {1, 3, 5, 7, 9};
        int[] targets9 = {0, 3, 4, 9, 10};
        System.out.println("Array: " + arrayToString(arr9));
        for (int t : targets9) {
            int pos = findInsertionPosition(arr9, t);
            System.out.println("  Target " + t + " → insert at index " + pos);
        }
        System.out.println();

        // ── TEST 10: Floor and ceiling ──
        System.out.println("═══ TEST 10: Floor and Ceiling ═══");
        int[] arr10 = {1, 3, 5, 7, 9, 11};
        int[] targets10 = {0, 4, 7, 12};
        System.out.println("Array: " + arrayToString(arr10));
        for (int t : targets10) {
            int floorIdx = findFloor(arr10, t);
            int ceilIdx = findCeiling(arr10, t);
            String floorStr = floorIdx == -1 ? "none" : String.valueOf(arr10[floorIdx]);
            String ceilStr = ceilIdx == -1 ? "none" : String.valueOf(arr10[ceilIdx]);
            System.out.println("  Target " + t
                + " → floor=" + floorStr
                + ", ceiling=" + ceilStr);
        }
        System.out.println();

        // ── TEST 11: Closest element ──
        System.out.println("═══ TEST 11: Closest Element ═══");
        int[] arr11 = {1, 4, 6, 10, 15};
        int[] targets11 = {3, 5, 8, 12, 16, 0};
        System.out.println("Array: " + arrayToString(arr11));
        for (int t : targets11) {
            int closestIdx = findClosest(arr11, t);
            System.out.println("  Target " + t
                + " → closest = " + arr11[closestIdx]
                + " (index " + closestIdx + ")");
        }
        System.out.println();

        // ── TEST 12: Correctness — binary vs linear ──
        System.out.println("═══ TEST 12: Correctness — Binary vs Linear vs Recursive ═══");
        int size = 1000;
        int[] sortedArr = new int[size];
        for (int i = 0; i < size; i++) sortedArr[i] = i * 3;  // [0, 3, 6, 9, ...]

        boolean allPassed = true;
        // Test existing elements
        for (int i = 0; i < size; i += 50) {
            int target = sortedArr[i];
            int r1 = binarySearch(sortedArr, target);
            int r2 = linearSearch(sortedArr, target);
            int r3 = binarySearchRecursive(sortedArr, target);
            if (r1 != r2 || r2 != r3) {
                System.out.println("  MISMATCH for target " + target);
                allPassed = false;
            }
        }
        // Test non-existing elements
        for (int t = 1; t < 100; t += 7) {
            int r1 = binarySearch(sortedArr, t);
            int r2 = linearSearch(sortedArr, t);
            int r3 = binarySearchRecursive(sortedArr, t);
            if (r1 != r2 || r2 != r3) {
                System.out.println("  MISMATCH for target " + t);
                allPassed = false;
            }
        }
        System.out.println("  Tested " + size + " elements (existing + non-existing)");
        System.out.println("  Overall: "
            + (allPassed ? "ALL TESTS PASSED ✓" : "SOME FAILED ✗"));
        System.out.println();

        // ── TEST 13: Performance comparison ──
        System.out.println("═══ TEST 13: Performance Comparison ═══");
        int perfSize = 100000000;  // 100 million
        int[] perfArr = new int[perfSize];
        for (int i = 0; i < perfSize; i++) perfArr[i] = i;

        int perfTarget = perfSize - 1;  // worst case for linear (last element)

        // Binary search
        long startTime = System.nanoTime();
        int binaryResult = binarySearch(perfArr, perfTarget);
        long binaryTime = System.nanoTime() - startTime;

        // Linear search
        startTime = System.nanoTime();
        int linearResult = linearSearch(perfArr, perfTarget);
        long linearTime = System.nanoTime() - startTime;

        System.out.println("Array size: " + perfSize + " (100 million)");
        System.out.println("Target: " + perfTarget + " (worst case — last element)");
        System.out.println("Binary Search: index " + binaryResult + " → "
            + (binaryTime / 1000) + " microseconds");
        System.out.println("Linear Search: index " + linearResult + " → "
            + (linearTime / 1_000_000) + " milliseconds");

        if (binaryTime > 0) {
            System.out.println("Speedup: ~"
                + (linearTime / Math.max(binaryTime, 1)) + "x faster");
        }

        System.out.println("Binary search steps: ~"
            + (int) (Math.log(perfSize) / Math.log(2)) + " comparisons");
        System.out.println("Linear search steps: " + perfSize + " comparisons");
        System.out.println();
    }
}