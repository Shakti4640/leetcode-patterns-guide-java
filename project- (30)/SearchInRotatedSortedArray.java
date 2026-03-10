import java.util.Arrays;

public class SearchInRotatedSortedArray {

    // ═══════════════════════════════════════════════
    // CORE SOLUTION: Modified Binary Search — Single Pass
    // ═══════════════════════════════════════════════
    public static int search(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            // ── Step 1: Check if mid IS the target ──
            if (nums[mid] == target) {
                return mid;
            }

            // ── Step 2: Determine which half is sorted ──
            if (nums[left] <= nums[mid]) {

                // LEFT half [left..mid] is sorted
                // ── Step 3: Is target in the sorted left half? ──
                if (nums[left] <= target && target < nums[mid]) {
                    // Target is within sorted left range
                    right = mid - 1;
                } else {
                    // Target is NOT in sorted left range → must be in right
                    left = mid + 1;
                }

            } else {

                // RIGHT half [mid..right] is sorted
                // ── Step 3: Is target in the sorted right half? ──
                if (nums[mid] < target && target <= nums[right]) {
                    // Target is within sorted right range
                    left = mid + 1;
                } else {
                    // Target is NOT in sorted right range → must be in left
                    right = mid - 1;
                }
            }
        }

        // Target not found
        return -1;
    }

    // ═══════════════════════════════════════════════
    // TRACE VERSION: See every decision step
    // ═══════════════════════════════════════════════
    public static int searchWithTrace(int[] nums, int target) {

        System.out.println("Array:  " + Arrays.toString(nums));
        System.out.println("Target: " + target);
        System.out.println("──────────────────────────────────────────");

        int left = 0;
        int right = nums.length - 1;
        int step = 1;

        while (left <= right) {

            int mid = left + (right - left) / 2;

            System.out.println("Step " + step + ":");
            System.out.println("  left=" + left
                    + " (val=" + nums[left] + ")"
                    + "  mid=" + mid
                    + " (val=" + nums[mid] + ")"
                    + "  right=" + right
                    + " (val=" + nums[right] + ")");

            // Visualize the search window
            StringBuilder visual = new StringBuilder("  [");
            for (int i = 0; i < nums.length; i++) {
                if (i == left && i == mid && i == right) {
                    visual.append("(LMR)").append(nums[i]);
                } else if (i == left && i == mid) {
                    visual.append("(LM)").append(nums[i]);
                } else if (i == mid && i == right) {
                    visual.append("(MR)").append(nums[i]);
                } else if (i == left) {
                    visual.append("(L)").append(nums[i]);
                } else if (i == mid) {
                    visual.append("(M)").append(nums[i]);
                } else if (i == right) {
                    visual.append("(R)").append(nums[i]);
                } else if (i > left && i < right) {
                    visual.append(nums[i]);
                } else {
                    visual.append("·");
                }
                if (i < nums.length - 1) visual.append(", ");
            }
            visual.append("]");
            System.out.println(visual);

            if (nums[mid] == target) {
                System.out.println("  → nums[mid]=" + nums[mid]
                        + " == target → FOUND at index " + mid);
                return mid;
            }

            if (nums[left] <= nums[mid]) {
                System.out.println("  → Left half sorted: ["
                        + nums[left] + ".." + nums[mid] + "]");

                if (nums[left] <= target && target < nums[mid]) {
                    System.out.println("  → Target " + target
                            + " is in [" + nums[left] + ", " + nums[mid]
                            + ") → go LEFT");
                    right = mid - 1;
                } else {
                    System.out.println("  → Target " + target
                            + " is NOT in [" + nums[left] + ", " + nums[mid]
                            + ") → go RIGHT");
                    left = mid + 1;
                }

            } else {
                System.out.println("  → Right half sorted: ["
                        + nums[mid] + ".." + nums[right] + "]");

                if (nums[mid] < target && target <= nums[right]) {
                    System.out.println("  → Target " + target
                            + " is in (" + nums[mid] + ", " + nums[right]
                            + "] → go RIGHT");
                    left = mid + 1;
                } else {
                    System.out.println("  → Target " + target
                            + " is NOT in (" + nums[mid] + ", " + nums[right]
                            + "] → go LEFT");
                    right = mid - 1;
                }
            }

            System.out.println();
            step++;
        }

        System.out.println("  → Search space exhausted → NOT FOUND");
        return -1;
    }

    // ═══════════════════════════════════════════════
    // ALTERNATIVE: Find Pivot First, Then Binary Search
    // (Two-pass approach — works but less elegant)
    // ═══════════════════════════════════════════════
    public static int searchTwoPass(int[] nums, int target) {

        int n = nums.length;

        // Step 1: Find the pivot (index of minimum element)
        int pivot = findPivot(nums);

        // Step 2: Determine which half to search
        if (target >= nums[pivot] && target <= nums[n - 1]) {
            // Target is in the right sorted portion [pivot..n-1]
            return binarySearch(nums, pivot, n - 1, target);
        } else {
            // Target is in the left sorted portion [0..pivot-1]
            return binarySearch(nums, 0, pivot - 1, target);
        }
    }

    private static int findPivot(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        // If not rotated (or single element)
        if (nums[left] <= nums[right]) return 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Found the pivot point
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return mid + 1;
            }
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                return mid;
            }

            if (nums[left] <= nums[mid]) {
                // Pivot is in right half
                left = mid + 1;
            } else {
                // Pivot is in left half
                right = mid - 1;
            }
        }

        return 0;
    }

    private static int binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Find Minimum Element in Rotated Array
    // (Related problem — uses similar modified binary search)
    // ═══════════════════════════════════════════════
    public static int findMin(int[] nums) {

        int left = 0;
        int right = nums.length - 1;

        // Already sorted (no rotation or full rotation)
        if (nums[left] <= nums[right]) return nums[left];

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Found the drop point
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                return nums[mid];
            }

            // Decide which half has the minimum
            if (nums[left] <= nums[mid]) {
                // Left is sorted → minimum is in right half
                left = mid + 1;
            } else {
                // Right is sorted → minimum is in left half
                right = mid - 1;
            }
        }

        return nums[0]; // fallback (shouldn't reach here)
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Find Maximum Element in Rotated Array
    // ═══════════════════════════════════════════════
    public static int findMax(int[] nums) {

        int left = 0;
        int right = nums.length - 1;

        // Not rotated
        if (nums[left] <= nums[right]) return nums[right];

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Found the drop point — max is just before the drop
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return nums[mid];
            }

            if (nums[left] <= nums[mid]) {
                // Left is sorted → max might be at mid or in right half
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return nums[0];
    }

    // ═══════════════════════════════════════════════
    // VARIANT: Count Rotations (Find Rotation Count)
    // Rotation count = index of minimum element
    // ═══════════════════════════════════════════════
    public static int countRotations(int[] nums) {

        int left = 0;
        int right = nums.length - 1;

        // Not rotated
        if (nums[left] <= nums[right]) return 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Check if mid is the minimum
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                return mid;
            }
            if (mid < nums.length - 1 && nums[mid] > nums[mid + 1]) {
                return mid + 1;
            }

            if (nums[left] <= nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return 0;
    }

    // ═══════════════════════════════════════════════
    // COMPARISON: Standard Binary Search vs Modified
    // Shows exactly where the logic diverges
    // ═══════════════════════════════════════════════
    public static void compareApproaches(int[] nums, int target) {

        System.out.println("Array:  " + Arrays.toString(nums));
        System.out.println("Target: " + target);
        System.out.println();

        // Standard binary search (wrong on rotated array)
        int standardResult = standardBinarySearch(nums, target);
        System.out.println("Standard Binary Search: " +
                (standardResult == -1 ? "NOT FOUND" : "index " + standardResult));

        // Modified binary search (correct on rotated array)
        int modifiedResult = search(nums, target);
        System.out.println("Modified Binary Search: " +
                (modifiedResult == -1 ? "NOT FOUND" : "index " + modifiedResult));

        // Two-pass approach
        int twoPassResult = searchTwoPass(nums, target);
        System.out.println("Two-Pass Approach:      " +
                (twoPassResult == -1 ? "NOT FOUND" : "index " + twoPassResult));

        // Verify
        int actual = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                actual = i;
                break;
            }
        }
        System.out.println("Linear Scan (ground truth): " +
                (actual == -1 ? "NOT FOUND" : "index " + actual));

        System.out.println("Standard correct? " + (standardResult == actual));
        System.out.println("Modified correct? " + (modifiedResult == actual));
        System.out.println("Two-pass correct? " + (twoPassResult == actual));
    }

    private static int standardBinarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // ─────────────────────────────────────────────
    // MAIN — Run all tests
    // ─────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║  PROJECT 30: Search in Rotated Sorted Array           ║");
        System.out.println("║  Pattern: Modified Binary Search →                    ║");
        System.out.println("║           Sorted-Half Identification →                ║");
        System.out.println("║           Pivot-Aware Elimination                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();

        // ── TEST 1: Basic traced search ──
        System.out.println("═══ TEST 1: Traced Search — Target in Right Half ═══");
        int[] arr1 = {4, 5, 6, 7, 0, 1, 2};
        searchWithTrace(arr1, 0);
        System.out.println();

        // ── TEST 2: Target in left half ──
        System.out.println("═══ TEST 2: Traced Search — Target in Left Half ═══");
        searchWithTrace(arr1, 5);
        System.out.println();

        // ── TEST 3: Target not present ──
        System.out.println("═══ TEST 3: Traced Search — Target Not Present ═══");
        searchWithTrace(arr1, 3);
        System.out.println();

        // ── TEST 4: Target at rotation point ──
        System.out.println("═══ TEST 4: Traced Search — Target at Rotation Point ═══");
        searchWithTrace(arr1, 4);
        System.out.println();

        // ── TEST 5: Compare standard vs modified ──
        System.out.println("═══ TEST 5: Standard vs Modified Binary Search ═══");
        System.out.println("─── Case where standard search FAILS: ───");
        compareApproaches(new int[]{4, 5, 6, 7, 0, 1, 2}, 0);
        System.out.println();
        System.out.println("─── Case where standard search works (by luck): ───");
        compareApproaches(new int[]{4, 5, 6, 7, 0, 1, 2}, 7);
        System.out.println();

        // ── TEST 6: No rotation ──
        System.out.println("═══ TEST 6: No Rotation (Fully Sorted) ═══");
        int[] sorted = {1, 2, 3, 4, 5, 6, 7};
        System.out.println("search(5): " + search(sorted, 5));
        System.out.println("search(1): " + search(sorted, 1));
        System.out.println("search(7): " + search(sorted, 7));
        System.out.println("search(0): " + search(sorted, 0));
        System.out.println();

        // ── TEST 7: Single element ──
        System.out.println("═══ TEST 7: Single Element ═══");
        System.out.println("search([1], 1): " + search(new int[]{1}, 1));
        System.out.println("search([1], 0): " + search(new int[]{1}, 0));
        System.out.println();

        // ── TEST 8: Two elements ──
        System.out.println("═══ TEST 8: Two Elements ═══");
        System.out.println("search([2,1], 1): " + search(new int[]{2, 1}, 1));
        System.out.println("search([2,1], 2): " + search(new int[]{2, 1}, 2));
        System.out.println("search([2,1], 3): " + search(new int[]{2, 1}, 3));
        System.out.println("search([1,2], 1): " + search(new int[]{1, 2}, 1));
        System.out.println("search([1,2], 2): " + search(new int[]{1, 2}, 2));
        System.out.println();

        // ── TEST 9: Various rotation amounts ──
        System.out.println("═══ TEST 9: All Rotation Amounts ═══");
        int[] base = {0, 1, 2, 3, 4, 5, 6};
        int target = 4;

        for (int rot = 0; rot < base.length; rot++) {
            int[] rotated = new int[base.length];
            for (int i = 0; i < base.length; i++) {
                rotated[i] = base[(i + rot) % base.length];
            }
            int result = search(rotated, target);
            System.out.println("  Rotation " + rot + ": "
                    + Arrays.toString(rotated)
                    + " → search(" + target + ")=" + result
                    + " (val=" + (result >= 0 ? rotated[result] : "N/A") + ")"
                    + " " + (result >= 0 && rotated[result] == target ? "✓" : "✗"));
        }
        System.out.println();

        // ── TEST 10: Find minimum, maximum, rotation count ──
        System.out.println("═══ TEST 10: Related Variants ═══");
        int[] arr10 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("Array: " + Arrays.toString(arr10));
        System.out.println("Minimum element:  " + findMin(arr10));
        System.out.println("Maximum element:  " + findMax(arr10));
        System.out.println("Rotation count:   " + countRotations(arr10));
        System.out.println();

        int[] arr10b = {11, 13, 15, 17, 2, 5, 7};
        System.out.println("Array: " + Arrays.toString(arr10b));
        System.out.println("Minimum element:  " + findMin(arr10b));
        System.out.println("Maximum element:  " + findMax(arr10b));
        System.out.println("Rotation count:   " + countRotations(arr10b));
        System.out.println();

        // ── TEST 11: Comprehensive correctness ──
        System.out.println("═══ TEST 11: Comprehensive Correctness Check ═══");
        int[] testArr = {15, 18, 22, 35, 3, 5, 7, 10, 12};
        System.out.println("Array: " + Arrays.toString(testArr));

        int passed = 0;
        int total = 0;

        // Search for every element that exists
        for (int i = 0; i < testArr.length; i++) {
            int res = search(testArr, testArr[i]);
            boolean correct = (res == i);
            total++;
            if (correct) passed++;
            if (!correct) {
                System.out.println("  FAIL: search(" + testArr[i]
                        + ") returned " + res + " expected " + i);
            }
        }

        // Search for elements that don't exist
        int[] missing = {0, 1, 2, 4, 6, 8, 11, 13, 16, 20, 25, 40};
        for (int m : missing) {
            int res = search(testArr, m);
            boolean correct = (res == -1);
            total++;
            if (correct) passed++;
            if (!correct) {
                System.out.println("  FAIL: search(" + m
                        + ") returned " + res + " expected -1");
            }
        }

        System.out.println("Passed: " + passed + "/" + total);
        System.out.println();

        // ── TEST 12: Performance ──
        System.out.println("═══ TEST 12: Performance — Large Array ═══");
        int size = 10_000_000;
        int[] largeArr = new int[size];

        // Create sorted array
        for (int i = 0; i < size; i++) {
            largeArr[i] = i * 3;  // 0, 3, 6, 9, ...
        }

        // Rotate by 7 million positions
        int rotateBy = 7_000_000;
        int[] rotatedLarge = new int[size];
        for (int i = 0; i < size; i++) {
            rotatedLarge[i] = largeArr[(i + rotateBy) % size];
        }

        int largeTarget = largeArr[size / 2]; // middle element of original

        // Modified binary search
        long start = System.nanoTime();
        int bsResult = search(rotatedLarge, largeTarget);
        long bsTime = System.nanoTime() - start;

        // Linear search
        start = System.nanoTime();
        int linResult = -1;
        for (int i = 0; i < rotatedLarge.length; i++) {
            if (rotatedLarge[i] == largeTarget) {
                linResult = i;
                break;
            }
        }
        long linTime = System.nanoTime() - start;

        System.out.println("Array size: " + size + " elements");
        System.out.println("Binary search: index=" + bsResult
                + " in " + (bsTime / 1000) + " microseconds");
        System.out.println("Linear search: index=" + linResult
                + " in " + (linTime / 1000) + " microseconds");
        System.out.println("Both correct: " + (bsResult == linResult));
        System.out.println("Speedup: " + (linTime / Math.max(bsTime, 1)) + "x");
    }
}